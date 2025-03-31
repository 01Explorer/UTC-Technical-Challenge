package com.challenge.tobacco.domain.exceptions;

import org.springframework.http.HttpStatus;

public class AddressException extends CustomException{
    public AddressException(HttpStatus status, String message) {
        super(status, message);
    }
}
