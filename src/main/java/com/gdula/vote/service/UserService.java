package com.gdula.vote.service;

import com.gdula.vote.model.User;
import com.gdula.vote.repository.UserRepository;
import com.gdula.vote.service.dto.CreateUserDto;
import com.gdula.vote.service.dto.UpdateUserDto;
import com.gdula.vote.service.dto.UserDto;
import com.gdula.vote.service.exception.UserAlreadyExists;
import com.gdula.vote.service.exception.UserDataInvalid;
import com.gdula.vote.service.exception.UserNotFound;
import com.gdula.vote.service.mapper.UserDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDtoMapper mapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDto createUser(CreateUserDto dto) throws UserDataInvalid, UserAlreadyExists {
        if (dto.getLogin() == null || dto.getLogin().isEmpty()
                || dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new UserDataInvalid();
        }

        if (userRepository.existsByLogin(dto.getLogin())) {
            throw new UserAlreadyExists();
        }
        User userToSave = mapper.toModel(dto);

        String hashedPass = passwordEncoder.encode(userToSave.getPassword());
        userToSave.setPassword(hashedPass);

        User savedUser = userRepository.save(userToSave);

        return mapper.toDto(savedUser);
    }

    public UserDto updateUser(UpdateUserDto dto, String id) throws UserNotFound, UserDataInvalid {
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new UserDataInvalid();
        }

        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFound());

        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setName(dto.getName());
        user.setSurname(dto.getSurname());
        user.setMail(dto.getMail());
        user.setSurvey(dto.getSurvey());

        User savedUser = userRepository.save(user);

        return mapper.toDto(savedUser);

    }

    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(u -> mapper.toDto(u))
                .collect(Collectors.toList());
    }

    public UserDto getUserById(String id) throws UserNotFound {
        return userRepository.findById(id)
                .map(c -> mapper.toDto(c))
                .orElseThrow(() -> new UserNotFound());
    }

    public UserDto deleteUserById(String id) throws UserNotFound {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFound());

        userRepository.delete(user);

        return mapper.toDto(user);
    }


}
