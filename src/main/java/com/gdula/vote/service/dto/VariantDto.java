package com.gdula.vote.service.dto;

import com.gdula.vote.model.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * class: VariantDto
 * Reprezentuje dto waraintu.
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VariantDto {
    private String id;
    private String variant;
    private Question question;
    private Integer voteAmount;


}
