package com.example.smsrly.auth;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "firstname must be not blank")
    private String firstname;
    @NotBlank(message = "lastname must be not blank")
    private String lastname;
    @Email(message = "email must be like test@test.com")
    @NotBlank(message = "email must be not blank")
    private String email;
    @NotBlank(message = "password must be not blank")
    @Size(min = 8, message = "password should have at least 8 characters")
    private String password;
    private long phoneNumber;
    private double latitude;
    private double longitude;

}
