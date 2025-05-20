package lr1.lr1.service;

import lr1.lr1.dto.ParcelDTO;
import lr1.lr1.exception.ResourceNotFoundException;
import lr1.lr1.model.Parcel;
import lr1.lr1.model.Warehouse;
import lr1.lr1.repository.ParcelRepository;
import lr1.lr1.repository.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParcelService {

    @Autowired
    private ParcelRepository parcelRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    public List<Parcel> getAllParcels() {
        return parcelRepository.findAll();
    }

    public Parcel getParcelById(Long id) {
        return parcelRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Посилку не знайдено з id: " + id));
    }

    public Parcel createParcel(ParcelDTO dto) {
        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Склад не знайдено з id: " + dto.getWarehouseId()));

        Parcel parcel = new Parcel();
        parcel.setName(dto.getName());
        parcel.setWeight(dto.getWeight());
        parcel.setDestination(dto.getDestination());
        parcel.setStatus(dto.getStatus());
        parcel.setType(dto.getType());
        parcel.setWarehouse(warehouse);

        return parcelRepository.save(parcel);
    }

    public Parcel updateParcel(Long id, ParcelDTO dto) {
        Parcel parcel = getParcelById(id);
        Warehouse warehouse = warehouseRepository.findById(dto.getWarehouseId())
                .orElseThrow(() -> new ResourceNotFoundException("Склад не знайдено з id: " + dto.getWarehouseId()));

        parcel.setName(dto.getName());
        parcel.setWeight(dto.getWeight());
        parcel.setDestination(dto.getDestination());
        parcel.setStatus(dto.getStatus());
        parcel.setType(dto.getType());
        parcel.setWarehouse(warehouse);

        return parcelRepository.save(parcel);
    }

    public void deleteParcel(Long id) {
        Parcel parcel = getParcelById(id);
        parcelRepository.delete(parcel);
    }
}
