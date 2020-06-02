package com.gdula.vote.service.dto;

import com.gdula.vote.model.Question;
import com.gdula.vote.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String id;
    private String login;
    private String name;
    private String surname;
    private String mail;
    private Question question;

}
