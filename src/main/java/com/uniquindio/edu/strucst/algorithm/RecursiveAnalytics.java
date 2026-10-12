package com.uniquindio.edu.strucst.algorithm;

import com.uniquindio.edu.strucst.model.Parcel;

import java.util.List;
import java.util.Objects;


/**
 * Operaciones de análisis sobre paquetes implementadas mediante recursividad.
 *
 *Los métodos recorren la lista desde el primer elemento hasta el último.
 * Cada llamada procesa el paquete ubicado en el índice actual y delega el
 * resto del recorrido a la siguiente llamada recursiva.
 */
public final class RecursiveAnalytics {

    // La clase solo expone operaciones estáticas.
    private RecursiveAnalytics() {
    }


    /**
     * Calcula el peso total de los paquetes enviados a un destino.
     *
     * La búsqueda del destino no distingue entre mayúsculas y minúsculas.
     * El seguimiento de llamadas recursivas se encuentra desactivado por
     * defecto.
     *
     * @param parcels lista de paquetes que se va a recorrer
     * @param destination destino cuyos paquetes se tendrán en cuenta
     * @return suma de los pesos de los paquetes que coinciden con el destino
     */
    public static double totalWeightByDestination(List<Parcel> parcels, String destination) {
        return totalWeightByDestination(parcels, destination, CallTrace.disabled());
    }


    /**
     * Calcula recursivamente el peso total de los paquetes de un destino.
     *
     * @param parcels lista de paquetes que se va a recorrer
     * @param destination destino cuyos paquetes se tendrán en cuenta
     * @param trace objeto utilizado para registrar las llamadas recursivas
     * @return suma de los pesos de los paquetes que coinciden con el destino
     */
    public static double totalWeightByDestination(List<Parcel> parcels, String destination, CallTrace trace) {
        Objects.requireNonNull(parcels, "La lista de paquetes no puede ser nula");
        Objects.requireNonNull(destination, "El destino no puede ser nulo");
        Objects.requireNonNull(trace, "El seguimiento no puede ser nulo");
        return totalWeightFrom(parcels, destination, 0, trace);
    }

    /**
     * Recorre la lista y suma el aporte del paquete actual.
     *
     * El caso base ocurre cuando el índice alcanza el tamaño de la lista;
     * en ese momento no quedan paquetes por procesar y se retorna cero
     */
    private static double totalWeightFrom(List<Parcel> parcels, String destination, int index, CallTrace trace) {
        String call = "totalWeight(index=" + index + ")";
        trace.enter(call);
        if (index == parcels.size()) {
            trace.note("caso base: no quedan paquetes");
            trace.exit(call, 0.0);
            return 0.0;
        }
        Parcel current = parcels.get(index);
        double contribution = current.getDestination().equalsIgnoreCase(destination) ? current.getWeight() : 0.0;
        trace.note(current.getCode() + " → " + current.getDestination() + " aporta " + contribution);
        double result = contribution + totalWeightFrom(parcels, destination, index + 1, trace);
        trace.exit(call, result);
        return result;
    }


    /**
     * Cuenta los paquetes cuya prioridad es igual o superior a la indicada.
     *
     * El seguimiento de llamadas recursivas se encuentra desactivado por
     * defecto.
     *
     * @param parcels lista de paquetes que se va a recorrer
     * @param minimumPriority prioridad mínima que debe cumplir cada paquete
     * @return cantidad de paquetes con prioridad suficiente
     */
    public static int countWithMinimumPriority(List<Parcel> parcels, int minimumPriority) {
        return countWithMinimumPriority(parcels, minimumPriority, CallTrace.disabled());
    }


    /**
     * Cuenta recursivamente los paquetes que cumplen una prioridad mínima.
     *
     * @param parcels lista de paquetes que se va a recorrer
     * @param minimumPriority prioridad mínima que debe cumplir cada paquete
     * @param trace objeto utilizado para registrar las llamadas recursivas
     * @return cantidad de paquetes con prioridad mayor o igual a la mínima
¿
     */
    public static int countWithMinimumPriority(List<Parcel> parcels, int minimumPriority, CallTrace trace) {
        Objects.requireNonNull(parcels, "La lista de paquetes no puede ser nula");
        Objects.requireNonNull(trace, "El seguimiento no puede ser nulo");
        return countFrom(parcels, minimumPriority, 0, trace);
    }

    /**
     * Procesa un paquete y delega el conteo de los elementos restantes.
     *
     * El caso base ocurre cuando el índice alcanza el tamaño de la lista;
     * en ese momento se retorna cero porque no quedan paquetes por contar.
     */
    private static int countFrom(List<Parcel> parcels, int minimumPriority, int index, CallTrace trace) {
        String call = "count(index=" + index + ")";
        trace.enter(call);
        if (index == parcels.size()) {
            trace.note("caso base: no quedan paquetes");
            trace.exit(call, 0);
            return 0;
        }
        Parcel current = parcels.get(index);
        int contribution = current.getPriority() >= minimumPriority ? 1 : 0;
        trace.note(current.getCode() + " prioridad " + current.getPriority() + " aporta " + contribution);
        int result = contribution + countFrom(parcels, minimumPriority, index + 1, trace);
        trace.exit(call, result);
        return result;
    }
}
