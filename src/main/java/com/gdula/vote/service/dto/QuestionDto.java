package com.gdula.vote.service.dto;

import com.gdula.vote.model.Survey;
import com.gdula.vote.model.User;
import com.gdula.vote.model.Variant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * class: QuestionDto
 * Reprezentuje dto pytania.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuestionDto {
    private String id;
    private String questionText;
    private Survey survey;
    private List<Variant> variants = new ArrayList<>();



}
