package com.example.smsrly.dto;


import com.fasterxml.jackson.annotation.JsonProperty;


public record UserRequestDTO(
        String username,
        @JsonProperty("user_number")
        String userPhoneNumber,
        @JsonProperty("user_image")
        String userImageUrl
) {
}
