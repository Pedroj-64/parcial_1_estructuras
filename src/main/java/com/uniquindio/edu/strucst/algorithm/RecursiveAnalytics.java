package com.uniquindio.edu.strucst.algorithm;

import com.uniquindio.edu.strucst.model.Parcel;

import java.util.List;
import java.util.Objects;


public final class RecursiveAnalytics {

    private RecursiveAnalytics() {
    }


    public static double totalWeightByDestination(List<Parcel> parcels, String destination) {
        return totalWeightByDestination(parcels, destination, CallTrace.disabled());
    }


    public static double totalWeightByDestination(List<Parcel> parcels, String destination, CallTrace trace) {
        Objects.requireNonNull(parcels, "La lista de paquetes no puede ser nula");
        Objects.requireNonNull(destination, "El destino no puede ser nulo");
        return totalWeightFrom(parcels, destination, 0, trace);
    }

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


    public static int countWithMinimumPriority(List<Parcel> parcels, int minimumPriority) {
        return countWithMinimumPriority(parcels, minimumPriority, CallTrace.disabled());
    }


    public static int countWithMinimumPriority(List<Parcel> parcels, int minimumPriority, CallTrace trace) {
        Objects.requireNonNull(parcels, "La lista de paquetes no puede ser nula");
        return countFrom(parcels, minimumPriority, 0, trace);
    }

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
