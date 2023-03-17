package com.example.smsrly.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class ResetPasswordCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int verificationCode;
    private Boolean expired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ResetPasswordCode() {
    }

    public ResetPasswordCode(int verificationCode, Boolean expired, User user) {
        this.verificationCode = verificationCode;
        this.expired = expired;
        this.user = user;
    }
}
