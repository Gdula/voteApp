package com.gdula.vote.service;

import com.gdula.vote.model.User;
import com.gdula.vote.service.dto.CreateUserDto;
import com.gdula.vote.service.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserDtoMapper {

    public UserDto toDto(User user) {
        return new UserDto(user.getId(), user.getLogin(), user.getName(), user.getSurname(), user.getMail());
    }

    public User toModel(CreateUserDto dto) {
        return new User(dto.getId(), dto.getLogin(), dto.getName(), dto.getSurname(), dto.getMail(), dto.getPassword());
    }
}
