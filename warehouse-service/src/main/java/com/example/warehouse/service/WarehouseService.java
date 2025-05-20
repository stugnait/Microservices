package com.example.warehouse.service;

import com.example.warehouse.dto.WarehouseDTO;
import com.example.warehouse.exception.ResourceNotFoundException;
import com.example.warehouse.model.Warehouse;
import com.example.warehouse.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {
    private final WarehouseRepository repo;
    public WarehouseService(WarehouseRepository repo) { this.repo = repo; }

    public List<Warehouse> getAll() { return repo.findAll(); }
    public Warehouse getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Warehouse not found: " + id));
    }
    public Warehouse create(WarehouseDTO dto) {
        Warehouse w = new Warehouse();
        w.setName(dto.getName());
        w.setLocation(dto.getLocation());
        w.setCapacity(dto.getCapacity());
        return repo.save(w);
    }
    public Warehouse update(Long id, WarehouseDTO dto) {
        Warehouse w = getById(id);
        w.setName(dto.getName());
        w.setLocation(dto.getLocation());
        w.setCapacity(dto.getCapacity());
        return repo.save(w);
    }
    public void delete(Long id) { repo.delete(getById(id)); }
}