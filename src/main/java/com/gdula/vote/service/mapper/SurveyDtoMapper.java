package com.gdula.vote.service.mapper;

import com.gdula.vote.model.Survey;
import com.gdula.vote.service.dto.CreateUpdateSurveyDto;
import com.gdula.vote.service.dto.SurveyDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * class: SurveyDtoMapper
 * Reprezentuje mapper dto ankiety.
 */

@Service
public class SurveyDtoMapper {
    public SurveyDto toDto(Survey survey) {
        return new SurveyDto(survey.getId(), survey.getName(), survey.getParticipants(), survey.getQuestions());
    }

    public Survey toModel(CreateUpdateSurveyDto dto) {
        String randomId = UUID.randomUUID().toString();

        return new Survey(randomId, dto.getName(), dto.getParticipants(), dto.getQuestions());
    }
}
