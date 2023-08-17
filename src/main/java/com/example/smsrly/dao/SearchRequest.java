package com.example.smsrly.dao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchRequest {

    private String title;
    private Double price;
    private Double area;
    @JsonProperty("floor_number")
    private Integer floorNumber;
    @JsonProperty("bathroom_number")
    private Integer bathroomNumber;
    @JsonProperty("room_number")
    private Integer roomNumber;
    @JsonProperty("is_sale")
    private Boolean isSale;

}
