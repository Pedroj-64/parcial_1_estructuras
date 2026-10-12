package com.uniquindio.edu.strucst.algorithm;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static com.uniquindio.edu.strucst.TestParcels.parcel;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.Test;

import com.uniquindio.edu.strucst.model.Parcel;
import com.uniquindio.edu.strucst.model.comparator.ParcelComparators;

public class AlgorithmsTest {
        private static final List<Parcel> WEIGHT_EXAMPLE = List.of(
            parcel("PQ101", "Armenia", 4.5),
            parcel("PQ203", "Salento", 3.0),
            parcel("PQ415", "Armenia", 7.2),
            parcel("PQ730", "Armenia", 2.3));

    @Test
    public void totalWeightByDestinationMatchesStatement() {
        assertEquals(14.0, RecursiveAnalytics.totalWeightByDestination(WEIGHT_EXAMPLE, "Armenia"), 1e-9);
        assertEquals(3.0, RecursiveAnalytics.totalWeightByDestination(WEIGHT_EXAMPLE, "salento"), 1e-9);
        assertEquals(0.0, RecursiveAnalytics.totalWeightByDestination(WEIGHT_EXAMPLE, "Filandia"), 1e-9);
        assertEquals(0.0, RecursiveAnalytics.totalWeightByDestination(List.of(), "Armenia"), 1e-9);
    }

    @Test
    public void countWithMinimumPriorityMatchesStatement() {
        int[] priorities = {5, 2, 4, 1, 5, 3, 4};
        List<Parcel> parcels = IntStream.range(0, priorities.length)
                .mapToObj(i -> parcel("PQ" + i, priorities[i], 10)).toList();
        assertEquals(4, RecursiveAnalytics.countWithMinimumPriority(parcels, 4));
        assertEquals(7, RecursiveAnalytics.countWithMinimumPriority(parcels, 1));
        assertEquals(0, RecursiveAnalytics.countWithMinimumPriority(List.of(), 1));
    }

    @Test
    public void recursionTraceShowsOneFramePerElementPlusBaseCase() {
        CallTrace trace = CallTrace.enabled();
        RecursiveAnalytics.totalWeightByDestination(WEIGHT_EXAMPLE, "Armenia", trace);
        assertEquals(WEIGHT_EXAMPLE.size() + 1, trace.maxDepth());
    }

    @Test
    public void binarySearchFindsAndMisses() {
        List<String> codes = List.of("PQ105", "PQ214", "PQ318", "PQ421", "PQ567", "PQ731", "PQ842", "PQ942");
        assertEquals(5, BinarySearch.searchByKey(codes, "PQ731", c -> c));
        assertEquals(0, BinarySearch.searchByKey(codes, "PQ105", c -> c));
        assertEquals(7, BinarySearch.searchByKey(codes, "PQ942", c -> c));
        assertEquals(BinarySearch.NOT_FOUND, BinarySearch.searchByKey(codes, "PQ500", c -> c));
        assertEquals(BinarySearch.NOT_FOUND, BinarySearch.searchByKey(List.<String>of(), "PQ500", c -> c));
    }

    @Test
    public void binarySearchExaminesFewElements() {
        List<String> codes = List.of("PQ105", "PQ214", "PQ318", "PQ421", "PQ567", "PQ731", "PQ842", "PQ942");
        CallTrace trace = CallTrace.enabled();
        BinarySearch.searchByKey(codes, "PQ731", c -> c, trace);
        assertEquals("solo dos comparaciones para encontrar PQ731", 2, trace.maxDepth());
    }

    @Test
    public void binarySearchWithComparatorOnParcels() {
        List<Parcel> sorted = List.of(parcel("PQ105"), parcel("PQ318"), parcel("PQ731"));
        assertEquals(2, BinarySearch.search(sorted, parcel("PQ731"), ParcelComparators.BY_CODE));
    }

    @Test
    public void divideAndConquerFindsHeaviest() {
        List<Double> weights = List.of(4.5, 12.0, 3.2, 18.7, 7.4, 5.1);
        assertEquals((Object) 18.7, DivideAndConquer.maximum(weights, Comparator.naturalOrder()));

        List<Parcel> parcels = IntStream.range(0, weights.size())
                .mapToObj(i -> parcel("PQ" + i, "Armenia", weights.get(i))).toList();
        assertEquals("PQ3", DivideAndConquer.maximum(parcels, ParcelComparators.BY_WEIGHT_ASC).getCode());
    }

    @Test
    public void divideAndConquerSingleElementAndEmpty() {
        assertEquals((Object) 7, DivideAndConquer.maximum(List.of(7), Comparator.naturalOrder()));
        assertThrows(IllegalArgumentException.class,
                () -> DivideAndConquer.maximum(List.<Integer>of(), Comparator.naturalOrder()));
    }

    @Test
    public void divideAndConquerStackDepthIsLogarithmic() {
        List<Integer> values = IntStream.range(0, 1024).boxed().toList();
        CallTrace trace = CallTrace.enabled();
        assertEquals((Object) 1023, DivideAndConquer.maximum(values, Comparator.naturalOrder(), trace));
        assertEquals(11, trace.maxDepth());
    }
}
