package com.example.smsrly.dto;


import com.fasterxml.jackson.annotation.JsonProperty;


public record UserRequestDTO(
        @JsonProperty("user_name")
        String userName,
        @JsonProperty("user_number")
        String userPhoneNumber,
        @JsonProperty("user_image")
        String userImageUrl
) {
}
