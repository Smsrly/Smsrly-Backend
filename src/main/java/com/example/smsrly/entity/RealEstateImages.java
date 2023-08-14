package com.example.smsrly.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String name;
    @Column(columnDefinition = "LONGTEXT",name = "real_estate_image_url")
    String realEstateImageURL;
    @ManyToOne
    @JoinColumn(name = "real_estate_id")
    private RealEstate realEstate;

    public RealEstateImages(String name, String realEstateImageURL, RealEstate realEstate) {
        this.name = name;
        this.realEstateImageURL = realEstateImageURL;
        this.realEstate = realEstate;
    }
}
