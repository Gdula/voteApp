package com.gdula.vote.service.dto;

import com.gdula.vote.model.Question;
import com.gdula.vote.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * class: CreateUpdateSurveyDto
 * Reprezentuje dto służące do aktualizacji i stworzenia ankiety.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateSurveyDto {
    private String name;
    private List<User> participants = new ArrayList<>();
    private List<Question> questions = new ArrayList<>();
}
