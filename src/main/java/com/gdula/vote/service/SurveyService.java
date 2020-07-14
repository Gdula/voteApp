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
import com.gdula.vote.service.mapper.UserDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
    private UserService userService;
    @Autowired
    private UserDtoMapper userDtoMapper;
    @Autowired
    private QuestionDtoMapper questionDtoMapper;
    @Autowired
    private AnswerRepository answerRepository;

    public SurveyDto createSurvey(CreateUpdateSurveyDto dto) throws SurveyDataInvalid {
        if(dto.getName().isEmpty()) {
            throw new SurveyDataInvalid();
        }
        Survey surveyToSave = mapper.toModel(dto);
        Survey savedSurvey = surveyRepository.save(surveyToSave);
        return mapper.toDto(savedSurvey);
    }

    public SurveyDto updateSurvey(CreateUpdateSurveyDto dto, String id) throws SurveyNotFound {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new SurveyNotFound());

        survey.setName(dto.getName());
        survey.setParticipants(dto.getParticipants());
        survey.setQuestions(dto.getQuestions());

        Survey savedSurvey = surveyRepository.save(survey);
        return mapper.toDto(savedSurvey);
    }

    public List<SurveyDto> getAllSurveys() {
        return surveyRepository.findAll()
                .stream()
                .map(u -> mapper.toDto(u))
                .collect(Collectors.toList());
    }

    public SurveyDto getSurveyById(String id) throws SurveyNotFound {
        return surveyRepository.findById(id)
                .map(c -> mapper.toDto(c))
                .orElseThrow(() -> new SurveyNotFound());
    }

    public SurveyDto deleteSurveyById(String id) throws SurveyNotFound {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new SurveyNotFound());

        surveyRepository.delete(survey);

        return mapper.toDto(survey);
    }

    public String completeSurvey(MultiValueMap<String, String> answers, String id) throws SurveyNotFound, UserNotFound, UserDataInvalid {
        String hash = UUID.randomUUID().toString();
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new SurveyNotFound());

        String userName = securityUtils.getUserName();
        User userToAdd = userRepository.findFirstByLogin(userName);

        if(!survey.getParticipants().stream().filter(user -> user.getId() == userToAdd.getId())
                .findFirst().isPresent()) {
            survey.getParticipants().add(userToAdd);
            surveyRepository.save(survey);
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

    public SurveyDto addSurveyQuestion(CreateUpdateQuestionDto dto, String id) throws SurveyNotFound {
        Survey surveyToSave = surveyRepository.findById(id).orElseThrow(() -> new SurveyNotFound());

        List<Question> questions = new ArrayList<>(surveyToSave.getQuestions());
        dto.setSurvey(surveyToSave);
        questions.add(questionDtoMapper.toModel(dto));
        surveyToSave.setQuestions(questions);
        surveyRepository.save(surveyToSave);

        return mapper.toDto(surveyToSave);
    }

    public List<Survey> getUserSurveys() {
        String userName = securityUtils.getUserName();
        User user = userRepository.findFirstByLogin(userName);
        return surveyRepository.findAll().stream()
                .filter(survey -> survey.getParticipants().contains(user))
                .collect(Collectors.toList());
    }
}
