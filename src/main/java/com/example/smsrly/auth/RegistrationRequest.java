package com.example.smsrly.auth;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "first name must be not blank")
    @JsonProperty("first_name")
    private String firstName;
    @NotBlank(message = "last name must be not blank")
    @JsonProperty("last_name")
    private String lastName;
    @Email(message = "email must be like test@test.com")
    @NotBlank(message = "email must be not blank")
    private String email;
    @NotBlank(message = "password must be not blank")
    @Size(min = 8, message = "password should have at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#^_])[A-Za-z\\d@$!%*?&#^_]+$", message = "password should contain at least one uppercase letter, one lowercase letter, one digit, and one special character")
    private String password;
    @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "phone number must be like +20 101 234 5678")
    @JsonProperty("phone_number")
    private String phoneNumber;
    @Min(value = -90, message = "latitude must be greater than or equal to -90")
    @Max(value = 90, message = "latitude must be less than or equal to 90")
    private double latitude;
    @Min(value = -180, message = "longitude must be greater than or equal to -180")
    @Max(value = 180, message = "longitude must be less than or equal to 180")
    private double longitude;

}
