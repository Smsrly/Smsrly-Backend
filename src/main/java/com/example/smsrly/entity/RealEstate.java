package com.example.smsrly.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table
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

    @Column(columnDefinition = "LONGTEXT")
    private String image;

    @ManyToMany(mappedBy = "save", fetch = FetchType.LAZY)
    private Set<User> save;

    @OneToMany(mappedBy = "realEstate", cascade = CascadeType.ALL)
    private Set<Request> realEstateRequest;


}
