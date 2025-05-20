package lr1.lr1.controller;

import lr1.lr1.dto.ParcelDTO;
import lr1.lr1.model.Parcel;
import lr1.lr1.service.ParcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parcels")
public class ParcelController {

    @Autowired
    private ParcelService parcelService;

    @GetMapping
    public List<Parcel> getAllParcels() {
        return parcelService.getAllParcels();
    }

    @GetMapping("/{id}")
    public Parcel getParcelById(@PathVariable Long id) {
        return parcelService.getParcelById(id);
    }

    @PostMapping
    public Parcel createParcel(@RequestBody ParcelDTO dto) {
        return parcelService.createParcel(dto);
    }

    @PutMapping("/{id}")
    public Parcel updateParcel(@PathVariable Long id, @RequestBody ParcelDTO dto) {
        return parcelService.updateParcel(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deleteParcel(@PathVariable Long id) {
        parcelService.deleteParcel(id);
    }
}
