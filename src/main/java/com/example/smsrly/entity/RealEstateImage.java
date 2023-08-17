package com.example.smsrly.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class RealEstateImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String filename;
    @Column(columnDefinition = "LONGTEXT", name = "image_url")
    String imageURL;
    @ManyToOne
    @JoinColumn(name = "real_estate_id")
    private RealEstate realEstate;

    public RealEstateImage(String filename, String imageURL, RealEstate realEstate) {
        this.filename = filename;
        this.imageURL = imageURL;
        this.realEstate = realEstate;
    }
}
