package com.gdula.vote.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * class: UserDataInvalid
 * Reprezentuje wyjątek użytkownika.
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserDataInvalid extends Exception {
}
