package com.example.logistics.service;

import com.example.logistics.model.Parcel;
import com.example.logistics.repository.ParcelRepository;
import com.example.logistics.repository.WarehouseRepository;
import org.springframework.stereotype.Service;

@Service
public class ParcelService {
    private final ParcelRepository parcelRepository;
    private final WarehouseRepository warehouseRepository;

    public ParcelService(ParcelRepository parcelRepository, WarehouseRepository warehouseRepository) {
        this.parcelRepository = parcelRepository;
        this.warehouseRepository = warehouseRepository;
    }

    public Parcel createParcel(Parcel parcel) {
        if (parcel.getWarehouse() == null) {
            throw new IllegalArgumentException("Warehouse must be provided");
        }
        return parcelRepository.save(parcel);
    }
}
