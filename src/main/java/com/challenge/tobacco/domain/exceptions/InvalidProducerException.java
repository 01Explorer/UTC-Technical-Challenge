package com.challenge.tobacco.domain.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidProducerException extends CustomException {
    public InvalidProducerException(HttpStatus status, String message) {
        super(status, message);
    }
}
