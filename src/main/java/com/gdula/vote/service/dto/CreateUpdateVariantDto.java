package com.gdula.vote.service.dto;

import com.gdula.vote.model.Question;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUpdateVariantDto {
    private String variant;
    private Question question;

}
