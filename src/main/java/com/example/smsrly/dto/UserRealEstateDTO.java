package com.example.smsrly.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Set;

public record UserRealEstateDTO(
        Long id,
        String title,
        String description,
        double area,
        @JsonProperty("floor_number")
        int floorNumber,
        @JsonProperty("bathroom_number")
        int bathroomNumber,
        @JsonProperty("room_number")
        int roomNumber,
        double price,
        double latitude,
        double longitude,
        String city,
        String country,
        @JsonProperty("is_sale")
        Boolean isSale,
        @JsonProperty("images")
        List<String> realEstateImages,
        Set<UserRequestDTO> requests
) {
}
