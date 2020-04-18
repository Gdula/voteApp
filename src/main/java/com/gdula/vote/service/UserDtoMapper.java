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
        return new UserDto(user.getId(), user.getLogin(), user.getName(), user.getSurname(), user.getMail(), user.getParticipant());
    }

    public User toModel(CreateUserDto dto) {

        return new User(dto.getId(), dto.getLogin(), dto.getName(), dto.getSurname(), dto.getMail(), dto.getPassword(), dto.getParticipant());
    }
}
