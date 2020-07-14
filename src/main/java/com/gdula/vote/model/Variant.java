package com.gdula.vote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * class: Wariant
 * Reprezentuje warianty
 * Do jednego pytania może być przypisane wiele wariantów
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Variant {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(100)")
    private String id;
    private String variant;
    private Integer voteAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Question question;

    @OneToMany(mappedBy = "answerKey", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Answer> answers;
}
