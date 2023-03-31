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


    @Transactional
    @Modifying
    @Query(value = "UPDATE reset_password_code SET confirmed_at = :confirmedAt WHERE verification_code = :code and user_id= :userId", nativeQuery = true)
    void updateConfirmedAt(int code, int userId, LocalDateTime confirmedAt);

    @Transactional
    @Modifying
    @Query(value = "UPDATE reset_password_code SET confirmed_at = :confirmedAt WHERE user_id = :userId", nativeQuery = true)
    void confirmAllRequestedCode(int userId, LocalDateTime confirmedAt);

    @Query(value = "SELECT * FROM reset_password_code where verification_code = :code and user_id= :userId", nativeQuery = true)
    Optional<ResetPasswordCode> findByCodeAndUserId(int code, int userId);
}
