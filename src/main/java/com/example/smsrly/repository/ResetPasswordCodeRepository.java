package com.example.smsrly.repository;

import com.example.smsrly.entity.ResetPasswordCode;
import com.example.smsrly.entity.VerificationEmailCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ResetPasswordCodeRepository extends JpaRepository<ResetPasswordCode, Integer> {

    @Query(value = "SELECT * FROM reset_password_code where verification_code = :verificationCode", nativeQuery = true)
    Optional<ResetPasswordCode> findByCode(String verificationCode);

    @Transactional
    @Modifying
    @Query(value = "UPDATE reset_password_code SET expired = :expired WHERE verification_code = :code", nativeQuery = true)
    void updateConfirmedAt(String code, Boolean expired);

    @Transactional
    @Modifying
    @Query(value = "UPDATE reset_password_code SET expired = :expired WHERE user_id =:userId", nativeQuery = true)
    void confirmAllRequestsCode(int userId, Boolean expired);

    @Query(value = "SELECT user_id FROM reset_password_code where verification_code = :code", nativeQuery = true)
    int getUserIdByVerificationEmailCode(int code);

}
