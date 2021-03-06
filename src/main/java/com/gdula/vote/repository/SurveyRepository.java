package com.gdula.vote.repository;

import com.gdula.vote.model.Survey;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * class: Repozytorium ankiety
 * Reprezentuje repozytorium ankiet.
 */

public interface SurveyRepository extends CrudRepository<Survey, String> {
    List<Survey> findAllByIdIn(List<String> surveysIds);
    List<Survey> findAll();
}
