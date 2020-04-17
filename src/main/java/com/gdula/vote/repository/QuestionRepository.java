package com.gdula.vote.repository;

import com.gdula.vote.model.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepository extends CrudRepository<Question, String> {
    List<Question> findAllByIdIn(List<String> questionIds);
}
