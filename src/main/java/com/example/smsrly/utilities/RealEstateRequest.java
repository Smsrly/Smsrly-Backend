package com.example.smsrly.utilities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class RealEstateRequest {
    @NotBlank(message = "title must be not blank")
    @Pattern(regexp = "^[\\p{L}\\d\\s]{10,100}$", message = "title should be between 10 and 100 characters and only alphabetical characters, numbers and spaces")
    private String title;
    @NotBlank(message = "description must be not blank")
    @Pattern(regexp = "^[\\p{L}\\d\\s,]{50,}$", message = "description should be at least 50 characters and only alphabetical characters, numbers, comma and spaces")
    private String description;
    @Min(value = 10, message = "area must be greater than or equal to 10")
    @Max(value = 1000000, message = "area must be less than or equal to 1000000")
    private double area;
    @Min(value = 0, message = "floor_number must be greater than or equal to 0")
    @Max(value = 163, message = "floor_number must be less than or equal to 163")
    @JsonProperty("floor_number")
    private int floorNumber;
    @Min(value = 1, message = "bathroom_number must be greater than or equal to 1")
    @Max(value = 10, message = "bathroom_number must be less than or equal to 10")
    @JsonProperty("bathroom_number")
    private int bathroomNumber;
    @Min(value = 1, message = "room_number must be greater than or equal to 1")
    @Max(value = 20, message = "room_number must be less than or equal to 20")
    @JsonProperty("room_number")
    private int roomNumber;
    @Min(value = 1000, message = "price must be greater than or equal to 1000")
    @Max(value = 1000000000, message = "price must be less than or equal to 1000000000")
    private double price;
    @Min(value = -90, message = "latitude must be greater than or equal to -90")
    @Max(value = 90, message = "latitude must be less than or equal to 90")
    private double latitude;
    @Min(value = -180, message = "longitude must be greater than or equal to -180")
    @Max(value = 180, message = "longitude must be less than or equal to 180")
    private double longitude;
    @NotBlank(message = "city must be not blank")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "city field allowed only alphabetical characters and spaces")
    private String city;
    @NotBlank(message = "country must be not blank")
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "country field allowed only alphabetical characters and spaces")
    private String country;
    @JsonProperty("is_sale")
    private Boolean isSale;
}
