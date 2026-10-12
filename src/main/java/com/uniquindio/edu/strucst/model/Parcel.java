package com.uniquindio.edu.strucst.model;

import java.util.Objects;

import com.uniquindio.edu.strucst.exception.ValidationException;
//El paquete normal solo que este tiene mas validaciones
public class Parcel implements Comparable<Parcel> {

    public static final int MIN_PRIORITY = 1;

    public static final int MAX_PRIORITY = 5;

    public static final int DEFAULT_PRIORITY = 3;

    private final String code;
    private final String destination;
    private final double weight;
    private final int priority;
    private final int estimatedMinutes;
    private ParcelStatus status = ParcelStatus.PENDING;

    public Parcel(String code, String destination, double weight, int priority, int estimatedMinutes) {
        this.code = Validations.requireText(code, "código");
        this.destination = Validations.requireText(destination, "destino");
        if (!(weight > 0) || Double.isInfinite(weight)) {
            throw new ValidationException("El peso debe ser mayor que cero: " + weight);
        }
        if (priority < MIN_PRIORITY || priority > MAX_PRIORITY) {
            throw new ValidationException("La prioridad debe estar entre " + MIN_PRIORITY
                    + " y " + MAX_PRIORITY + ": " + priority);
        }
        if (estimatedMinutes <= 0) {
            throw new ValidationException("El tiempo estimado debe ser mayor que cero: " + estimatedMinutes);
        }
        this.weight = weight;
        this.priority = priority;
        this.estimatedMinutes = estimatedMinutes;
    }

    public Parcel(String code, String destination, double weight, int estimatedMinutes) {
        this(code, destination, weight, DEFAULT_PRIORITY, estimatedMinutes);
    }

    public String getCode() {
        return code;
    }

    public String getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    public int getPriority() {
        return priority;
    }

    public int getEstimatedMinutes() {
        return estimatedMinutes;
    }

    public ParcelStatus getStatus() {
        return status;
    }

    public boolean isPending() {
        return status == ParcelStatus.PENDING;
    }

    public void changeStatus(ParcelStatus newStatus) {
        this.status = Objects.requireNonNull(newStatus, "El estado no puede ser nulo");
    }

    @Override
    public int compareTo(Parcel other) {
        return code.compareTo(other.code);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Parcel other && code.equals(other.code));
    }

    @Override
    public int hashCode() {
        return code.hashCode();
    }

    @Override
    public String toString() {
        return code;
    }

    public String describe() {
        return String.format("%s | destino=%s | peso=%.1f kg | prioridad=%d | tiempo=%d min | estado=%s",
                code, destination, weight, priority, estimatedMinutes, status);
    }
}
