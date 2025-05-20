package lr1.lr1.service;

import lr1.lr1.model.Warehouse;
import lr1.lr1.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    @Autowired
    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    public Warehouse getWarehouseById(Long id) {
        return warehouseRepository.findById(id).orElseThrow();
    }

    public Warehouse createWarehouse(Warehouse warehouse) {
        if (warehouse == null) {
            throw new IllegalArgumentException("Warehouse must not be null");
        }
        return warehouseRepository.save(warehouse);
    }

    public Warehouse updateWarehouse(Long id, Warehouse warehouse) {
        // якщо warehouse == null — впаде NullPointerException, як у тестах
        warehouse.setId(id);
        return warehouseRepository.save(warehouse);
    }

    public void deleteWarehouse(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Id must not be null");
        }
        if (!warehouseRepository.existsById(id)) {
            throw new EmptyResultDataAccessException("Warehouse not found", 1);
        }
        warehouseRepository.deleteById(id);
    }
}
