package com.example.smsrly.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Builder
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private double area;
    @Column(name = "floor_number", nullable = false)
    private int floorNumber;
    @Column(name = "bathroom_number", nullable = false)
    private int bathroomNumber;
    @Column(name = "room_number", nullable = false)
    private int roomNumber;
    @Column(nullable = false)
    private double price;
    @Column(nullable = false)
    private double latitude;
    @Column(nullable = false)
    private double longitude;
    @Column(nullable = false)
    private String city;
    @Column(nullable = false)
    private String country;
    @Column(nullable = false)
    private Boolean isSale;
    @OneToMany(mappedBy = "realEstate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RealEstateImages> realEstateImages;

    @Column(name = "date_uploaded", columnDefinition = "DATETIME", nullable = false)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateUploaded;

    @OneToMany(mappedBy = "realEstate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Request> realEstateRequest;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User user;

    @OneToMany(mappedBy = "realEstate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Save> save;
}
