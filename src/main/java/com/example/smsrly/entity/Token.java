package com.example.smsrly.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    @NotEmpty(message = "Token must be not empty")
    @NotNull(message = "Token must be not null")
    private String token;
    @Column(nullable = false)
    private boolean isExpired;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
