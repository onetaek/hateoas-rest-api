package com.devframe.global.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class CustomErrorResponse {
    private final HttpStatus httpStatus;
    private final int code;
    private final String message;
    private final Map<String, String> validation;

    @Builder
    public CustomErrorResponse(HttpStatus httpStatus, int code, String message, Map<String, String> validation) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
        this.validation = validation == null ? new HashMap<>() : validation;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
