package com.example.smsrly.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OTPRequest {
    @NotBlank(message = "first name must be not blank")
    @JsonProperty("first_name")
    String firstName;
    @NotBlank(message = "last name must be not blank")
    @JsonProperty("last_name")
    String lastName;
    @Email(message = "email must be like test@test.com")
    @NotBlank(message = "email must be not blank")
    String email;
}
