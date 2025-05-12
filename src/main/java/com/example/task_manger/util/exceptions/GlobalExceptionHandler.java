package com.example.task_manger.util.exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.ForbiddenException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentials(InvalidCredentialsException ex) {
        HttpStatus invalidCredentialsStatus = HttpStatus.UNAUTHORIZED;
        APIException errorResponse = new APIException(ex.getMessage(), invalidCredentialsStatus, invalidCredentialsStatus.value());
        return new ResponseEntity<>(errorResponse, invalidCredentialsStatus);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<APIException> handleConflictStatus(ConflictException ex) {
        HttpStatus conflictExceptionStatus = HttpStatus.CONFLICT;
        APIException errorResponse = new APIException(ex.getMessage(), conflictExceptionStatus, conflictExceptionStatus.value());
        return ResponseEntity.status(conflictExceptionStatus).body(errorResponse);
    }


    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) {
        HttpStatus expiredJwtStatus = HttpStatus.UNAUTHORIZED;
        APIException errorResponse = new APIException("JWT token has expired.", expiredJwtStatus, expiredJwtStatus.value());
        return new ResponseEntity<>(errorResponse, expiredJwtStatus);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<Object> handleUnsupportedJwtException(UnsupportedJwtException ex) {
        HttpStatus unsupportedJwtStatus = HttpStatus.UNAUTHORIZED;
        APIException errorResponse = new APIException("Unsupported token.", unsupportedJwtStatus, unsupportedJwtStatus.value());
        return new ResponseEntity<>(errorResponse, unsupportedJwtStatus);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<String> handleMalformedJwtException(MalformedJwtException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("JWT token is invalid.");
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<String> handleSignatureException(SignatureException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("JWT signature is not valid.");
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(TaskNotFoundException ex) {
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
    public ResponseEntity<Map<String, String>> handleUsernameNotFound(UsernameNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", "Unauthorized", "message", ex.getMessage()));
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(UnAuthorizedException ex) {
        HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;
        APIException apiException = new APIException(
                ex.getMessage(),
                unauthorized,
                unauthorized.value()
        );
        return new ResponseEntity<>(apiException, unauthorized);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<Object> handleForbiddenException(ForbiddenException ex) {
        HttpStatus forbiddenStatud = HttpStatus.FORBIDDEN;
        APIException apiException = new APIException(
                ex.getMessage(),
                forbiddenStatud,
                forbiddenStatud.value()
        );
        return new ResponseEntity<>(apiException, forbiddenStatud);
    }

    @ExceptionHandler(value = {TooManyRequestException.class})
    public ResponseEntity<Object> handleTooManyRequestException(TooManyRequestException ex) {
        HttpStatus tooManyRequests = HttpStatus.TOO_MANY_REQUESTS;
        APIException apiException = new APIException(
                ex.getMessage(),
                tooManyRequests,
                tooManyRequests.value()
        );
        return new ResponseEntity<>(apiException, tooManyRequests);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + ex.getMessage());
    }
}
