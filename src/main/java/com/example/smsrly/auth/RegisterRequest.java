package com.example.smsrly.auth;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    private String image;
    @NotEmpty(message = "firstname must be not empty")
    @NotNull(message = "firstname must be not null")
    private String firstname;
    @NotEmpty(message = "lastname must be not empty")
    @NotNull(message = "lastname must be not null")
    private String lastname;
    @Email(message = "email must be like test@test.com")
    @NotEmpty(message = "email must be not empty")
    @NotNull(message = "email must be not null")
    private String email;
    @NotEmpty(message = "password must be not empty")
    @NotNull(message = "password must be not null")
    @Size(min = 8, message = "password should have at least 8 characters")
    private String password;
    private long phoneNumber;
    private double latitude;
    private double longitude;

}
