package com.example.smsrly.handler;


import com.example.smsrly.exception.InputException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InputException.class)
    public ResponseEntity<Object> handleException(InputException exception) {
        Map<String, Object> error = new HashMap<>();
        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("code", 400);
        errorDetails.put("message", exception.getMessage());
        error.put("error", errorDetails);
        return ResponseEntity.badRequest().body(error);
    }
}
