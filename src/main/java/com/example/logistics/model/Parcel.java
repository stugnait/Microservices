package com.example.logistics.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
public class Parcel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String recipient;

    @ManyToOne(optional = false)
    private Warehouse warehouse;

    // getters and setters
}
