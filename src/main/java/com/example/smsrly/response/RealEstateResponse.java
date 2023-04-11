package com.example.smsrly.response;

import com.example.smsrly.entity.RealEstateImages;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    private String city;
    private String country;
    private Boolean isSale;
    private OwnerInfo ownerInfo;
    private List<RealEstateImages> realEstateImages;

}
