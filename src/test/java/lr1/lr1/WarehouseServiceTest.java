package lr1.lr1;

import lr1.lr1.model.Warehouse;
import lr1.lr1.repository.WarehouseRepository;
import lr1.lr1.service.WarehouseService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WarehouseServiceTest {

    @Autowired
    private WarehouseRepository repository;

    @Autowired
    private WarehouseService service;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("createWarehouse: при коректній кімнаті повертає згенерований ID")
    void whenCreateWarehouse_thenIdGenerated() {
        Warehouse r = new Warehouse();
        r.setName("Living Warehouse");

        Warehouse saved = service.createWarehouse(r);

        assertNotNull(saved.getId());
        assertEquals(1, repository.count());
        assertEquals("Living Warehouse", saved.getName());
    }

    @Test
    @DisplayName("createWarehouse: при null-кімнаті кидає IllegalArgumentException")
    void whenCreateWarehouseNull_thenThrowsIllegalArg() {
        assertThrows(IllegalArgumentException.class, () -> service.createWarehouse(null));
        assertEquals(0, repository.count());
    }

    @Test
    @DisplayName("getWarehouseById: повертає створену кімнату")
    void whenGetByIdExisting_thenReturnsWarehouse() {
        Warehouse r = new Warehouse();
        r.setName("Kitchen");
        Warehouse saved = repository.save(r);

        Warehouse found = service.getWarehouseById(saved.getId());

        assertEquals(saved.getId(), found.getId());
        assertEquals("Kitchen", found.getName());
    }

    @Test
    @DisplayName("getWarehouseById: при неіснуючому ID кидає NoSuchElementException")
    void whenGetByIdNonExisting_thenThrowsNoSuch() {
        assertThrows(NoSuchElementException.class, () -> service.getWarehouseById(123L));
    }

    @Test
    @DisplayName("repository.findAll: при відсутності кімнат повертає порожній список")
    void whenNoWarehouses_thenFindAllEmpty() {
        List<Warehouse> list = repository.findAll();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("repository.findAll: повертає всі збережені кімнати")
    void whenMultipleWarehouses_thenFindAllReturnsAll() {
        Warehouse r1 = new Warehouse(); r1.setName("A");
        Warehouse r2 = new Warehouse(); r2.setName("B");
        repository.save(r1);
        repository.save(r2);

        List<Warehouse> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(x -> "A".equals(x.getName())));
        assertTrue(all.stream().anyMatch(x -> "B".equals(x.getName())));
    }

    @Test
    @DisplayName("updateWarehouse: змінює поля існуючої кімнати")
    void whenUpdateWarehouse_thenFieldsUpdated() {
        Warehouse orig = new Warehouse(); orig.setName("OldName");
        Warehouse saved = repository.save(orig);

        Warehouse toUpdate = new Warehouse();
        toUpdate.setName("NewName");
        Warehouse updated = service.updateWarehouse(saved.getId(), toUpdate);

        assertEquals(saved.getId(), updated.getId());
        assertEquals("NewName", updated.getName());
        assertEquals(1, repository.count());
    }

    @Test
    @DisplayName("updateWarehouse: при null-кімнаті кидає NullPointerException")
    void whenUpdateNullWarehouse_thenThrowsNullPtr() {
        assertThrows(NullPointerException.class, () -> service.updateWarehouse(1L, null));
        assertEquals(0, repository.count());
    }

    @Test
    @DisplayName("updateWarehouse: при null-ID створює нову кімнату")
    void whenUpdateNullId_thenCreatesNew() {
        Warehouse r = new Warehouse(); r.setName("Guest");
        Warehouse result = service.updateWarehouse(null, r);

        assertNotNull(result.getId());
        assertEquals(1, repository.count());
    }

    @Test
    @DisplayName("deleteWarehouse: видаляє існуючу кімнату")
    void whenDeleteExisting_thenRemoved() {
        Warehouse r = new Warehouse(); r.setName("Office");
        Warehouse saved = repository.save(r);

        service.deleteWarehouse(saved.getId());

        assertFalse(repository.findById(saved.getId()).isPresent());
        assertEquals(0, repository.count());
    }

    @Test
    @DisplayName("deleteWarehouse: при неіснуючому ID кидає EmptyResultDataAccessException")
    void whenDeleteNonExisting_thenThrowsEmptyResult() {
        assertThrows(EmptyResultDataAccessException.class, () -> service.deleteWarehouse(999L));
    }

    @Test
    @DisplayName("deleteWarehouse: при null-ID кидає IllegalArgumentException")
    void whenDeleteNullId_thenThrowsIllegalArg() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteWarehouse(null));
    }

    @Test
    @DisplayName("createWarehouse: кількість записів зростає на 1")
    void whenCreate_thenCountIncrements() {
        long before = repository.count();
        Warehouse r = new Warehouse(); r.setName("Hall");
        service.createWarehouse(r);
        assertEquals(before + 1, repository.count());
    }

    @Test
    @DisplayName("updateWarehouse: кількість записів не змінюється")
    void whenUpdate_thenCountUnchanged() {
        Warehouse r = new Warehouse(); r.setName("X");
        Warehouse saved = repository.save(r);
        long before = repository.count();

        service.updateWarehouse(saved.getId(), r);
        assertEquals(before, repository.count());
    }

    @Test
    @DisplayName("deleteWarehouse: кількість записів зменшується на 1")
    void whenDelete_thenCountDecrements() {
        Warehouse r = new Warehouse(); r.setName("Y");
        Warehouse saved = repository.save(r);
        long before = repository.count();

        service.deleteWarehouse(saved.getId());
        assertEquals(before - 1, repository.count());
    }

    @Test
    @DisplayName("послідовні операції: створити → оновити → видалити")
    void whenSequenceCreateUpdateDelete_sequenceWorks() {
        Warehouse r = new Warehouse(); r.setName("Seq");
        Warehouse created = service.createWarehouse(r);
        assertNotNull(created.getId());

        Warehouse upd = new Warehouse(); upd.setName("SeqUp");
        Warehouse after = service.updateWarehouse(created.getId(), upd);
        assertEquals("SeqUp", after.getName());

        service.deleteWarehouse(after.getId());
        assertFalse(repository.findById(after.getId()).isPresent());
    }

    @Test
    @DisplayName("getWarehouseById: після створення повертає об'єкт з правильним ім'ям")
    void whenFindAfterCreate_thenNameMatches() {
        Warehouse r = new Warehouse(); r.setName("Match");
        Warehouse saved = service.createWarehouse(r);

        Warehouse found = service.getWarehouseById(saved.getId());
        assertEquals("Match", found.getName());
    }

    @Test
    @DisplayName("BeforeEach: репозиторій порожній перед тестом")
    void beforeEach_repositoryIsEmpty() {
        assertTrue(repository.findAll().isEmpty());
    }

    @Test
    @DisplayName("нічого не створюється без виклику create/update")
    void nothingSavedWithoutAction() {
        assertEquals(0, repository.count());
    }

}
