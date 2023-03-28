package com.example.smsrly.repository;

import com.example.smsrly.entity.VerificationEmailCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface VerificationEmailCodeRepository extends JpaRepository<VerificationEmailCode, Integer> {
    @Query(value = "SELECT * FROM verification_email_code where verification_code = :verificationCode and user_id = :userId", nativeQuery = true)
    Optional<VerificationEmailCode> findByCodeAndUserId(int verificationCode, int userId);

    @Transactional
    @Modifying
    @Query(value = "UPDATE verification_email_code SET confirmed_at = :confirmedAt WHERE verification_code = :code and user_id = :userId", nativeQuery = true)
    void updateConfirmedAt(int code, int userId, LocalDateTime confirmedAt);

    @Query(value = "SELECT * FROM verification_email_code where user_id = :userId and expired_at > :now", nativeQuery = true)
    Optional<VerificationEmailCode> getByUserIdAndExpiredDate(int userId, LocalDateTime now);

}
