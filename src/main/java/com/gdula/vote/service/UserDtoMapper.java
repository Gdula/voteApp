package com.gdula.vote.service;

import com.gdula.vote.model.User;
import com.gdula.vote.service.dto.CreateUserDto;
import com.gdula.vote.service.dto.UserDto;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDtoMapper {

    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getLogin(), user.getName(), user.getSurname(), user.getMail(), user.getParticipant());
    }

    public User toModel(CreateUserDto dto) {
        String randomId = UUID.randomUUID().toString();

        return new User(randomId, dto.getLogin(), dto.getName(), dto.getSurname(), dto.getMail(), dto.getPassword(), dto.getParticipant());
    }
}
