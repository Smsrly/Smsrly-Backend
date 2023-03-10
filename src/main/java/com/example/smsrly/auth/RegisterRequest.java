package com.example.smsrly.auth;


import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private int id;
    private String image;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private long phoneNumber;
    private double latitude;
    private double longitude;

}
