package com.challenge.tobacco.domain.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidTransactionException extends CustomException {
  public InvalidTransactionException(HttpStatus status, String message) {
    super(status, message);
  }
}
