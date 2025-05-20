package lr1.lr1.controller;

import lr1.lr1.model.Warehouse;
import lr1.lr1.service.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Warehouse> getWarehouse(@PathVariable Long id) {
        return ResponseEntity.ok(warehouseService.getWarehouseById(id));
    }

    @PostMapping
    public ResponseEntity<Warehouse> createWarehouse(@RequestBody Warehouse warehouse) {
        return ResponseEntity.status(HttpStatus.CREATED).body(warehouseService.createWarehouse(warehouse));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Warehouse> updateWarehouse(@PathVariable Long id, @RequestBody Warehouse warehouse) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(id, warehouse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.noContent().build();
    }
}
