package com.uniquindio.edu.strucst.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.uniquindio.edu.strucst.exception.AssignmentException;
import com.uniquindio.edu.strucst.model.Assignment;
import com.uniquindio.edu.strucst.model.Parcel;
import com.uniquindio.edu.strucst.model.ParcelStatus;

public class AssignmentService {
    private final Map<String, Assignment> activeAssignments = new HashMap<>();
    private final DeliveryHistory history;

    public AssignmentService(DeliveryHistory history) {
        this.history = history;
    }

    public Assignment assign(Parcel parcel, Courier courier) {
        if (!parcel.isPending()) {
            throw new AssignmentException("El paquete " + parcel.getCode()
                    + " no está pendiente (estado " + parcel.getStatus() + ")");
        }
        if (!courier.isAvailable()) {
            throw new AssignmentException("El repartidor " + courier.getId() + " no está disponible");
        }
        if (!courier.servesZone(parcel.getDestination())) {
            throw new AssignmentException("La zona del repartidor " + courier.getId() + " (" + courier.getZone()
                    + ") no corresponde al destino del paquete " + parcel.getCode()
                    + " (" + parcel.getDestination() + ")");
        }
        parcel.changeStatus(ParcelStatus.IN_TRANSIT);
        courier.markBusy();
        Assignment assignment = new Assignment(parcel, courier);
        activeAssignments.put(parcel.getCode(), assignment);
        return assignment;
    }

    public Assignment registerDelivery(Parcel parcel) {
        Assignment assignment = activeAssignments.remove(parcel.getCode());
        if (assignment == null) {
            throw new AssignmentException("El paquete " + parcel.getCode()
                    + " no tiene una asignación activa (estado " + parcel.getStatus() + ")");
        }
        parcel.changeStatus(ParcelStatus.DELIVERED);
        assignment.courier().markAvailable();
        history.record(parcel);
        return assignment;
    }

    public Optional<Assignment> findActiveAssignment(String parcelCode) {
        return Optional.ofNullable(activeAssignments.get(parcelCode));
    }

    public Map<String, Assignment> activeAssignments() {
        return Collections.unmodifiableMap(activeAssignments);
    }

}
