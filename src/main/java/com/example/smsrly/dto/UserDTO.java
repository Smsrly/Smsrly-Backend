package com.example.smsrly.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record UserDTO(
        @JsonProperty("first_name")
        String firstName,
        @JsonProperty("last_name")
        String lastName,
        String email,
        @JsonProperty("phone_number")
        String phoneNumber,
        Double latitude,
        Double longitude,
        @JsonProperty("image_url")
        String imageURL,
        List<UserRealEstateDTO> uploads,
        List<RealEstateDTO> saves,
        List<RealEstateDTO> requests
) {
}