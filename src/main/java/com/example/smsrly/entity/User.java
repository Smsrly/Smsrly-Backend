package com.example.smsrly.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private long phoneNumber;
    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(columnDefinition = "LONGTEXT")
    private String image;

    @OneToMany(targetEntity = RealEstate.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private List<RealEstate> realEstate;

    @OneToMany(targetEntity = Message.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<Message> message;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "save",
            joinColumns = {
                    @JoinColumn(name = "customer_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "real_estate_id", referencedColumnName = "id")
            }
    )
    private Set<RealEstate> save;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Request> userRequest;


}
