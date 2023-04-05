package com.example.smsrly.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RealEstateResponse {

    private int id;
    private String title;
    private String description;
    private double area;
    private int floorNumber;
    private int bathroomNumber;
    private int roomNumber;
    private double price;
    private double latitude;
    private double longitude;
    private OwnerInfo ownerInfo;

}
