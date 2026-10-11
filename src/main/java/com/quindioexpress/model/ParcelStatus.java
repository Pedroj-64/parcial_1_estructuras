package com.quindioexpress.model;

/**
 * Ciclo de vida de un paquete dentro de la jornada.
 *
 * <pre>
 * PENDING ──asignar──▶ IN_TRANSIT ──registrar entrega──▶ DELIVERED
 * </pre>
 */
public enum ParcelStatus {
    /** Registrado y a la espera de ser asignado a un repartidor. */
    PENDING,
    /** Asignado a un repartidor y en camino a su destino. */
    IN_TRANSIT,
    /** Entregado; forma parte del historial de entregas. */
    DELIVERED
}
