package com.gdula.vote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
/**
 * class: Pytanie
 * Reprezentuje pytania w ankiecie
 * Ankieta może posiadać wiele pytań
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Question {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(100)")
    private String id;

    @NotBlank
    @Size(min = 3)
    private String questionText;


    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Variant> variants = new ArrayList<>();

    @ManyToOne
    private Survey survey;

}
