package com.example.smsrly.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "date_created", columnDefinition = "DATETIME", nullable = false)
    private LocalDateTime dateCreated;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "real_estate_id")
    private RealEstate realEstate;

    public RealEstate getRealEstate() {
        return realEstate;
    }

    public void setRealEstate(RealEstate realEstate) {
        this.realEstate = realEstate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
