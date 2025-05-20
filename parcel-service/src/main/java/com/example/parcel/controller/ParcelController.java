package com.example.parcel.controller;

import com.example.parcel.dto.ParcelDTO;
import com.example.parcel.model.Parcel;
import com.example.parcel.service.ParcelService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parcels")
public class ParcelController {
    private final ParcelService service;
    public ParcelController(ParcelService service) { this.service = service; }

    @GetMapping
    public List<Parcel> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public Parcel getById(@PathVariable Long id) { return service.getById(id); }

    @PostMapping
    public Parcel create(@RequestBody ParcelDTO dto) { return service.create(dto); }

    @PutMapping("/{id}")
    public Parcel update(@PathVariable Long id, @RequestBody ParcelDTO dto) { return service.update(id, dto); }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}