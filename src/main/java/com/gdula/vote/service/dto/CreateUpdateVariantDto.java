package com.gdula.vote.service.dto;

import com.gdula.vote.model.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * class: CreateUpdateVariantDto
 * Reprezentuje dto służące do aktualizacji i stworzenia wariantu.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateVariantDto {
    private String variant;
    private Question question;
    private Integer voteAmount;

}
