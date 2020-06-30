package com.gdula.vote.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * class: UserNotFound
 * Reprezentuje wyjątek użytkownika.
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFound extends Exception {
}
