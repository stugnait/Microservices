package lr1.lr1.mapper;

import lr1.lr1.dto.ParcelDTO;
import lr1.lr1.model.Parcel;
import lr1.lr1.model.Warehouse;

public class ParcelMapper {

    public static ParcelDTO toDTO(Parcel parcel) {
        ParcelDTO dto = new ParcelDTO();
        dto.setName(parcel.getName());
        dto.setWeight(parcel.getWeight());
        dto.setDestination(parcel.getDestination());
        dto.setStatus(parcel.getStatus());
        dto.setType(parcel.getType());
        dto.setWarehouseId(parcel.getWarehouse() != null ? parcel.getWarehouse().getId() : null);
        return dto;
    }
}
