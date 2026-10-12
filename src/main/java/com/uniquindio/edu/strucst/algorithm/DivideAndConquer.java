package com.uniquindio.edu.strucst.algorithm;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Clase que implementa el algoritmo de divide y vencerás.
 * usa la misma mecanica que la busqueda binaria, pero en este caso se busca el
 * maximo de una lista de elementos
 * 
 */
public final class DivideAndConquer {

    // Constructor privado pues todo es static
    private DivideAndConquer() {
    }

    // Método público para encontrar el máximo de una lista de elementos usando un
    // comparador.
    public static <T> T maximum(List<? extends T> elements, Comparator<? super T> comparator) {
        return maximum(elements, comparator, CallTrace.disabled());
    }

    // Metodo de divide y venceras lo manda a un metodo interno despues de
    // comprobaciones
    /**
     * @param <T>        genrico que tambien sera lo que devolvamos
     * @param elements   lista en la que se buscara el maximo
     * @param comparator comparador que nos ayudara a comparar los elementos de la
     *                   lista, ej: Integer::compareTo
     * @param trace      objeto de la clase CallTrace que nos ayudara a hacer un
     *                   seguimiento de las llamadas recursivas y la combinacion de
     *                   resultados
     * @return retornamos el maximo de la lista de elementos de forma de generico
     */
    public static <T> T maximum(List<? extends T> elements, Comparator<? super T> comparator, CallTrace trace) {
        Objects.requireNonNull(elements, "La colección no puede ser nula");
        Objects.requireNonNull(comparator, "El comparador no puede ser nulo");
        if (elements.isEmpty()) {
            throw new IllegalArgumentException("No se puede calcular el máximo de una colección vacía");
        }
        return maximum(elements, comparator, 0, elements.size() - 1, trace);
    }

    // Método privado recursivo que implementa la lógica de divide y vencerás para
    // encontrar el máximo.
    /**
     * @param <T>        genrico que tambien sera lo que devolvamos
     * @param elements   lista en la que se buscara el maximo
     * @param comparator comparador que nos ayudara a comparar los elementos de la
     *                   lista, ej: Integer::compareTo
     * @param low        indice del primer elemento de la sublista
     * @param high       indice del ultimo elemento de la sublista
     * @param trace      objeto de la clase CallTrace que nos ayudara a hacer un
     *                   seguimiento de las llamadas recursivas y la combinacion de
     *                   resultados
     * @return  retornamos el maximo de la lista de elementos de forma de generico
     */
    private static <T> T maximum(List<? extends T> elements, Comparator<? super T> comparator,
            int low, int high, CallTrace trace) {
        String call = "maximum(low=" + low + ", high=" + high + ")";
        trace.enter(call);
        // Caso base: si la sublista tiene un solo elemento, ese es el máximo
        if (low == high) {
            T single = elements.get(low);
            trace.note("caso base: un elemento");
            trace.exit(call, single);
            return single;
        }
        // Encuentra la mitad en numero de indice
        int mid = low + (high - low) / 2;
        //Llamadas recursivas mandamos la lista partida en dos 
        T leftMaximum = maximum(elements, comparator, low, mid, trace);
        T rightMaximum = maximum(elements, comparator, mid + 1, high, trace);
        //Combinamos los resultados de las llamadas recursivas usando el comparador para encontrar el máximo entre los dos
        T combined = combine(leftMaximum, rightMaximum, comparator);
        trace.note("combinar " + leftMaximum + " vs " + rightMaximum + " → " + combined);
        trace.exit(call, combined);
        return combined;
    }

    /**
     * @param <T>       generico que tambien sera lo que devolvamos
     * @param left    elemento izquierdo a comparar
     * @param right  elemento derecho a comparar
     * @param comparator  comparador que nos ayudara a comparar los elementos de la lista, ej: Integer::compareTo
     * @return retornamos el maximo de los dos elementos comparados
     */
    private static <T> T combine(T left, T right, Comparator<? super T> comparator) {
        //Usamos lamda de tipo if para comparar los dos elementos y retornar el mayor
        return comparator.compare(right, left) > 0 ? right : left;
    }
}
