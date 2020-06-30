package com.gdula.vote.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * class: QuestionAlreadyExist
 * Reprezentuje wyjątek pytania.
 */

@ResponseStatus(HttpStatus.CONFLICT)
public class QuestionAlreadyExist extends Exception {
}
