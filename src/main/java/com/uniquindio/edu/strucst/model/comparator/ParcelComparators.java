package com.uniquindio.edu.strucst.model.comparator;

import java.util.Comparator;

import com.uniquindio.edu.strucst.model.Parcel;


public final class ParcelComparators {


    public static final Comparator<Parcel> BY_PRIORITY_DESC =
            Comparator.comparingInt(Parcel::getPriority).reversed();


    public static final Comparator<Parcel> BY_WEIGHT_DESC =
            Comparator.comparingDouble(Parcel::getWeight).reversed();


    public static final Comparator<Parcel> BY_WEIGHT_ASC =
            Comparator.comparingDouble(Parcel::getWeight);


    public static final Comparator<Parcel> BY_ESTIMATED_TIME_ASC =
            Comparator.comparingInt(Parcel::getEstimatedMinutes);


    public static final Comparator<Parcel> BY_CODE = Comparator.naturalOrder();


    public static final Comparator<Parcel> DISPATCH_ORDER =
            BY_PRIORITY_DESC.thenComparing(BY_ESTIMATED_TIME_ASC).thenComparing(BY_CODE);

    private ParcelComparators() {
    }
}
