package com.gdula.vote.service;

import com.gdula.vote.model.*;
import com.gdula.vote.repository.AnswerRepository;
import com.gdula.vote.repository.SurveyRepository;
import com.gdula.vote.repository.UserRepository;
import com.gdula.vote.service.dto.CreateUpdateQuestionDto;
import com.gdula.vote.service.dto.CreateUpdateSurveyDto;
import com.gdula.vote.service.dto.SurveyDto;
import com.gdula.vote.service.exception.SurveyDataInvalid;
import com.gdula.vote.service.exception.SurveyNotFound;
import com.gdula.vote.service.exception.UserDataInvalid;
import com.gdula.vote.service.exception.UserNotFound;
import com.gdula.vote.service.mapper.QuestionDtoMapper;
import com.gdula.vote.service.mapper.SurveyDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * class: SecurityUtils
 * Reprezentuje serwis ankiety.
 */

@Service
public class SurveyService {
    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private SurveyDtoMapper mapper;
    @Autowired
    private SecurityUtils securityUtils;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private QuestionDtoMapper questionDtoMapper;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private StringBuilder stringBuilder;
    /**
     * method: createSurvey
     * Tworzy ankietę
     */
    public SurveyDto createSurvey(CreateUpdateSurveyDto dto) throws SurveyDataInvalid {
        if(dto.getName().isEmpty()) {
            throw new SurveyDataInvalid();
        }
        Survey surveyToSave = mapper.toModel(dto);
        Survey savedSurvey = surveyRepository.save(surveyToSave);
        return mapper.toDto(savedSurvey);
    }

    /**
     * method: updateSurvey
     * Aktualizuje ankietę
     */
    public SurveyDto updateSurvey(CreateUpdateSurveyDto dto, String id) throws SurveyNotFound {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new SurveyNotFound());

        survey.setName(dto.getName());
        survey.setParticipants(dto.getParticipants());
        survey.setQuestions(dto.getQuestions());

        Survey savedSurvey = surveyRepository.save(survey);
        return mapper.toDto(savedSurvey);
    }

    /**
     * method: getAllSurveys
     * Zwraca wszystkie ankiety
     */
    public List<SurveyDto> getAllSurveys() {
        return surveyRepository.findAll()
                .stream()
                .map(u -> mapper.toDto(u))
                .collect(Collectors.toList());
    }

    /**
     * method: getSurveyById
     * Zwraca ankietę po ID
     */
    public SurveyDto getSurveyById(String id) throws SurveyNotFound {
        return surveyRepository.findById(id)
                .map(c -> mapper.toDto(c))
                .orElseThrow(() -> new SurveyNotFound());
    }

    /**
     * method: getSurveyById
     * Usuwa ankietę po ID
     */
    public SurveyDto deleteSurveyById(String id) throws SurveyNotFound {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new SurveyNotFound());

        surveyRepository.delete(survey);

        return mapper.toDto(survey);
    }

    /**
     * method: getSurveyById
     * Zapisuje odpowiedzi wybrane przez użytkownika i zwraca hasz dzięki któremu jesteśmy w stanie sprawdzić nasze wybory w ankiecie.
     */
    public String completeSurvey(MultiValueMap<String, String> answers, String id) throws SurveyNotFound, UserNotFound, UserDataInvalid {
        System.out.println(answers);
        List<String> answersValueList = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : answers.entrySet()) {
            answersValueList.add(entry.getValue().get(0));
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (String answer : answersValueList) {
            stringBuilder.append(answer);
        }

        String answersInOneString = stringBuilder.toString();
        String plaintext = answersInOneString;
        MessageDigest m = null;
        try {
            m = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        m.reset();
        m.update(plaintext.getBytes());
        byte[] digest = m.digest();
        BigInteger bigInt = new BigInteger(1,digest);
        String hashtext = bigInt.toString(16);
        while(hashtext.length() < 32 ){
            hashtext = "0"+hashtext;
        }

        String hash = hashtext;
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new SurveyNotFound());

        String userName = securityUtils.getUserName();
        User userToAdd = userRepository.findFirstByLogin(userName);

        if(!survey.getParticipants().stream().filter(user -> user.getId() == userToAdd.getId())
                .findFirst().isPresent()) {
            survey.getParticipants().add(userToAdd);
            userToAdd.setSurvey(survey);
            userRepository.save(userToAdd);
        }

        survey.getQuestions().forEach(question -> {
            if (answers.containsKey(question.getId())) {
                String answerId = answers.get(question.getId()).get(0);
                answerRepository.save(new Answer(
                        new AnswerId(
                            question.getId(),
                            hash,
                            survey.getId()
                        ),
                        answerId
                ));
            }
        });

        return hash;
    }

    /**
     * method: addSurveyQuestion
     * Dodaje pytanie do ankiety
     */
    public SurveyDto addSurveyQuestion(CreateUpdateQuestionDto dto, String id) throws SurveyNotFound {
        Survey surveyToSave = surveyRepository.findById(id).orElseThrow(() -> new SurveyNotFound());

        List<Question> questions = new ArrayList<>(surveyToSave.getQuestions());
        dto.setSurvey(surveyToSave);
        questions.add(questionDtoMapper.toModel(dto));
        surveyToSave.setQuestions(questions);
        surveyRepository.save(surveyToSave);

        return mapper.toDto(surveyToSave);
    }

    /**
     * method: getUserSurveys
     * Zwraca ankiety użytkownika
     */
    public List<Survey> getUserSurveys() {
        String userName = securityUtils.getUserName();
        User user = userRepository.findFirstByLogin(userName);
        return surveyRepository.findAll().stream()
                .filter(survey -> survey.getParticipants().contains(user))
                .collect(Collectors.toList());
    }
}
