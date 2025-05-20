package com.example.warehouse.controller;

import com.example.warehouse.dto.WarehouseDTO;
import com.example.warehouse.model.Warehouse;
import com.example.warehouse.service.WarehouseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/warehouses")
public class WarehouseController {
    private final WarehouseService service;
    public WarehouseController(WarehouseService service) { this.service = service; }

    @GetMapping
    public List<Warehouse> getAll() { return service.getAll(); }

    @GetMapping("/{id}")
    public Warehouse getById(@PathVariable Long id) { return service.getById(id); }

    @PostMapping
    public Warehouse create(@RequestBody WarehouseDTO dto) { return service.create(dto); }

    @PutMapping("/{id}")
    public Warehouse update(@PathVariable Long id, @RequestBody WarehouseDTO dto) { 
        return service.update(id, dto); 
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) { service.delete(id); }
}