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
public class AuthenticationRequest {

    @Email(message = "email must be like test@test.com")
    @NotBlank(message = "email must be not blank")
    private String email;
    @NotBlank(message = "password must be not blank")
    private String password;
}
