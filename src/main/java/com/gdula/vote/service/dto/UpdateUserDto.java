package com.gdula.vote.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

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
    private List<String> ownedQuestionIds;

}
