package com.gdula.vote.service.dto;

import com.gdula.vote.model.Question;
import com.gdula.vote.model.Survey;
import com.gdula.vote.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * class: UpdateUserDto
 * Reprezentuje dto użytkownika służące do aktualizacji.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {
    @NotBlank
    @Size(min = 3)
    private String name;
    @NotBlank
    @Size(min = 3)
    private String surname;
    @NotBlank
    @Size(min = 3)
    private String mail;
    @NotBlank
    @Size(min = 8)
    private String password;
    private Survey survey;

}
