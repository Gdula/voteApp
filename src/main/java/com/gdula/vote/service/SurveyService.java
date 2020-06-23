package com.gdula.vote.service;

import com.gdula.vote.model.Survey;
import com.gdula.vote.model.User;
import com.gdula.vote.repository.SurveyRepository;
import com.gdula.vote.repository.UserRepository;
import com.gdula.vote.service.dto.CreateUpdateSurveyDto;
import com.gdula.vote.service.dto.SurveyDto;
import com.gdula.vote.service.exception.SurveyDataInvalid;
import com.gdula.vote.service.exception.SurveyNotFound;
import com.gdula.vote.service.exception.UserDataInvalid;
import com.gdula.vote.service.exception.UserNotFound;
import com.gdula.vote.service.mapper.SurveyDtoMapper;
import com.gdula.vote.service.mapper.UserDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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

    public SurveyDto completeSurvey(CreateUpdateSurveyDto dto, String id) throws SurveyNotFound, UserNotFound, UserDataInvalid {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new SurveyNotFound());
        List<User> participants = dto.getParticipants();

        String userName = securityUtils.getUserName();

        System.out.println(userName);

        User userToAdd = userRepository.findFirstByLogin(userName);

        participants.add(userToAdd);
        System.out.println(participants);

        survey.setParticipants(participants);
        survey.setQuestions(dto.getQuestions());

        Survey savedSurvey = surveyRepository.save(survey);
        System.out.println(savedSurvey.getParticipants());
        userToAdd.setSurvey(survey);
        userService.updateUser(userDtoMapper.toUpdateDto(userDtoMapper.toDto(userToAdd)), userToAdd.getId());

        return mapper.toDto(savedSurvey);
    }

}
