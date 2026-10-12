package com.uniquindio.edu.strucst.algorithm;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * // Implementación de la búsqueda binaria.
 * //
 */
public final class BinarySearch {
    // Valor pequeño que sirve para indicar que no se encontró el elemento buscado
    public static final int NOT_FOUND = -1;

    // Constructor privado pa que publico si todo es static :p
    private BinarySearch() {
    }

    /**
     * Metodo central de busqueda binaria
     * 
     * @param <T>        variable generica pa que entre listas de cualquier tipo y
     *                   salga cualquier tipo
     * @param list       lista de elementos a buscar (clave que este ordenada)
     * @param target     elemento a buscar
     * @param comparator tiene que venir el tipo de comparacion pa que sepa como
     *                   comparar los elementos ej:
     *                   Comparator.naturalOrder()
     * @return utiliza su funcion amiga searchBykey
     */
    public static <T> int search(
            List<T> list,
            T target,
            Comparator<T> comparator) {
        Objects.requireNonNull(comparator, "El comparador no puede ser nulo");
        return searchByKey(list, target, element -> element, comparator, CallTrace.disabled());
    }

    /**
     * funcion con seguimiento de llamadas recursivas apagada
     * 
     * @param <T>    primer tipo generico cualquiera
     * @param <K>    segundo tipo generico pero es necesario que tenga relacion con
     *               Comparable
     * @param list   lista de elementos a buscar (clave que este ordenada)
     * @param target elemento a buscar
     * @param getKey funcion que obtiene la clave de un elemento, ej: element ->
     *               element.getCode()
     * @param trace  objeto de seguimiento de llamadas recursivas
     * @return utiliza su funcion amiga searchBykey, si esto es una muñeca rusa nmms
     */
    public static <T, K extends Comparable<? super K>> int searchByKey(
            List<T> list,
            K target,
            Function<T, K> getKey) {
        return searchByKey(list, target, getKey, CallTrace.disabled());
    }

    /**
     * metodos con seguimiento de llamadas recursivas activado
     * 
     * @param <T>    primer tipo generico cualquiera
     * @param <K>    segundo tipo generico pero es necesario que tenga relacion con
     *               Comparable
     * @param list   lista de elementos a buscar (clave que este ordenada)
     * @param target elemento a buscar
     * @param getKey funcion que obtiene la clave de un elemento, ej: element ->
     *               element.getCode()
     * @param trace  objeto de seguimiento de llamadas recursivas
     * @return
     */
    public static <T, K extends Comparable<? super K>> int searchByKey(
            List<T> list,
            K target,
            Function<T, K> getKey,
            CallTrace trace) {
        return searchByKey(list, target, getKey, Comparator.naturalOrder(), trace);
    }

    /**
     * Esto ya es la busqueda binaria real
     * 
     * @param <T>        primer tipo generico cualquiera
     * @param <K>        segundo tipo generico pero es necesario que tenga relacion
     *                   con Comparable
     * @param list       lista de elementos a buscar (clave que este ordenada)
     * @param target     elemento a buscar
     * @param getKey     funcion que obtiene la clave de un elemento, ej: element ->
     *                   element.getCode()
     * @param comparator tiene que venir el tipo de comparacion pa que sepa como
     *                   comparar los elementos ej:
     *                   Comparator.naturalOrder()
     * @param trace      objeto de seguimiento de llamadas recursivas
     * @return
     */
    private static <T, K> int searchByKey(
            List<T> list,
            K target,
            Function<T, K> getKey,
            Comparator<K> comparator,
            CallTrace trace) {
        // es una validacion clasica pa evitar que entren cosas nulas y explote todo alv
        Objects.requireNonNull(list, "La lista no puede ser nula");
        Objects.requireNonNull(getKey, "La función para obtener la clave no puede ser nula");
        // el uso de -1 para dar a de cuenta que el valor no existe en donde lo buscamos
        if (target == null) {
            return NOT_FOUND;
        }
        // utilizamos metodo auxilar para la bsqueda binaria recursiva, le pasamos los
        // indices de inicio y fin de la lista
        return searchBetween(
                list,
                target,
                getKey,
                comparator,
                0,
                list.size() - 1,
                trace);
    }

    /**
     * @param <T>        Primera variable generica cualquiera
     * @param <K>        Segunda variable generica
     * @param list       lista de elementos a buscar (clave que este ordenada)
     * @param target     elemento a buscar
     * @param getKey     funcion que obtiene la clave de un elemento, ej: element ->
     *                   element.getCode()
     * @param comparator tiene que venir el tipo de comparacion pa que sepa como
     *                   comparar los elementos ej: Comparator.naturalOrder()
     * @param start      indice de inicio de la busqueda
     * @param end        indice de fin de la busqueda
     * @param trace      objeto de seguimiento de llamadas recursivas
     * @return
     */
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

        // Primera comprovacion o caso base se mira que el inicio sea mayor que el
        // final, si es asi no hay elementos por revisar y se retorna NOT_FOUND
        if (start > end) {
            trace.note("no quedan posiciones por revisar");
            trace.exit(call, NOT_FOUND);
            return NOT_FOUND;
        }
        // Se calcula la posición central de la lista a partir de lo que se sabe tambien
        // usable (start+end)/2
        int middle = start + (end - start) / 2;
        K middleKey = getKey.apply(list.get(middle));
        int comparison = comparator.compare(target, middleKey);

        trace.note("posición central=" + middle + ", clave=" + middleKey);
        // En caso de que la clave del elemento central sea igual a la clave del
        // elemento buscado, se retorna la posición central
        if (comparison == 0) {
            trace.note("elemento encontrado");
            trace.exit(call, middle);
            return middle;
        }
        // Si la clave del elemento buscado es menor que la clave del elemento central,
        // se busca en la mitad izquierda
        // Si la clave del elemento buscado es mayor que la clave del elemento central,
        // se busca en la mitad derecha
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
