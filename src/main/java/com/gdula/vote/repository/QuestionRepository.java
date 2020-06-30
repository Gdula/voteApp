package com.gdula.vote.repository;

import com.gdula.vote.model.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * class: Repozytorium pytania
 * Reprezentuje repozytorium pytań.
 */
@Repository
public interface QuestionRepository extends CrudRepository<Question, String> {
    List<Question> findAllByIdIn(List<String> questionIds);
    List<Question> findAll();
}
