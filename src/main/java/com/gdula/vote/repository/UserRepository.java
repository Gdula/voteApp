package com.gdula.vote.repository;

import com.gdula.vote.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    List<User> findAll();
    Boolean existsByLogin(String string);

    User findFirstByLogin(String username);
}
