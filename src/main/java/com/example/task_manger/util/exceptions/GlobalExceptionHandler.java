package com.example.task_manger.util.exceptions;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<APIException> handleInvalidCredentials(InvalidCredentialsException ex) {
        HttpStatus invalidCredentialsStatus = HttpStatus.UNAUTHORIZED;
        APIException errorResponse = new APIException(ex.getMessage(), invalidCredentialsStatus, invalidCredentialsStatus.value());
        return new ResponseEntity<>(errorResponse, invalidCredentialsStatus);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<APIException> handleConflictStatus(ConflictException ex) {
        HttpStatus conflictExceptionStatus = HttpStatus.CONFLICT;
        APIException errorResponse = new APIException(ex.getMessage(), conflictExceptionStatus, conflictExceptionStatus.value());
        return ResponseEntity.status(conflictExceptionStatus).body(errorResponse);
    }


    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<APIException> handleNotFoundException(TaskNotFoundException ex) {
        APIException errorResponse = new APIException(ex.getMessage(), HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }


    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<APIException> handleBadRequestException(BadRequestException ex) {
        HttpStatus badRequestStatus = HttpStatus.BAD_REQUEST;
        APIException errorResponse = new APIException(ex.getMessage(), badRequestStatus, badRequestStatus.value());
        return new ResponseEntity<>(errorResponse, badRequestStatus);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<APIException>  handleUsernameNotFound(UsernameNotFoundException ex) {
        APIException errorResponse = new APIException(ex.getMessage(), HttpStatus.NOT_FOUND, HttpStatus.NOT_FOUND.value());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<APIException> handleUnauthorizedException(UnAuthorizedException ex) {
        HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;
        APIException apiException = new APIException(
                ex.getMessage(),
                unauthorized,
                unauthorized.value()
        );
        return new ResponseEntity<>(apiException, unauthorized);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<APIException> handleForbiddenException(ForbiddenException ex) {
        HttpStatus forbiddenStatud = HttpStatus.FORBIDDEN;
        APIException apiException = new APIException(
                ex.getMessage(),
                forbiddenStatud,
                forbiddenStatud.value()
        );
        return new ResponseEntity<>(apiException, forbiddenStatud);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }
}
