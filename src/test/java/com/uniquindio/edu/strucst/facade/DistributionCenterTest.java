package com.uniquindio.edu.strucst.facade;

import com.uniquindio.edu.strucst.data.SampleData;
import com.uniquindio.edu.strucst.dispatch.DispatchMode;
import com.uniquindio.edu.strucst.exception.AssignmentException;
import com.uniquindio.edu.strucst.model.Assignment;
import com.uniquindio.edu.strucst.model.Parcel;
import com.uniquindio.edu.strucst.model.ParcelStatus;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class DistributionCenterTest {

    private DistributionCenter center;

    @Before
    public void setUp() {
        center = SampleData.loadInto(new DistributionCenter());
    }

    @Test
    public void sampleDataMeetsStatementMinimums() {
        assertTrue(center.parcelsInRegistrationOrder().size() >= 30);
        assertTrue(center.destinations().size() >= 5);
        assertTrue(center.couriers().size() >= 6);
    }

    @Test
    public void dispatchByArrivalDoesNotSkipTheNextParcel() {
        Assignment first = center.dispatchNext(DispatchMode.ARRIVAL_ORDER);
        assertEquals("PQ731", first.parcel().getCode());
        assertEquals("PQ105", center.peekNextParcel(DispatchMode.ARRIVAL_ORDER).orElseThrow().getCode());
    }

    @Test
    public void parcelDispatchedByPriorityIsSkippedInArrivalQueue() {
        Assignment byPriority = center.dispatchNext(DispatchMode.PRIORITY);
        assertEquals("PQ903", byPriority.parcel().getCode());
        Assignment byArrival = center.dispatchNext(DispatchMode.ARRIVAL_ORDER);
        assertNotEquals(byPriority.parcel(), byArrival.parcel());
    }

    @Test
    public void assignmentRules() {
        AssignmentException zone = assertThrows(AssignmentException.class, () -> center.assign("PQ942", "R-1003"));
        assertTrue(zone.getMessage().contains("zona"));
        assertThrows(AssignmentException.class, () -> center.assign("PQ688", "R-1008"));

        center.assign("PQ942", "R-1001");
        assertThrows(AssignmentException.class, () -> center.assign("PQ310", "R-1001"));
        assertThrows(AssignmentException.class, () -> center.assign("PQ942", "R-1002"));
    }

    @Test
    public void dispatchNextFailsWhenNoCourierAvailableAndKeepsParcelQueued() {
        center.assign("PQ903", "R-1001");
        center.assign("PQ310", "R-1002");
        assertThrows(AssignmentException.class, () -> center.dispatchNext(DispatchMode.ARRIVAL_ORDER));
        assertEquals("PQ731", center.peekNextParcel(DispatchMode.ARRIVAL_ORDER).orElseThrow().getCode());
    }

    @Test
    public void deliveriesAreKeptInRealOrderInOwnList() {
        center.assign("PQ105", "R-1003");
        center.assign("PQ942", "R-1001");
        center.assign("PQ731", "R-1002");
        center.registerDelivery("PQ105");
        center.registerDelivery("PQ942");
        center.registerDelivery("PQ731");

        List<String> history = new ArrayList<>();
        for (Parcel parcel : center.deliveryHistory()) {
            history.add(parcel.getCode());
        }
        assertEquals(List.of("PQ105", "PQ942", "PQ731"), history);
        assertEquals(ParcelStatus.DELIVERED, center.findParcel("PQ105").orElseThrow().getStatus());
        assertEquals(27, center.pendingParcels().size());
        assertTrue(center.findCourier("R-1003").orElseThrow().isAvailable());
    }

    @Test
    public void cannotDeliverParcelWithoutAssignment() {
        assertThrows(AssignmentException.class, () -> center.registerDelivery("PQ105"));
    }

    @Test
    public void analyticsOverSampleData() {
        assertEquals(39.7, center.totalWeightTo("Armenia"), 1e-9);
        assertEquals("PQ318", center.heaviestParcel().orElseThrow().getCode());
        List<Parcel> sorted = center.parcelsSortedByCode();
        assertEquals("PQ731", center.binarySearchByCode(sorted, "PQ731").orElseThrow().getCode());
        assertTrue(center.binarySearchByCode(sorted, "PQ000").isEmpty());
    }
}
