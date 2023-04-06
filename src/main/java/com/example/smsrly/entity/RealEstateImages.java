package com.example.smsrly.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private int id;
    @Column(columnDefinition = "LONGTEXT")
    String realEstateImageURL;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "real_estate_id")
    private RealEstate realEstate;

    public RealEstateImages(String realEstateImageURL, RealEstate realEstate) {
        this.realEstateImageURL = realEstateImageURL;
        this.realEstate = realEstate;
    }
}
