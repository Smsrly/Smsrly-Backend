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
    @JsonIgnore
    @Column(nullable = false, unique = true)
    private String name;
    @Column(columnDefinition = "LONGTEXT")
    String realEstateImageURL;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "real_estate_id")
    private RealEstate realEstate;

    public RealEstateImages(String name, String realEstateImageURL, RealEstate realEstate) {
        this.name = name;
        this.realEstateImageURL = realEstateImageURL;
        this.realEstate = realEstate;
    }
}
