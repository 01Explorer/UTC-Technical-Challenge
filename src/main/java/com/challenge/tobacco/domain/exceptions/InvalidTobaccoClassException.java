package com.challenge.tobacco.domain.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidTobaccoClassException extends CustomException {
    public InvalidTobaccoClassException(HttpStatus status, String message) {
        super(status, message);
    }
}
