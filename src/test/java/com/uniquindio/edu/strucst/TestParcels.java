package com.uniquindio.edu.strucst;

import com.uniquindio.edu.strucst.model.Parcel;

/**
 * Fábrica de paquetes para las pruebas, para no repetir datos irrelevantes.
 */
public final class TestParcels {

    private TestParcels() {
    }

    public static Parcel parcel(String code) {
        return new Parcel(code, "Armenia", 1.0, 3, 10);
    }

    public static Parcel parcel(String code, String destination, double weight) {
        return new Parcel(code, destination, weight, 3, 10);
    }

    public static Parcel parcel(String code, int priority, int estimatedMinutes) {
        return new Parcel(code, "Armenia", 1.0, priority, estimatedMinutes);
    }
}
