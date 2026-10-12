package com.uniquindio.edu.strucst.model.comparator;

import java.util.Comparator;

import com.uniquindio.edu.strucst.model.Parcel;

//Comparators para los paquetes dependiendo de la prioridad, peso, tiempo estimado y codigo de los paquetes
public final class ParcelComparators {

        // Se usa labda llamando al metodo por cada parcel usado
        public static final Comparator<Parcel> BY_PRIORITY_DESC = Comparator.comparingInt(Parcel::getPriority)
                        .reversed();

        public static final Comparator<Parcel> BY_WEIGHT_DESC = Comparator.comparingDouble(Parcel::getWeight)
                        .reversed();

        public static final Comparator<Parcel> BY_WEIGHT_ASC = Comparator.comparingDouble(Parcel::getWeight);

        public static final Comparator<Parcel> BY_ESTIMATED_TIME_ASC = Comparator
                        .comparingInt(Parcel::getEstimatedMinutes);

        public static final Comparator<Parcel> BY_CODE = Comparator.naturalOrder();

        public static final Comparator<Parcel> DISPATCH_ORDER = BY_PRIORITY_DESC.thenComparing(BY_ESTIMATED_TIME_ASC)
                        .thenComparing(BY_CODE);

        private ParcelComparators() {
        }
}
