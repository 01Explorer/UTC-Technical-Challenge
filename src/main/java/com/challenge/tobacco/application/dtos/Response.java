package com.challenge.tobacco.application.dtos;

import com.challenge.tobacco.application.enums.ResponseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record Response(ResponseStatus status, String message, Map<String, Object> data) {
}
