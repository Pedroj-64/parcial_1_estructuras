package com.quindioexpress.model;

/**
 * Repartidor encargado de entregar paquetes en una zona (municipio).
 *
 * <p>Dos repartidores son iguales si tienen la misma identificación. Solo la
 * disponibilidad cambia durante la jornada.</p>
 */
public class Courier {

    private final String id;
    private final String name;
    private final String zone;
    private boolean available;

    /**
     * @param id        identificación única del repartidor
     * @param name      nombre completo
     * @param zone      municipio o zona principal de entregas
     * @param available si puede recibir una nueva asignación
     */
    public Courier(String id, String name, String zone, boolean available) {
        this.id = Validations.requireText(id, "identificación");
        this.name = Validations.requireText(name, "nombre");
        this.zone = Validations.requireText(zone, "zona");
        this.available = available;
    }

    /** Crea un repartidor disponible. */
    public Courier(String id, String name, String zone) {
        this(id, name, zone, true);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getZone() {
        return zone;
    }

    public boolean isAvailable() {
        return available;
    }

    /** Marca al repartidor como ocupado (no puede recibir asignaciones). */
    public void markBusy() {
        this.available = false;
    }

    /** Marca al repartidor como disponible para una nueva asignación. */
    public void markAvailable() {
        this.available = true;
    }

    /**
     * @param destination municipio destino de un paquete
     * @return {@code true} si la zona del repartidor corresponde a ese destino
     */
    public boolean servesZone(String destination) {
        return zone.equalsIgnoreCase(destination);
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Courier other && id.equals(other.id));
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s (%s, zona=%s, %s)", id, name, zone, available ? "disponible" : "ocupado");
    }
}
