package com.gdula.vote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

/**
 * class: OdpowiedźID
 * Reprezentuje odpowiedź w ankiencie
 * Posiada informacje na temat ID question, usera, oraz survey.
 */
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnswerId implements Serializable {
    @Column(length = 50)
    private String questionId;
    @Column(length = 50)
    private String userId;
    @Column(length = 50)
    private String surveyId;
}
