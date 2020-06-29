package com.gdula.vote.service.mapper;

import com.gdula.vote.model.Question;
import com.gdula.vote.service.dto.CreateUpdateQuestionDto;
import com.gdula.vote.service.dto.QuestionDto;
import com.gdula.vote.service.dto.VariantDto;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.UUID;

@Service
public class QuestionDtoMapper {
    public QuestionDto toDto(Question question, List<VariantDto> variantDto) {
        return new QuestionDto(question.getId(), question.getQuestionText(), question.getSurvey(), variantDto);
    }

    public Question toModel(CreateUpdateQuestionDto dto) {
        String randomId = UUID.randomUUID().toString();

        return new Question(randomId, dto.getQuestionText(), dto.getVariants(), dto.getSurvey());
    }

}
