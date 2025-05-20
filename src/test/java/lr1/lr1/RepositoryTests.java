package lr1.lr1;

import lr1.lr1.model.Parcel;
import lr1.lr1.repository.ParcelRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RepositoryTests {

    @Autowired
    private ParcelRepository parcelRepository;

    @BeforeEach
    void setUp() {
        parcelRepository.save(new Parcel("Lamp", "Lighting", null));
        parcelRepository.save(new Parcel("Heater", "Heating", null));
    }

    @AfterEach
    void tearDown() {
        parcelRepository.deleteAll();
    }

    @Test
    void shouldFindAllParcels() {
        List<Parcel> parcels = parcelRepository.findAll();
        assertThat(parcels).hasSize(2);
    }

    @Test
    void shouldFindById() {
        Parcel parcel = parcelRepository.save(new Parcel("Thermostat", "Climate", null));
        Optional<Parcel> found = parcelRepository.findById(parcel.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Thermostat");
    }

    @Test
    void shouldUpdateParcel() {
        Parcel parcel = parcelRepository.findAll().get(0);
        parcel.setName("Updated Lamp");
        parcelRepository.save(parcel);

        Parcel updated = parcelRepository.findById(parcel.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("Updated Lamp");
    }

    @Test
    void shouldDeleteParcel() {
        Parcel parcel = parcelRepository.findAll().get(0);
        parcelRepository.deleteById(parcel.getId());

        Optional<Parcel> found = parcelRepository.findById(parcel.getId());
        assertThat(found).isEmpty();
    }

    @Test
    void shouldFindParcelsByType() {
        List<Parcel> parcels = parcelRepository.findAll();
        List<Parcel> lightingParcels = parcels.stream()
                .filter(d -> "Lighting".equals(d.getType()))
                .toList();

        assertThat(lightingParcels).hasSize(1);
        assertThat(lightingParcels.get(0).getName()).isEqualTo("Lamp");
    }

    @Test
    void shouldSaveNewParcel() {
        Parcel parcel = new Parcel("Fan", "Cooling", null);
        Parcel saved = parcelRepository.save(parcel);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Fan");
    }

    @Test
    void shouldExistById() {
        Parcel parcel = parcelRepository.findAll().get(0);
        boolean exists = parcelRepository.existsById(parcel.getId());

        assertThat(exists).isTrue();
    }

    @Test
    void shouldCountParcels() {
        long count = parcelRepository.count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    void shouldDeleteAllParcels() {
        parcelRepository.deleteAll();
        List<Parcel> parcels = parcelRepository.findAll();

        assertThat(parcels).isEmpty();
    }

    @Test
    void shouldAssignIdAfterSave() {
        Parcel parcel = new Parcel("Speaker", "Audio", null);
        Parcel saved = parcelRepository.save(parcel);

        assertThat(saved.getId())
                .isNotNull()
                .isGreaterThan(0);
    }
}
