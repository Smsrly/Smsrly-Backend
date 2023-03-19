package com.example.smsrly.service;

import com.example.smsrly.entity.VerificationEmailCode;
import com.example.smsrly.repository.VerificationEmailCodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class VerificationEmailCodeService {

    private VerificationEmailCodeRepository verificationEmailCodeRepository;

    public void saveVerificationCode(VerificationEmailCode verificationEmailCode) {
        verificationEmailCodeRepository.save(verificationEmailCode);
    }

    public int getUserByVerificationEmailCode(int code) {
        return verificationEmailCodeRepository.getUserIdByVerificationEmailCode(code);
    }


    public Optional<VerificationEmailCode> getCode(int code) {
        return verificationEmailCodeRepository.findByCode(code);
    }

    public void setConfirmedAt(int code) {
        verificationEmailCodeRepository.updateConfirmedAt(
                code, LocalDateTime.now());
    }


    public void expireAllRequestsCode(int code) {
        verificationEmailCodeRepository.confirmAllRequestsCode(
                code, LocalDateTime.now());
    }



}
