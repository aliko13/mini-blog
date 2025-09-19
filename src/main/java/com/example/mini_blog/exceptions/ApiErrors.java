package com.example.mini_blog.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ApiErrors {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> notFound(IllegalArgumentException ex) {

        // 404 or 400 Exception Handling
        boolean isNotFound = ex.getMessage() != null && ex.getMessage().contains("not found");
        return ResponseEntity.status(isNotFound ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST)
                .body(Map.of("error", ex.getMessage()));
    }
}
