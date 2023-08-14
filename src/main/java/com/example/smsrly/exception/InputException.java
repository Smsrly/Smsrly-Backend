package com.example.smsrly.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class InputException extends RuntimeException {
    private String message;
}
