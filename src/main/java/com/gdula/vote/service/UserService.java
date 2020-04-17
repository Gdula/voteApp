package com.gdula.vote.service;

import com.gdula.vote.model.User;
import com.gdula.vote.repository.UserRepository;
import com.gdula.vote.service.dto.CreateUserDto;
import com.gdula.vote.service.dto.UpdateUserDto;
import com.gdula.vote.service.dto.UserDto;
import com.gdula.vote.service.exception.UserNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDtoMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDto createUser(CreateUserDto dto) {
        User userToSave = mapper.toModel(dto);

        String hashedPass = passwordEncoder.encode(userToSave.getPassword());
        userToSave.setPassword(hashedPass);

        User savedUser = userRepository.save(userToSave);

        return mapper.toDto(savedUser);
    }

    public UserDto updateUser(UpdateUserDto dto, String id) throws UserNotFound {
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFound());

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setMail(dto.getMail());

        return mapper.toDto(user);

    }
}
