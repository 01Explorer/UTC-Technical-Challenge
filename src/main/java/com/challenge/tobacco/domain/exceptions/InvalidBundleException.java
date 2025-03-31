package com.challenge.tobacco.domain.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidBundleException extends CustomException {
  public InvalidBundleException(HttpStatus status, String message) {
    super(status, message);
  }
}
