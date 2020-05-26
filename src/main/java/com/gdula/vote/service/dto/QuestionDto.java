package com.gdula.vote.service.dto;

import com.gdula.vote.model.User;
import com.gdula.vote.model.Variant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private String id;
    private String questionText;
    private List<User> participants = new ArrayList<>();
    private List<Variant> variants = new ArrayList<>();



}
