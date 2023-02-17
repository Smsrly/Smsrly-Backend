package com.example.smsrly.entity;

import jakarta.persistence.*;

@Entity
@Table
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String messageType;
    @Column(nullable = false)
    private String messageContent;
    @Column(columnDefinition = "LONGTEXT")
    private String image;

}
