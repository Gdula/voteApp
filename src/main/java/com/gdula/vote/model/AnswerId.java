package com.gdula.vote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AnswerId implements Serializable {
    @Column(length = 100)
    private String questionId;
    @Column(length = 100)
    private String userId;
}
