package lr1.lr1.service;

import lr1.lr1.model.Parcel;
import lr1.lr1.repository.ParcelRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParcelServiceTest {

    @Mock
    private ParcelRepository parcelRepository;

    @InjectMocks
    private ParcelService parcelService;

    private Parcel sampleParcel;

    @BeforeEach
    void setUp() {
        sampleParcel = new Parcel();
        sampleParcel.setId(1L);
        sampleParcel.setName("TestParcel");
    }

    @AfterEach
    void tearDown() {
        clearInvocations(parcelRepository);
    }

    @Test
    @DisplayName("getParcelById: повертає пристрій, якщо він є")
    void getParcelById_shouldReturnParcel_whenFound() {
        when(parcelRepository.findById(1L)).thenReturn(Optional.of(sampleParcel));

        Parcel result = parcelService.getParcelById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("TestParcel");
        verify(parcelRepository).findById(1L);
    }

    @Test
    @DisplayName("getParcelById: кидає NoSuchElementException, якщо пристрій не знайдено")
    void getParcelById_shouldThrow_whenNotFound() {
        when(parcelRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parcelService.getParcelById(2L))
                .isInstanceOf(NoSuchElementException.class);
        verify(parcelRepository).findById(2L);
    }

    @Test
    @DisplayName("getParcelById: кидає RuntimeException, якщо репозиторій падає")
    void getParcelById_shouldPropagateRepoException() {
        when(parcelRepository.findById(1L)).thenThrow(new RuntimeException("DB down"));

        assertThatThrownBy(() -> parcelService.getParcelById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("DB down");
        verify(parcelRepository).findById(1L);
    }

    @Test
    @DisplayName("getParcelById: повідомлення виключення 'No value present' при відсутності значення")
    void getParcelById_exceptionMessageCorrect() {
        when(parcelRepository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> parcelService.getParcelById(2L))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("No value present");
        verify(parcelRepository).findById(2L);
    }

    @Test
    @DisplayName("getParcelById: делегує пошук репозиторію з правильним ID")
    void getParcelById_shouldDelegateToRepository() {
        when(parcelRepository.findById(7L)).thenReturn(Optional.of(sampleParcel));

        parcelService.getParcelById(7L);

        verify(parcelRepository).findById(7L);
    }

    @Test
    @DisplayName("createParcel: зберігає і повертає пристрій")
    void createParcel_shouldSaveAndReturnParcel() {
        when(parcelRepository.save(sampleParcel)).thenReturn(sampleParcel);

        Parcel result = ParcelService.createParcel(sampleParcel);

        assertThat(result).isSameAs(sampleParcel);
        verify(parcelRepository).save(sampleParcel);
    }

    @Test
    @DisplayName("createParcel: кидає RuntimeException, якщо save() провалюється")
    void createParcel_shouldPropagateException_whenSaveFails() {
        when(parcelRepository.save(sampleParcel)).thenThrow(new RuntimeException("DB error"));

        assertThatThrownBy(() -> ParcelService.createParcel(sampleParcel))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("DB error");
        verify(parcelRepository).save(sampleParcel);
    }

    @Test
    @DisplayName("createParcel: кидає NullPointerException при null-пристрої")
    void createParcel_nullInput() {
        when(parcelRepository.save(null)).thenThrow(new NullPointerException("Parcel is null"));

        assertThatThrownBy(() -> ParcelService.createParcel(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("Parcel is null");
        verify(parcelRepository).save(null);
    }

    @Test
    @DisplayName("createParcel: multiple invocations викликає save відповідну кількість разів")
    void createParcel_multipleInvocations() {
        Parcel d1 = new Parcel();
        Parcel d2 = new Parcel();
        when(parcelRepository.save(d1)).thenReturn(d1);
        when(parcelRepository.save(d2)).thenReturn(d2);

        ParcelService.createParcel(d1);
        ParcelService.createParcel(d2);

        verify(parcelRepository, times(1)).save(d1);
        verify(parcelRepository, times(1)).save(d2);
    }

    @Test
    @DisplayName("createParcel: не викликає findById")
    void createParcel_noFindByIdCalls() {
        when(parcelRepository.save(sampleParcel)).thenReturn(sampleParcel);

        ParcelService.createParcel(sampleParcel);

        verify(parcelRepository).save(sampleParcel);
        verify(parcelRepository, never()).findById(any());
    }

    @Test
    @DisplayName("updateParcel: встановлює ID та повертає оновлений пристрій")
    void updateParcel_shouldSetIdAndReturnUpdatedParcel() {
        Parcel toUpdate = new Parcel();
        toUpdate.setName("NewName");
        Parcel saved = new Parcel();
        saved.setId(1L);
        saved.setName("NewName");

        when(parcelRepository.save(any(Parcel.class))).thenReturn(saved);

        Parcel result = parcelService.updateParcel(1L, toUpdate);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("NewName");
        ArgumentCaptor<Parcel> captor = ArgumentCaptor.forClass(Parcel.class);
        verify(parcelRepository).save(captor.capture());
        assertThat(captor.getValue().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("updateParcel: кидає RuntimeException, якщо save() провалюється")
    void updateParcel_shouldPropagateException_whenSaveFails() {
        Parcel anyParcel = new Parcel();
        when(parcelRepository.save(any(Parcel.class))).thenThrow(new RuntimeException("Update failed"));

        assertThatThrownBy(() -> parcelService.updateParcel(5L, anyParcel))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Update failed");
        verify(parcelRepository).save(any(Parcel.class));
    }

    @Test
    @DisplayName("updateParcel: встановлює null ID, коли передано null")
    void updateParcel_nullIdSetsNull() {
        Parcel toUpdate = new Parcel();
        toUpdate.setName("Name");
        when(parcelRepository.save(any(Parcel.class))).thenAnswer(inv -> inv.getArgument(0));

        Parcel result = parcelService.updateParcel(null, toUpdate);

        assertThat(result.getId()).isNull();
        assertThat(result.getName()).isEqualTo("Name");
        verify(parcelRepository).save(toUpdate);
    }

    @Test
    @DisplayName("updateParcel: переписує початковий ID пристрою")
    void updateParcel_overwritesExistingId() {
        Parcel toUpdate = new Parcel();
        toUpdate.setId(5L);
        toUpdate.setName("Name");
        when(parcelRepository.save(any(Parcel.class))).thenAnswer(inv -> inv.getArgument(0));

        Parcel result = parcelService.updateParcel(10L, toUpdate);

        assertThat(result.getId()).isEqualTo(10L);
        verify(parcelRepository).save(toUpdate);
    }

    @Test
    @DisplayName("updateParcel: кидає NullPointerException, якщо parcel null")
    void updateParcel_nullParcelThrows() {
        assertThatThrownBy(() -> parcelService.updateParcel(1L, null))
                .isInstanceOf(NullPointerException.class);
        verify(parcelRepository, never()).save(any());
    }

    @Test
    @DisplayName("updateParcel: не викликає deleteById або findById")
    void updateParcel_noUnrelatedRepoCalls() {
        Parcel d = new Parcel();
        when(parcelRepository.save(any())).thenReturn(d);

        parcelService.updateParcel(1L, d);

        verify(parcelRepository).save(any());
        verify(parcelRepository, never()).deleteById(any());
        verify(parcelRepository, never()).findById(any());
    }

    @Test
    @DisplayName("deleteParcel: викликає deleteById з правильним ID")
    void deleteParcel_shouldCallDeleteById() {
        doNothing().when(parcelRepository).deleteById(3L);

        parcelService.deleteParcel(3L);

        verify(parcelRepository).deleteById(3L);
    }

    @Test
    @DisplayName("deleteParcel: кидає RuntimeException, якщо deleteById() провалюється")
    void deleteParcel_shouldPropagateException_whenDeleteFails() {
        doThrow(new RuntimeException("Delete error")).when(parcelRepository).deleteById(4L);

        assertThatThrownBy(() -> parcelService.deleteParcel(4L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Delete error");
        verify(parcelRepository).deleteById(4L);
    }

    @Test
    @DisplayName("deleteParcel: кидає IllegalArgumentException при null-ID")
    void deleteParcel_nullIdThrows() {
        doThrow(new IllegalArgumentException("ID is null")).when(parcelRepository).deleteById(null);

        assertThatThrownBy(() -> parcelService.deleteParcel(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ID is null");
        verify(parcelRepository).deleteById(null);
    }

    @Test
    @DisplayName("deleteParcel: не викликає save або findById")
    void deleteParcel_noUnrelatedRepoCalls() {
        doNothing().when(parcelRepository).deleteById(2L);

        parcelService.deleteParcel(2L);

        verify(parcelRepository).deleteById(2L);
        verify(parcelRepository, never()).save(any());
        verify(parcelRepository, never()).findById(any());
    }
}
