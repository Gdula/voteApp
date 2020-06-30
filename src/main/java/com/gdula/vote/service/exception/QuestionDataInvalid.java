package com.gdula.vote.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * class: QuestionDataInvalid
 * Reprezentuje wyjątek pytania.
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class QuestionDataInvalid extends Exception {
}
