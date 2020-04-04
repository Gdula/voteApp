package com.gdula.vote.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

public class Form {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "varchar(100)")
    private String id;

    @ManyToMany
    Set<User> users = new HashSet<>();

}
