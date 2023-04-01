package com.example.smsrly.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int verificationCode;
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime createdAt;
    @Column(nullable = false, columnDefinition = "DATETIME")
    private LocalDateTime expiredAt;
    @Column(columnDefinition = "DATETIME")
    private LocalDateTime confirmedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ResetPasswordCode(int verificationCode, LocalDateTime createdAt, LocalDateTime expiredAt, User user) {
        this.verificationCode = verificationCode;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.user = user;
    }
}
