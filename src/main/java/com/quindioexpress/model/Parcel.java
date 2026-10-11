package com.quindioexpress.model;

import com.quindioexpress.exception.ValidationException;

import java.util.Objects;

/**
 * Paquete registrado durante una jornada en el centro de distribución.
 *
 * <p><b>Orden natural:</b> los paquetes se comparan por su código en orden ascendente
 * ({@link Comparable}). Los criterios alternativos (prioridad, peso, tiempo) viven en
 * {@link com.quindioexpress.model.comparator.ParcelComparators} y no alteran este orden.</p>
 *
 * <p><b>Identidad:</b> dos paquetes son iguales si tienen el mismo código, coherente con
 * la regla de negocio "no pueden existir dos paquetes con el mismo código".</p>
 *
 * <p>Los datos del paquete son inmutables; solo su {@link ParcelStatus} cambia a lo largo
 * de la jornada.</p>
 */
public class Parcel implements Comparable<Parcel> {

    /** Prioridad mínima permitida. */
    public static final int MIN_PRIORITY = 1;
    /** Prioridad máxima permitida. */
    public static final int MAX_PRIORITY = 5;
    /** Prioridad asignada cuando no se indica una. */
    public static final int DEFAULT_PRIORITY = 5;

    private final String code;
    private final String destination;
    private final double weight;
    private final int priority;
    private final int estimatedMinutes;
    private ParcelStatus status = ParcelStatus.PENDING;

    /**
     * Crea un paquete validando todas las reglas del enunciado.
     *
     * @param code             código único, por ejemplo {@code PQ731}
     * @param destination      municipio destino; no puede ser nulo ni vacío
     * @param weight           peso en kilogramos; debe ser mayor que cero
     * @param priority         prioridad entre {@value #MIN_PRIORITY} y {@value #MAX_PRIORITY}
     * @param estimatedMinutes minutos estimados de entrega; debe ser mayor que cero
     * @throws ValidationException si algún dato es inválido
     */
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

    /**
     * Crea un paquete con la prioridad por defecto ({@value #DEFAULT_PRIORITY}).
     */
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

    /** @return {@code true} si el paquete aún espera ser asignado */
    public boolean isPending() {
        return status == ParcelStatus.PENDING;
    }

    /**
     * Cambia el estado del paquete. Está pensado para ser invocado por los servicios
     * de despacho y entrega, que son quienes conocen las transiciones válidas.
     */
    public void changeStatus(ParcelStatus newStatus) {
        this.status = Objects.requireNonNull(newStatus, "El estado no puede ser nulo");
    }

    /**
     * Orden natural: código ascendente.
     */
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

    /**
     * @return descripción completa del paquete, útil para reportes
     */
    public String describe() {
        return String.format("%s | destino=%s | peso=%.1f kg | prioridad=%d | tiempo=%d min | estado=%s",
                code, destination, weight, priority, estimatedMinutes, status);
    }
}
