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

    public Optional<VerificationEmailCode> getCode(int code, int userId) {
        return verificationEmailCodeRepository.findByCodeAndUserId(code, userId);
    }

    public void setConfirmedAt(int code, int userId) {
        verificationEmailCodeRepository.updateConfirmedAt(
                code, userId, LocalDateTime.now());
    }

    public Optional<VerificationEmailCode> checkIfCodeExpiredOrNot(int userId) {
        return verificationEmailCodeRepository.getByUserIdAndExpiredDate(userId, LocalDateTime.now());
    }

}
