package com.uniquindio.edu.strucst.dispatch;

import com.uniquindio.edu.strucst.model.Parcel;
import com.uniquindio.edu.strucst.model.ParcelStatus;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.uniquindio.edu.strucst.TestParcels.parcel;
import static org.junit.Assert.*;

public class DispatchStrategyTest {

    private static List<String> drain(DispatchStrategy strategy) {
        List<String> order = new ArrayList<>();
        strategy.pollNext().ifPresent(p -> {
            order.add(p.getCode());
            order.addAll(drain(strategy));
        });
        return order;
    }

    @Test
    public void arrivalOrderIsFifo() {
        DispatchStrategy strategy = new ArrivalOrderDispatchStrategy();
        List.of("PQ731", "PQ105", "PQ942", "PQ318").forEach(code -> strategy.enqueue(parcel(code)));
        assertEquals(List.of("PQ731", "PQ105", "PQ942", "PQ318"), drain(strategy));
        assertTrue(strategy.pollNext().isEmpty());
    }

    @Test
    public void priorityFollowsDispatchRules() {
        DispatchStrategy strategy = new PriorityDispatchStrategy();
        strategy.enqueue(parcel("PQ420", 3, 30));
        strategy.enqueue(parcel("PQ731", 5, 45));
        strategy.enqueue(parcel("PQ105", 5, 25));
        strategy.enqueue(parcel("PQ942", 4, 20));
        assertEquals("PQ105", strategy.peekNext().orElseThrow().getCode());
        assertEquals(List.of("PQ105", "PQ731", "PQ942", "PQ420"), drain(strategy));
    }

    @Test
    public void skipsParcelsThatAreNoLongerPending() {
        DispatchStrategy strategy = new ArrivalOrderDispatchStrategy();
        Parcel first = parcel("PQ1");
        Parcel second = parcel("PQ2");
        strategy.enqueue(first);
        strategy.enqueue(second);
        first.changeStatus(ParcelStatus.IN_TRANSIT);
        assertEquals(second, strategy.peekNext().orElseThrow());
    }
}
