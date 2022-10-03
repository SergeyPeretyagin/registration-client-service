package com.userservice.domain.exception;

/**
 * Для неверного ввода нового пароля
 */
public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException(String message) {
        super(message);
    }
}
