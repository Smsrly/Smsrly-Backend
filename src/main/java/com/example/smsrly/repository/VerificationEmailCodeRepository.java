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
    @Query(value = "SELECT * FROM verification_email_code where verification_code = :verificationCode", nativeQuery = true)
    Optional<VerificationEmailCode> findByCode(int verificationCode);

    @Transactional
    @Modifying
    @Query(value = "UPDATE verification_email_code SET confirmed_at = :confirmedAt WHERE verification_code = :code", nativeQuery = true)
    void updateConfirmedAt(int code, LocalDateTime confirmedAt);

    @Transactional
    @Modifying
    @Query(value = "UPDATE verification_email_code SET confirmed_at = :confirmedAt WHERE user_id =:userId", nativeQuery = true)
    void confirmAllRequestsCode(int userId, LocalDateTime confirmedAt);

    @Query(value = "SELECT user_id FROM verification_email_code where verification_code = :code", nativeQuery = true)
    int getUserIdByVerificationEmailCode(int code);

}
