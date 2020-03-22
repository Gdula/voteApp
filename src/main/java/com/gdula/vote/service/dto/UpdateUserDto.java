package com.gdula.vote.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
}
