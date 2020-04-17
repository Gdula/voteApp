package com.gdula.vote.service;

import com.gdula.vote.model.Question;
import com.gdula.vote.model.User;
import com.gdula.vote.repository.QuestionRepository;
import com.gdula.vote.service.dto.CreateUserDto;
import com.gdula.vote.service.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDtoMapper {
    @Autowired
    private QuestionRepository questionRepository;

    public UserDto toDto(User user) {
        List<String> questionIds = user.getQuestions().stream().map(c -> c.getId()).collect(Collectors.toList());
        return new UserDto(user.getId(), user.getLogin(), user.getName(), user.getSurname(), user.getMail(), questionIds);
    }

    public User toModel(CreateUserDto dto) {
        List<Question> questions = questionRepository.findAllByIdIn(dto.getOwnedQuestionIds());

        return new User(dto.getId(), dto.getLogin(), dto.getName(), dto.getSurname(), dto.getMail(), dto.getPassword(), questions);
    }
}
