package com.uniquindio.edu.strucst.algorithm;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;


/**
 * Clase que implementa el algoritmo de divide y vencerás.
 * usa la misma mecanica que la busqueda binaria, pero en este caso se busca el maximo de una lista de elementos
 * 
 */
public final class DivideAndConquer {

    // Constructor privado pues todo es static
    private DivideAndConquer() {
    }

    
    public static <T> T maximum(List<? extends T> elements, Comparator<? super T> comparator) {
        return maximum(elements, comparator, CallTrace.disabled());
    }


    public static <T> T maximum(List<? extends T> elements, Comparator<? super T> comparator, CallTrace trace) {
        Objects.requireNonNull(elements, "La colección no puede ser nula");
        Objects.requireNonNull(comparator, "El comparador no puede ser nulo");
        if (elements.isEmpty()) {
            throw new IllegalArgumentException("No se puede calcular el máximo de una colección vacía");
        }
        return maximum(elements, comparator, 0, elements.size() - 1, trace);
    }

    private static <T> T maximum(List<? extends T> elements, Comparator<? super T> comparator,
                                 int low, int high, CallTrace trace) {
        String call = "maximum(low=" + low + ", high=" + high + ")";
        trace.enter(call);

        if (low == high) {
            T single = elements.get(low);
            trace.note("caso base: un elemento");
            trace.exit(call, single);
            return single;
        }

        int mid = low + (high - low) / 2;

        T leftMaximum = maximum(elements, comparator, low, mid, trace);
        T rightMaximum = maximum(elements, comparator, mid + 1, high, trace);

        T combined = combine(leftMaximum, rightMaximum, comparator);
        trace.note("combinar " + leftMaximum + " vs " + rightMaximum + " → " + combined);
        trace.exit(call, combined);
        return combined;
    }

    private static <T> T combine(T left, T right, Comparator<? super T> comparator) {
        return comparator.compare(right, left) > 0 ? right : left;
    }
}
