package com.gdula.vote.repository;

import com.gdula.vote.model.Variant;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * class: Repozytorium wariantu
 * Reprezentuje repozytorium wariantu.
 */

@Repository
public interface VariantRepository extends CrudRepository<Variant, String> {
    List<Variant> findAllByIdIn(List<String> questionsIds);
    List<Variant> findAll();
}
