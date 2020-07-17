package com.gdula.vote.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

/**
 * class: Odpowiedź
 * Reprezentuje odpowiedź w ankiecie
 * Ankieta może posiadać wiele odpowiedzi
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Answer {

    @EmbeddedId
    private AnswerId answerId;
    @Column(length = 100)
    private String answerKey;
}
