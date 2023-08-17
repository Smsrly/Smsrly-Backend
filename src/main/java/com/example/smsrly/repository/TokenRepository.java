package com.example.smsrly.repository;

import com.example.smsrly.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query(value = "SELECT * FROM token where user_id= :userId and is_expired = 0", nativeQuery = true)
    List<Token> findAllValidTokens(long userId);

//    @Query(value = "SELECT * FROM token where token = :token", nativeQuery = true)
    Optional<Token> findByToken(String token);

    @Query(value = "SELECT * FROM token where expire_date <= :now", nativeQuery = true)
    List<Token> findAllExpiredTokenByTime(LocalDateTime now);
}
