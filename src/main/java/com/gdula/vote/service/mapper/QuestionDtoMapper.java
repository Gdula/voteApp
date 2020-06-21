package com.gdula.vote.service.mapper;

import com.gdula.vote.model.Question;
import com.gdula.vote.service.dto.CreateUpdateQuestionDto;
import com.gdula.vote.service.dto.QuestionDto;
import org.springframework.stereotype.Service;


import java.util.UUID;

@Service
public class QuestionDtoMapper {
    public QuestionDto toDto(Question question) {
        return new QuestionDto(question.getId(), question.getQuestionText(), question.getSurvey(), question.getVariants());
    }

    public Question toModel(CreateUpdateQuestionDto dto) {
        String randomId = UUID.randomUUID().toString();

        return new Question(randomId, dto.getQuestionText(), dto.getVariants(), dto.getSurvey());
    }

    public CreateUpdateQuestionDto toUpdateDto(QuestionDto dto) {
        return new CreateUpdateQuestionDto(dto.getQuestionText(), dto.getSurvey(), dto.getVariants());
    }



}
