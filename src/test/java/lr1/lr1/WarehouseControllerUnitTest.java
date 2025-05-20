// src/test/java/lr1/lr1/WarehouseControllerUnitTest.java
package lr1.lr1;

import lr1.lr1.controller.WarehouseController;
import lr1.lr1.model.Warehouse;
import lr1.lr1.service.WarehouseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class WarehouseControllerUnitTest {

    @Mock
    WarehouseService warehouseService;

    @InjectMocks
    WarehouseController controller;

    private Warehouse warehouse;

    @BeforeEach
    void setUp() {
        warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setName("TestWarehouse");
    }

    @Test
    void getWarehouse_returnsOk() {
        given(warehouseService.getWarehouseById(1L)).willReturn(warehouse);

        ResponseEntity<Warehouse> resp = controller.getWarehouse(1L);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isEqualTo(warehouse);
    }

    @Test
    void createWarehouse_returnsCreated() {
        given(warehouseService.createWarehouse(warehouse)).willReturn(warehouse);

        ResponseEntity<Warehouse> resp = controller.createWarehouse(warehouse);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(resp.getBody()).isEqualTo(warehouse);
    }

    @Test
    void updateWarehouse_returnsOk() {
        given(warehouseService.updateWarehouse(1L, warehouse)).willReturn(warehouse);

        ResponseEntity<Warehouse> resp = controller.updateWarehouse(1L, warehouse);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(resp.getBody()).isEqualTo(warehouse);
    }

    @Test
    void deleteWarehouse_returnsNoContent() {
        willDoNothing().given(warehouseService).deleteWarehouse(1L);

        ResponseEntity<Void> resp = controller.deleteWarehouse(1L);

        assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(resp.getBody()).isNull();
    }
}
