package com.gdula.vote.service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * class: VariantNotFound
 * Reprezentuje wyjątek wariantu.
 */

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VariantNotFound extends Exception{
}
