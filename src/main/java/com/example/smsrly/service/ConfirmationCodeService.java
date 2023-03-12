package com.example.smsrly.service;

import com.example.smsrly.entity.ConfirmationCode;
import com.example.smsrly.repository.ConfirmationCodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationCodeService {

    private ConfirmationCodeRepository confirmationCodeRepository;

    public void saveVerificationCode(ConfirmationCode confirmationCode) {
        confirmationCodeRepository.save(confirmationCode);
    }


    public Optional<ConfirmationCode> getCode(String code) {
        return confirmationCodeRepository.findByCode(code);
    }

    public int setConfirmedAt(String code) {
        return confirmationCodeRepository.updateConfirmedAt(
                code, LocalDateTime.now());
    }

}
