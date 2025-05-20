package lr1.lr1;

import lr1.lr1.model.Room;
import lr1.lr1.repository.RoomRepository;
import lr1.lr1.service.RoomService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoomServiceTest {

    @Autowired
    private RoomRepository repository;

    @Autowired
    private RoomService service;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("createRoom: при коректній кімнаті повертає згенерований ID")
    void whenCreateRoom_thenIdGenerated() {
        Room r = new Room();
        r.setName("Living Room");

        Room saved = service.createRoom(r);

        assertNotNull(saved.getId());
        assertEquals(1, repository.count());
        assertEquals("Living Room", saved.getName());
    }

    @Test
    @DisplayName("createRoom: при null-кімнаті кидає IllegalArgumentException")
    void whenCreateRoomNull_thenThrowsIllegalArg() {
        assertThrows(IllegalArgumentException.class, () -> service.createRoom(null));
        assertEquals(0, repository.count());
    }

    @Test
    @DisplayName("getRoomById: повертає створену кімнату")
    void whenGetByIdExisting_thenReturnsRoom() {
        Room r = new Room();
        r.setName("Kitchen");
        Room saved = repository.save(r);

        Room found = service.getRoomById(saved.getId());

        assertEquals(saved.getId(), found.getId());
        assertEquals("Kitchen", found.getName());
    }

    @Test
    @DisplayName("getRoomById: при неіснуючому ID кидає NoSuchElementException")
    void whenGetByIdNonExisting_thenThrowsNoSuch() {
        assertThrows(NoSuchElementException.class, () -> service.getRoomById(123L));
    }

    @Test
    @DisplayName("repository.findAll: при відсутності кімнат повертає порожній список")
    void whenNoRooms_thenFindAllEmpty() {
        List<Room> list = repository.findAll();
        assertNotNull(list);
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("repository.findAll: повертає всі збережені кімнати")
    void whenMultipleRooms_thenFindAllReturnsAll() {
        Room r1 = new Room();
        r1.setName("A");
        Room r2 = new Room();
        r2.setName("B");
        repository.save(r1);
        repository.save(r2);

        List<Room> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(x -> "A".equals(x.getName())));
        assertTrue(all.stream().anyMatch(x -> "B".equals(x.getName())));
    }

    @Test
    @DisplayName("updateRoom: змінює поля існуючої кімнати")
    void whenUpdateRoom_thenFieldsUpdated() {
        Room orig = new Room();
        orig.setName("OldName");
        Room saved = repository.save(orig);

        Room toUpdate = new Room();
        toUpdate.setName("NewName");
        Room updated = service.updateRoom(saved.getId(), toUpdate);

        assertEquals(saved.getId(), updated.getId());
        assertEquals("NewName", updated.getName());
        assertEquals(1, repository.count());
    }

    @Test
    @DisplayName("updateRoom: при null-кімнаті кидає NullPointerException")
    void whenUpdateNullRoom_thenThrowsNullPtr() {
        assertThrows(NullPointerException.class, () -> service.updateRoom(1L, null));
        assertEquals(0, repository.count());
    }

    @Test
    @DisplayName("updateRoom: при null-ID створює нову кімнату")
    void whenUpdateNullId_thenCreatesNew() {
        Room r = new Room();
        r.setName("Guest");
        Room result = service.updateRoom(null, r);

        assertNotNull(result.getId());
        assertEquals(1, repository.count());
    }

    @Test
    @DisplayName("deleteRoom: видаляє існуючу кімнату")
    void whenDeleteExisting_thenRemoved() {
        Room r = new Room();
        r.setName("Office");
        Room saved = repository.save(r);

        service.deleteRoom(saved.getId());

        assertFalse(repository.findById(saved.getId()).isPresent());
        assertEquals(0, repository.count());
    }

    @Test
    @DisplayName("deleteRoom: при неіснуючому ID кидає EmptyResultDataAccessException")
    void whenDeleteNonExisting_thenThrowsEmptyResult() {
        assertThrows(EmptyResultDataAccessException.class, () -> service.deleteRoom(999L));
    }

    @Test
    @DisplayName("deleteRoom: при null-ID кидає IllegalArgumentException")
    void whenDeleteNullId_thenThrowsIllegalArg() {
        assertThrows(IllegalArgumentException.class, () -> service.deleteRoom(null));
    }

    @Test
    @DisplayName("createRoom: кількість записів зростає на 1")
    void whenCreate_thenCountIncrements() {
        long before = repository.count();
        Room r = new Room();
        r.setName("Hall");
        service.createRoom(r);
        assertEquals(before + 1, repository.count());
    }

    @Test
    @DisplayName("updateRoom: кількість записів не змінюється")
    void whenUpdate_thenCountUnchanged() {
        Room r = new Room();
        r.setName("X");
        Room saved = repository.save(r);
        long before = repository.count();

        service.updateRoom(saved.getId(), r);
        assertEquals(before, repository.count());
    }

    @Test
    @DisplayName("deleteRoom: кількість записів зменшується на 1")
    void whenDelete_thenCountDecrements() {
        Room r = new Room();
        r.setName("Y");
        Room saved = repository.save(r);
        long before = repository.count();

        service.deleteRoom(saved.getId());
        assertEquals(before - 1, repository.count());
    }

    @Test
    @DisplayName("послідовні операції: створити → оновити → видалити")
    void whenSequenceCreateUpdateDelete_sequenceWorks() {
        Room r = new Room();
        r.setName("Seq");
        Room created = service.createRoom(r);
        assertNotNull(created.getId());

        Room upd = new Room();
        upd.setName("SeqUp");
        Room after = service.updateRoom(created.getId(), upd);
        assertEquals("SeqUp", after.getName());

        service.deleteRoom(after.getId());
        assertFalse(repository.findById(after.getId()).isPresent());
    }

    @Test
    @DisplayName("getRoomById: після створення повертає об'єкт з правильним ім'ям")
    void whenFindAfterCreate_thenNameMatches() {
        Room r = new Room();
        r.setName("Match");
        Room saved = service.createRoom(r);

        Room found = service.getRoomById(saved.getId());
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
