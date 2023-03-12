package com.example.smsrly.repository;

import com.example.smsrly.entity.ConfirmationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ConfirmationCodeRepository extends JpaRepository<ConfirmationCode, Integer> {
    @Query(value = "SELECT * FROM confirmation_code where verification_code = :verificationCode", nativeQuery = true)
    Optional<ConfirmationCode> findByCode(String verificationCode);

    @Transactional
    @Modifying
    @Query(value = "UPDATE confirmation_code SET confirmed_at = :confirmedAt WHERE verification_code = :code", nativeQuery = true)
    int updateConfirmedAt(String code, LocalDateTime confirmedAt);

}
