package com.uniquindio.edu.strucst.repository;

import com.uniquindio.edu.strucst.exception.DuplicateEntityException;
import com.uniquindio.edu.strucst.exception.EntityNotFoundException;
import com.uniquindio.edu.strucst.model.Courier;
import com.uniquindio.edu.strucst.model.Parcel;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static com.uniquindio.edu.strucst.TestParcels.parcel;
import static org.junit.Assert.*;

public class ParcelRepositoryTest {

    private ParcelRepository repository;

    @Before
    public void setUp() {
        repository = new ParcelRepository();
        repository.save(parcel("PQ731", "Armenia", 1));
        repository.save(parcel("PQ105", "Salento", 1));
        repository.save(parcel("PQ942", "Armenia", 1));
        repository.save(parcel("PQ318", "Calarcá", 1));
        repository.save(parcel("PQ567", "Salento", 1));
        repository.save(parcel("PQ829", "Armenia", 1));
    }

    @Test
    public void keepsRegistrationOrder() {
        assertEquals("[PQ731, PQ105, PQ942, PQ318, PQ567, PQ829]",
                repository.findAllInRegistrationOrder().toString());
    }

    @Test
    public void rejectsDuplicateCode() {
        assertThrows(DuplicateEntityException.class, () -> repository.save(parcel("PQ731")));
        assertEquals(6, repository.count());
    }

    @Test
    public void findsByCode() {
        assertTrue(repository.exists("PQ942"));
        assertEquals("PQ942", repository.getByCode("PQ942").getCode());
        assertTrue(repository.findByCode("PQ000").isEmpty());
        assertThrows(EntityNotFoundException.class, () -> repository.getByCode("PQ000"));
    }

    @Test
    public void destinationsWithoutDuplicatesInAppearanceOrder() {
        assertEquals(List.of("Armenia", "Salento", "Calarcá"), List.copyOf(repository.findDestinations()));
    }

    @Test
    public void destinationsSortedAlphabeticallyRespectingAccents() {
        repository.save(parcel("PQ001", "Circasia", 1));
        assertEquals(List.of("Armenia", "Calarcá", "Circasia", "Salento"),
                List.copyOf(repository.findDestinationsSorted()));
    }

    @Test
    public void groupsByDestination() {
        var grouped = repository.findGroupedByDestination();
        assertEquals("[PQ731, PQ942, PQ829]", grouped.get("Armenia").toString());
        assertEquals("[PQ105, PQ567]", grouped.get("Salento").toString());
        assertEquals("[PQ318]", grouped.get("Calarcá").toString());
    }

    @Test
    public void viewsAreReadOnly() {
        assertThrows(UnsupportedOperationException.class,
                () -> repository.findAllInRegistrationOrder().add(parcel("PQ000")));
        assertThrows(UnsupportedOperationException.class,
                () -> repository.findGroupedByDestination().get("Armenia").clear());
    }

    @Test
    public void sortedByCodeDoesNotAlterRegistrationOrder() {
        List<Parcel> sorted = repository.findAllSortedByCode();
        assertEquals("[PQ105, PQ318, PQ567, PQ731, PQ829, PQ942]", sorted.toString());
        assertEquals("PQ731", repository.findAllInRegistrationOrder().getFirst().getCode());
    }

    @Test
    public void courierRepositoryRejectsDuplicateIdAndFindsByZone() {
        CourierRepository couriers = new CourierRepository();
        couriers.save(new Courier("R1", "Ana", "Armenia", false));
        couriers.save(new Courier("R2", "Luis", "armenia"));
        assertThrows(DuplicateEntityException.class, () -> couriers.save(new Courier("R1", "Otro", "Salento")));
        assertEquals("R2", couriers.findFirstAvailableInZone("Armenia").orElseThrow().getId());
        assertTrue(couriers.findFirstAvailableInZone("Salento").isEmpty());
    }
}
