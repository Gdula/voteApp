package com.gdula.vote.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String login;
    private String name;
    private String surname;
    private String mail;
    private List<String> ownedQuestionIds;

}
