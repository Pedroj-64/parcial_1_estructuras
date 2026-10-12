package com.uniquindio.edu.strucst.algorithm;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class BinarySearch {

    public static final int NOT_FOUND = -1;

    private BinarySearch() {
    }

    public static <T> int search(
            List<T> list,
            T target,
            Comparator<T> comparator) {
        Objects.requireNonNull(comparator, "El comparador no puede ser nulo");
        return searchByKey(list, target, element -> element, comparator, CallTrace.disabled());
    }

    public static <T, K extends Comparable<? super K>> int searchByKey(
            List<T> list,
            K target,
            Function<T, K> getKey) {
        return searchByKey(list, target, getKey, CallTrace.disabled());
    }

    public static <T, K extends Comparable<? super K>> int searchByKey(
            List<T> list,
            K target,
            Function<T, K> getKey,
            CallTrace trace) {
        return searchByKey(list, target, getKey, Comparator.naturalOrder(), trace);
    }

    private static <T, K> int searchByKey(
            List<T> list,
            K target,
            Function<T, K> getKey,
            Comparator<K> comparator,
            CallTrace trace) {
        Objects.requireNonNull(list, "La lista no puede ser nula");
        Objects.requireNonNull(getKey, "La función para obtener la clave no puede ser nula");

        if (target == null) {
            return NOT_FOUND;
        }

        return searchBetween(
                list,
                target,
                getKey,
                comparator,
                0,
                list.size() - 1,
                trace);
    }

    private static <T, K> int searchBetween(
            List<T> list,
            K target,
            Function<T, K> getKey,
            Comparator<K> comparator,
            int start,
            int end,
            CallTrace trace) {
        String call = "binarySearch(start=" + start + ", end=" + end + ")";
        trace.enter(call);

        if (start > end) {
            trace.note("no quedan posiciones por revisar");
            trace.exit(call, NOT_FOUND);
            return NOT_FOUND;
        }

        int middle = start + (end - start) / 2;
        K middleKey = getKey.apply(list.get(middle));
        int comparison = comparator.compare(target, middleKey);

        trace.note("posición central=" + middle + ", clave=" + middleKey);

        if (comparison == 0) {
            trace.note("elemento encontrado");
            trace.exit(call, middle);
            return middle;
        }

        int result;
        if (comparison < 0) {
            result = searchBetween(
                    list, target, getKey, comparator, start, middle - 1, trace);
        } else {
            result = searchBetween(
                    list, target, getKey, comparator, middle + 1, end, trace);
        }

        trace.exit(call, result);
        return result;
    }
}
