package com.example.logistics.controller;

import com.example.logistics.model.Parcel;
import com.example.logistics.service.ParcelService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parcels")
public class ParcelController {
    private final ParcelService parcelService;

    public ParcelController(ParcelService parcelService) {
        this.parcelService = parcelService;
    }

    @PostMapping
    public Parcel create(@RequestBody Parcel parcel) {
        return parcelService.createParcel(parcel);
    }
}
