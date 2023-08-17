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
public class AuthorizationRequest {

    @NotBlank(message = "first name must be not blank")
    private String firstname;
    @NotBlank(message = "last name must be not blank")
    private String lastname;
    @NotBlank(message = "email must be not blank")
    @Email(message = "email is not valid")
    private String email;
    @JsonProperty("image_url")
    private String imageURL;

}
