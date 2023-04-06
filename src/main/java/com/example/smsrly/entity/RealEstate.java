package com.example.smsrly.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class RealEstate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    @NotBlank(message = "title must be not blank")
    private String title;
    @Column(nullable = false)
    @NotBlank(message = "description must be not blank")
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
    @OneToMany(mappedBy = "realEstate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RealEstateImages> realEstateImages;

    @JsonIgnore
    @Column(name = "date_uploaded", columnDefinition = "DATETIME", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateUploaded;

    @JsonIgnore
    @OneToMany(mappedBy = "realEstate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Request> realEstateRequest;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "realEstate", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Save> save;
}
