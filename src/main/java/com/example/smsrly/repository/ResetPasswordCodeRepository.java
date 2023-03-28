package com.example.smsrly.repository;

import com.example.smsrly.entity.ResetPasswordCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ResetPasswordCodeRepository extends JpaRepository<ResetPasswordCode, Integer> {


    @Transactional
    @Modifying
    @Query(value = "UPDATE reset_password_code SET expired = :expired WHERE verification_code = :code and user_id= :userId", nativeQuery = true)
    void updateConfirmedAt(int code, int userId, Boolean expired);

    @Transactional
    @Modifying
    @Query(value = "UPDATE reset_password_code SET expired = :expired WHERE user_id =:userId", nativeQuery = true)
    void confirmAllRequestedCode(int userId, Boolean expired);

    @Query(value = "SELECT * FROM reset_password_code where verification_code = :code and user_id= :userId", nativeQuery = true)
    Optional<ResetPasswordCode> findByCodeAndUserId(int code, int userId);
}
