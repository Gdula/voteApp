package com.gdula.vote.service;

import com.gdula.vote.model.Question;
import com.gdula.vote.model.Variant;
import com.gdula.vote.repository.QuestionRepository;
import com.gdula.vote.repository.UserRepository;
import com.gdula.vote.repository.VariantRepository;
import com.gdula.vote.service.dto.CreateUpdateQuestionDto;
import com.gdula.vote.service.dto.CreateUpdateVariantDto;
import com.gdula.vote.service.dto.QuestionDto;
import com.gdula.vote.service.dto.VariantDto;
import com.gdula.vote.service.exception.QuestionDataInvalid;
import com.gdula.vote.service.exception.QuestionNotFound;
import com.gdula.vote.service.exception.VariantDataInvalid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private QuestionRepository questionRepository;
    private QuestionDtoMapper questionDtoMapper;
    private VariantDtoMapper variantDtoMapper;
    private UserRepository userRepository;
    private VariantService variantService;
    private VariantRepository variantRepository;
    private SecurityUtils securityUtils;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, QuestionDtoMapper questionDtoMapper, UserRepository userRepository, VariantService variantService, VariantDtoMapper variantDtoMapper, VariantRepository variantRepository, SecurityUtils securityUtils) {
        this.questionRepository = questionRepository;
        this.questionDtoMapper = questionDtoMapper;
        this.userRepository = userRepository;
        this.variantService = variantService;
        this.variantDtoMapper = variantDtoMapper;
        this.variantRepository = variantRepository;
        this.securityUtils = securityUtils;
    }

    public List<QuestionDto> getAllQuestions() {
        return questionRepository.findAll()
                .stream()
                .map(q -> questionDtoMapper.toDto(q))
                .collect(Collectors.toList());
    }

    public QuestionDto getQuestionById(String id) throws QuestionNotFound {
        return questionRepository.findById(id)
                .map(q -> questionDtoMapper.toDto(q)).orElseThrow(() -> new QuestionNotFound());
    }

    public QuestionDto deleteQuestionById(String id) throws QuestionNotFound {
        Question question = questionRepository.findById(id).orElseThrow(() -> new QuestionNotFound());
        questionRepository.delete(question);

        return questionDtoMapper.toDto(question);
    }

    public QuestionDto createQuestion(CreateUpdateQuestionDto dto) throws QuestionDataInvalid {
        if(dto.getQuestionText().isEmpty()) {
            throw new QuestionDataInvalid();
        }

        Question questionToSave = questionDtoMapper.toModel(dto);
        questionToSave.setQuestionText(dto.getQuestionText());
        Question savedQuestion = questionRepository.save(questionToSave);

        return questionDtoMapper.toDto(savedQuestion);
    }

    public QuestionDto upadateQuestion(CreateUpdateQuestionDto dto, String id) throws QuestionNotFound {
        Question question = questionRepository.findById(id).orElseThrow(() -> new QuestionNotFound());

        question.setParticipants(dto.getParticipants());
        question.setVariants(dto.getVariants());
        question.setQuestionText(dto.getQuestionText());

        return  questionDtoMapper.toDto(question);
    }

    public List<VariantDto> getAllQuestionVariantsById(String id) throws QuestionNotFound {
        return questionRepository.findById(id).orElseThrow(() -> new QuestionNotFound())
                .getVariants()
                .stream()
                .map(q -> variantDtoMapper.toDto(q))
                .collect(Collectors.toList());

    }

    public QuestionDto addQuestionVariant(CreateUpdateVariantDto dto, String id) throws QuestionNotFound, VariantDataInvalid {
        Question questionToSave = questionRepository.findById(id).orElseThrow(() -> new QuestionNotFound());

        List<Variant> variants = new ArrayList<>(questionRepository.findById(id).orElseThrow(() -> new QuestionNotFound())
                .getVariants());

        dto.setQuestion(questionToSave);
        variants.add(variantDtoMapper.toModel(dto));
        questionToSave.setVariants(variants);
        questionRepository.save(questionToSave);

        return questionDtoMapper.toDto(questionToSave);
    }


}
