package com.gdula.vote.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * class: QuestionNotFound
 * Reprezentuje wyjątek pytania.
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class QuestionNotFound extends Exception{
}
