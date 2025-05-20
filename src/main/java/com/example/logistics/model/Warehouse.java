package com.example.logistics.model;

import jakarta.persistence.*;

@Entity
public class Warehouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    // getters and setters
}
