package com.gdula.vote.service.dto;

import com.gdula.vote.model.Question;
import com.gdula.vote.model.Survey;
import com.gdula.vote.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * class: CreateUserDto
 * Reprezentuje dto służące do stworzenia użytkownika.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {
    @NotBlank
    @Size(min = 3)
    private String login;
    @NotBlank
    @Size(min = 2)
    private String name;
    @NotBlank
    @Size(min = 2)
    private String surname;
    @NotBlank
    @Size(min = 4)
    private String mail;
    @NotBlank
    @Size(min = 3)
    private String password;
    private Survey survey;

}
