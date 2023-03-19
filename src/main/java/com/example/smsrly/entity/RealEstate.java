package com.example.smsrly.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Table
@Getter
@Setter
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    @Column(columnDefinition = "LONGTEXT")
    private String image;

    @JsonIgnore
    @ManyToMany(mappedBy = "save", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private Set<User> save;

    @JsonIgnore
    @OneToMany(mappedBy = "realEstate", cascade = CascadeType.ALL)
    private Set<Request> realEstateRequest;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User user;


}
