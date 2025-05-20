package com.example.parcel.service;

import com.example.parcel.dto.ParcelDTO;
import com.example.parcel.exception.ResourceNotFoundException;
import com.example.parcel.model.Parcel;
import com.example.parcel.repository.ParcelRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParcelService {
    private final ParcelRepository repo;
    public ParcelService(ParcelRepository repo) { this.repo = repo; }

    public List<Parcel> getAll() { return repo.findAll(); }
    public Parcel getById(Long id) {
        return repo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Parcel not found: " + id));
    }
    public Parcel create(ParcelDTO dto) {
        Parcel p = new Parcel();
        p.setName(dto.getName());
        p.setWeight(dto.getWeight());
        p.setDestination(dto.getDestination());
        p.setStatus(dto.getStatus());
        p.setType(dto.getType());
        p.setWarehouseId(dto.getWarehouseId());
        return repo.save(p);
    }
    public Parcel update(Long id, ParcelDTO dto) {
        Parcel p = getById(id);
        p.setName(dto.getName());
        p.setWeight(dto.getWeight());
        p.setDestination(dto.getDestination());
        p.setStatus(dto.getStatus());
        p.setType(dto.getType());
        p.setWarehouseId(dto.getWarehouseId());
        return repo.save(p);
    }
    public void delete(Long id) { repo.delete(getById(id)); }
}