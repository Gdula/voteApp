package com.gdula.vote.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * class: Użytkownik
 * Reprezentuje użytkowników
 *
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(columnDefinition = "varchar(100)")
    private String id;
    @NotBlank
    @Size(min = 3)
    private String login;
    @NotBlank
    @Size(min = 3)
    private String name;
    @NotBlank
    @Size(min = 3)
    private String surname;
    @NotBlank
    @Size(min = 3)
    private String mail;
    @NotBlank
    @Size(min = 3)
    private String password;

    @OneToMany(mappedBy = "answerId.userId", cascade = CascadeType.REMOVE)
    private List<Answer> answers = new ArrayList<>();

    @ManyToOne
    private Survey survey;


}
