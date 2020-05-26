package com.gdula.vote.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "varchar(100)")
    private String id;

    @NotBlank
    @Size(min = 3)
    private String questionText;

    @Column(columnDefinition = "varchar(100)")
    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL)
    private List<User> participants = new ArrayList<>();

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Variant> variants = new ArrayList<>();
}
