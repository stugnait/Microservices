package lr1.lr1;

import lr1.lr1.model.Room;
import lr1.lr1.repository.RoomRepository;
import lr1.lr1.service.RoomService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class RoomServiceMockTests {

    @Mock
    private RoomRepository mockRepo;

    @InjectMocks
    private RoomService underTest;

    private ArgumentCaptor<Room> roomCaptor;

    @BeforeEach
    void setUp() {
        roomCaptor = ArgumentCaptor.forClass(Room.class);
    }

    @Test
    @DisplayName("createRoom: valid room is saved and returned")
    void createRoom_validRoom_saved() {
        Room saved = new Room();
        saved.setId(100L);
        saved.setName("Living");

        // stub any(Room) → saved
        given(mockRepo.save(any(Room.class))).willReturn(saved);

        Room result = underTest.createRoom(new Room());

        then(mockRepo).should(times(1)).save(roomCaptor.capture());
        assertThat(roomCaptor.getValue().getName()).isNull(); // new Room() name is null
        assertThat(result.getId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("createRoom: null input throws IllegalArgumentException")
    void createRoom_nullInput_throws() {
        assertThatThrownBy(() -> underTest.createRoom(null))
                .isInstanceOf(IllegalArgumentException.class);

        then(mockRepo).shouldHaveNoInteractions();
    }

    @Test
    @DisplayName("getRoomById: existing ID returns room")
    void getRoomById_existing_returned() {
        Room existing = new Room();
        existing.setId(5L);
        existing.setName("Office");
        given(mockRepo.findById(5L)).willReturn(Optional.of(existing));

        Room result = underTest.getRoomById(5L);

        then(mockRepo).should(times(1)).findById(5L);
        assertThat(result).isSameAs(existing);
    }

    @Test
    @DisplayName("getRoomById: non-existing ID throws NoSuchElementException")
    void getRoomById_nonExisting_throws() {
        given(mockRepo.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getRoomById(99L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("updateRoom: valid update sets ID and saves")
    void updateRoom_valid_setsIdAndSaves() {
        Room returned = new Room();
        returned.setId(10L);
        returned.setName("Updated");
        given(mockRepo.save(any(Room.class))).willReturn(returned);

        Room result = underTest.updateRoom(10L, new Room());

        then(mockRepo).should(times(1)).save(roomCaptor.capture());
        assertThat(roomCaptor.getValue().getId()).isEqualTo(10L);
        assertThat(result).isSameAs(returned);
    }

    @Test
    @DisplayName("updateRoom: null room throws NullPointerException")
    void updateRoom_nullRoom_throws() {
        assertThatThrownBy(() -> underTest.updateRoom(1L, null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("updateRoom: null ID still saves room with null ID")
    void updateRoom_nullId_savesWithNullId() {
        Room input = new Room();
        given(mockRepo.save(any(Room.class))).willReturn(input);

        Room result = underTest.updateRoom(null, input);

        then(mockRepo).should(times(1)).save(roomCaptor.capture());
        assertThat(roomCaptor.getValue().getId()).isNull();
        assertThat(result).isSameAs(input);
    }

    @Test
    @DisplayName("deleteRoom: existing ID invokes deleteById")
    void deleteRoom_existing_invokesDeleteById() {
        // підготовка: існуючий ID
        given(mockRepo.existsById(3L)).willReturn(true);

        // виконання
        underTest.deleteRoom(3L);

        // перевірка, що саме deleteById(3L) викликався
        then(mockRepo).should(times(1)).deleteById(3L);
    }

    @Test
    @DisplayName("deleteRoom: non-existing ID throws EmptyResultDataAccessException")
    void deleteRoom_nonExisting_throwsEmptyResult() {
        // підготовка: неіснуючий ID
        given(mockRepo.existsById(9L)).willReturn(false);

        // перевірка, що кидається потрібний виняток
        assertThatThrownBy(() -> underTest.deleteRoom(9L))
                .isInstanceOf(org.springframework.dao.EmptyResultDataAccessException.class)
                .hasMessageContaining("Room not found");
    }


    @Test
    @DisplayName("deleteRoom: null ID throws IllegalArgumentException")
    void deleteRoom_nullId_throws() {
        assertThatThrownBy(() -> underTest.deleteRoom(null))
                .isInstanceOf(IllegalArgumentException.class);

        then(mockRepo).shouldHaveNoMoreInteractions();
    }
}
