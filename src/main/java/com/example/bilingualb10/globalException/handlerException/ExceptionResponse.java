package com.example.bilingualb10.globalException.handlerException;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
public class ExceptionResponse{
    private HttpStatus httpStatus;
    private String message;
    private String exceptionClass;

    public ExceptionResponse(HttpStatus httpStatus, String message, String exceptionClass) {
        this.httpStatus = httpStatus;
        this.message = message;
        this.exceptionClass = exceptionClass;
    }
}