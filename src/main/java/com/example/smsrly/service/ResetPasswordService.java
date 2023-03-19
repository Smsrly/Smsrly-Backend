package com.example.smsrly.service;

import com.example.smsrly.entity.ResetPasswordCode;
import com.example.smsrly.repository.ResetPasswordCodeRepository;
import lombok.AllArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ResetPasswordService {
    private final ResetPasswordCodeRepository resetPasswordCodeRepository;



    public int getUserByResetPasswordCode(int code) {
        return resetPasswordCodeRepository.getUserIdByVerificationEmailCode(code);
    }


    public Optional<ResetPasswordCode> getCode(int code) {
        return resetPasswordCodeRepository.findByCode(code);
    }

    public void setExpired(int code) {
        resetPasswordCodeRepository.updateConfirmedAt(
                code, true);
    }


    public void expireAllRequestsCode(int code) {
        resetPasswordCodeRepository.confirmAllRequestsCode(
                code, true);
    }


}
