package com.gdula.vote.service;

import com.gdula.vote.model.Question;
import com.gdula.vote.model.User;
import com.gdula.vote.repository.QuestionRepository;
import com.gdula.vote.repository.UserRepository;
import com.gdula.vote.service.dto.CreateUpdateQuestionDto;
import com.gdula.vote.service.dto.QuestionDto;
import com.gdula.vote.service.dto.UpdateUserDto;
import com.gdula.vote.service.exception.QuestionDataInvalid;
import com.gdula.vote.service.exception.QuestionNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {
    private QuestionRepository questionRepository;
    private QuestionDtoMapper mapper;
    private UserRepository userRepository;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, QuestionDtoMapper mapper, UserRepository userRepository) {
        this.questionRepository = questionRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    public List<QuestionDto> getAllQuestions() {
        return questionRepository.findAll()
                .stream()
                .map(q -> mapper.toDto(q))
                .collect(Collectors.toList());
    }

    public QuestionDto getQuestionById(String id) throws QuestionNotFound {
        return questionRepository.findById(id)
                .map(q -> mapper.toDto(q)).orElseThrow(() -> new QuestionNotFound());
    }

    public QuestionDto deleteQuestionById(String id) throws QuestionNotFound {
        Question question = questionRepository.findById(id).orElseThrow(() -> new QuestionNotFound());
        questionRepository.delete(question);

        return mapper.toDto(question);
    }

    public QuestionDto createQuestion(CreateUpdateQuestionDto dto) throws QuestionDataInvalid {
        if(dto.getQuestionText().isEmpty()) {
            throw new QuestionDataInvalid();
        }

        Question questionToSave = mapper.toModel(dto);
        questionToSave.setQuestionText(dto.getQuestionText());
        Question savedQuestion = questionRepository.save(questionToSave);

        return mapper.toDto(savedQuestion);
    }

    public QuestionDto upadateQuestion(CreateUpdateQuestionDto dto, String id) throws QuestionNotFound {
        Question question = questionRepository.findById(id).orElseThrow(() -> new QuestionNotFound());

        question.setParticipants(dto.getParticipants());
        question.setVariants(dto.getVariants());
        question.setQuestionText(dto.getQuestionText());

        return  mapper.toDto(question);
    }


}
