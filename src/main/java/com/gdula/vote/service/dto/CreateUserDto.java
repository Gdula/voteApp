package com.gdula.vote.service.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class CreateUserDto {
    @NotBlank
    @Size(min = 3)
    private String id;
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

}
