package com.example.smsrly.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private boolean isExpired;

    @Column(name = "expire_date", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime expireDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
