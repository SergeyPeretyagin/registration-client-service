package com.userservice.domain.exception;

public class CountAttemptsException extends RuntimeException{
    public CountAttemptsException(String message) {
        super(message);
    }
}
