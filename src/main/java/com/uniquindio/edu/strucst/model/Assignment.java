package com.uniquindio.edu.strucst.model;

import java.util.Objects;

/**
 * Asignación inmutable de un paquete a un repartidor.
 *
 * @param parcel  paquete asignado
 * @param courier repartidor responsable de la entrega
 */
public record Assignment(Parcel parcel, Courier courier) {

    public Assignment {
        Objects.requireNonNull(parcel, "El paquete no puede ser nulo");
        Objects.requireNonNull(courier, "El repartidor no puede ser nulo");
    }

    @Override
    public String toString() {
        return parcel.getCode() + " -> " + courier.getId() + " (" + courier.getName() + ", " + courier.getZone() + ")";
    }
}
