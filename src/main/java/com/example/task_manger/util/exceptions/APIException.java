package com.example.task_manger.util.exceptions;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;


public class APIException {

    private final LocalDateTime timestamp;
    private final String message;
    private final int status;
    private final HttpStatus error;

    public APIException(  String message,HttpStatus error,int status) {
        this.timestamp = LocalDateTime.now();  // إضافة الـ timestamp
        this.message = message;
        this.status = status;
        this.error = error;
    }


    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public HttpStatus getError() {
        return error;
    }
}