package com.example.smsrly.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table
@Getter
@Setter
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String messageType;
    @Column(nullable = false)
    private String messageContent;
    @Column(columnDefinition = "LONGTEXT")
    private String image;

}
