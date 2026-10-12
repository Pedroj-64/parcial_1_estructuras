package com.uniquindio.edu.strucst.model;

import com.uniquindio.edu.strucst.exception.ValidationException;
import com.uniquindio.edu.strucst.model.comparator.ParcelComparators;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.uniquindio.edu.strucst.TestParcels.parcel;
import static org.junit.Assert.*;

public class ParcelTest {

    @Test
    public void defaultPriorityIsThree() {
        assertEquals(3, new Parcel("PQ1", "Armenia", 1.0, 10).getPriority());
    }

    @Test
    public void rejectsNonPositiveWeight() {
        for (double weight : new double[]{0.0, -1.5, Double.NaN}) {
            assertThrows(ValidationException.class, () -> new Parcel("PQ1", "Armenia", weight, 3, 10));
        }
    }

    @Test
    public void rejectsPriorityOutOfRange() {
        for (int priority : new int[]{0, 6, -1}) {
            assertThrows(ValidationException.class, () -> new Parcel("PQ1", "Armenia", 1.0, priority, 10));
        }
    }

    @Test
    public void rejectsNonPositiveTime() {
        assertThrows(ValidationException.class, () -> new Parcel("PQ1", "Armenia", 1.0, 3, 0));
    }

    @Test
    public void rejectsBlankDestinationAndCode() {
        assertThrows(ValidationException.class, () -> new Parcel("PQ1", " ", 1.0, 3, 10));
        assertThrows(ValidationException.class, () -> new Parcel("PQ1", null, 1.0, 3, 10));
        assertThrows(ValidationException.class, () -> new Parcel("", "Armenia", 1.0, 3, 10));
    }

    @Test
    public void naturalOrderIsByCode() {
        List<Parcel> parcels = new ArrayList<>(List.of(parcel("PQ731"), parcel("PQ105"), parcel("PQ942"),
                parcel("PQ318"), parcel("PQ567")));
        Collections.sort(parcels);
        assertEquals("[PQ105, PQ318, PQ567, PQ731, PQ942]", parcels.toString());
    }

    @Test
    public void dispatchOrderFollowsStatementExample() {
        List<Parcel> parcels = new ArrayList<>(List.of(parcel("PQ420", 3, 30), parcel("PQ731", 5, 45),
                parcel("PQ105", 5, 25), parcel("PQ942", 4, 20)));
        parcels.sort(ParcelComparators.DISPATCH_ORDER);
        assertEquals("[PQ105, PQ731, PQ942, PQ420]", parcels.toString());
    }

    @Test
    public void dispatchOrderBreaksFullTieByCode() {
        List<Parcel> parcels = new ArrayList<>(List.of(parcel("PQ900", 4, 20), parcel("PQ100", 4, 20)));
        parcels.sort(ParcelComparators.DISPATCH_ORDER);
        assertEquals("[PQ100, PQ900]", parcels.toString());
    }

    @Test
    public void alternativeComparatorsDoNotChangeNaturalOrder() {
        Parcel light = new Parcel("PQ100", "Armenia", 1.0, 1, 50);
        Parcel heavy = new Parcel("PQ200", "Armenia", 9.0, 5, 10);
        List<Parcel> parcels = new ArrayList<>(List.of(light, heavy));

        parcels.sort(ParcelComparators.BY_WEIGHT_DESC);
        assertEquals(List.of(heavy, light), parcels);
        parcels.sort(ParcelComparators.BY_PRIORITY_DESC);
        assertEquals(List.of(heavy, light), parcels);
        parcels.sort(ParcelComparators.BY_ESTIMATED_TIME_ASC);
        assertEquals(List.of(heavy, light), parcels);

        assertTrue("el orden natural sigue siendo por código", light.compareTo(heavy) < 0);
    }

    @Test
    public void equalityIsByCode() {
        assertEquals(parcel("PQ1"), new Parcel("PQ1", "Salento", 7.0, 1, 99));
        assertEquals(parcel("PQ1").hashCode(), new Parcel("PQ1", "Salento", 7.0, 1, 99).hashCode());
    }
}
