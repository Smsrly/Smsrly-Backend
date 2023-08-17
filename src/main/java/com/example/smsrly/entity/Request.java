package com.example.smsrly.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "date_created", columnDefinition = "DATETIME", nullable = false)
//    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dateCreated;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "real_estate_id")
    private RealEstate realEstate;

    public Request(LocalDateTime dateCreated, User user, RealEstate realEstate) {
        this.dateCreated = dateCreated;
        this.user = user;
        this.realEstate = realEstate;
    }

}
