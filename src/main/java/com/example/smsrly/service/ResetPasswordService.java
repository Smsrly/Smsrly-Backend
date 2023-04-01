package com.example.smsrly.service;

import com.example.smsrly.entity.ResetPasswordCode;
import com.example.smsrly.repository.ResetPasswordCodeRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ResetPasswordService {

    private final ResetPasswordCodeRepository resetPasswordCodeRepository;

    public Optional<ResetPasswordCode> getCode(int code, int userId) {
        return resetPasswordCodeRepository.findByCodeAndUserId(code, userId);
    }

    public void setExpired(int code, int userId) {
        resetPasswordCodeRepository.updateConfirmedAt(
                code, userId, LocalDateTime.now());
    }


    public void expireAllRequestedCode(int userId) {
        resetPasswordCodeRepository.confirmAllRequestedCode(
                userId, LocalDateTime.now());
    }


}
