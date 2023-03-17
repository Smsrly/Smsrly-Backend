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

    public void saveVerificationCode(ResetPasswordCode verificationEmailCode) {
        resetPasswordCodeRepository.save(verificationEmailCode);
    }

    public int getUserByResetPasswordCode(int code) {
        return resetPasswordCodeRepository.getUserIdByVerificationEmailCode(code);
    }


    public Optional<ResetPasswordCode> getCode(String code) {
        return resetPasswordCodeRepository.findByCode(code);
    }

    public void setExpired(String code) {
        resetPasswordCodeRepository.updateConfirmedAt(
                code, true);
    }


    public void expireAllRequestsCode(int code) {
        resetPasswordCodeRepository.confirmAllRequestsCode(
                code, true);
    }


}
