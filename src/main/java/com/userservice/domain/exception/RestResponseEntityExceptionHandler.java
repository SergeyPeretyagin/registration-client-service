package com.userservice.domain.exception;

import com.userservice.domain.dto.ResponseVerificationDto;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Object> handleConflictException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), CONFLICT, request);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), BAD_REQUEST, request);
    }

    @ExceptionHandler(CountAttemptsException.class)
    protected ResponseEntity<Object> handleCountAttemptsException(RuntimeException ex, WebRequest request) {
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(ex.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    protected ResponseEntity<Object> handleUsernameNotFoundException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), UNAUTHORIZED, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleIncorrectPasswordException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), UNAUTHORIZED, request);
    }


    @ExceptionHandler(InternalServerException.class)
    protected ResponseEntity<Object> handleInternalServerException(RuntimeException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(ValidTokenException.class)
    protected ResponseEntity<Object> validTokenFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        return handleExceptionInternal(ex, null, headers, status, request);
    }

    @ExceptionHandler(CannotCreateTransactionException.class)
    protected ResponseEntity<Object> canFoundException() {
        return ResponseEntity.status(500).body("Server Error");
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                          HttpHeaders headers,
                                                          HttpStatus status,
                                                          WebRequest request) {
        return ResponseEntity.status(400).body("Invalid data");
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        return ResponseEntity.status(400).body("Invalid data");
    }


    @ExceptionHandler(EmailExistsException.class)
    protected ResponseEntity<Object>  emailExistsException() {
        return ResponseEntity.status(409).body("Email already exists");
    }
    @ExceptionHandler(VerificationAcceptException.class)
    protected ResponseEntity<Object>  verificationAcceptException(RuntimeException ex) {
        ResponseVerificationDto responseVerificationDto = new ResponseVerificationDto(ex.getMessage().replaceAll("true|false",""),
                ex.getMessage().contains("true"));
        return ResponseEntity.status(406).body(responseVerificationDto);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object>  validationExceptionException(RuntimeException ex) {
        return ResponseEntity.status(400).body("Invalid data");
    }
    @ExceptionHandler(WrongPasswordException.class)
    protected ResponseEntity<Object>  validationPasswordException(RuntimeException ex) {
        return ResponseEntity.status(409).body("Passwords do not match");
    }
    @ExceptionHandler(NotExistsUserException.class)
    protected ResponseEntity<Object>  notExistsClient(RuntimeException ex) {
        return ResponseEntity.status(404).body("This user does not exist");
    }
    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<Object> handleHttpClient(HttpClientErrorException ex){
        return ResponseEntity.status(ex.getStatusCode()).body(ex.getMessage());
    }
}
