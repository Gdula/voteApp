package com.gdula.vote.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * class: UserAlreadyExists
 * Reprezentuje wyjątek użytkownika.
 */

@ResponseStatus(HttpStatus.CONFLICT)
public class UserAlreadyExists extends Exception {
}
