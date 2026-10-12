package com.uniquindio.edu.strucst.model;
//Clase de repartidor normalito
public class Courier {

    private final String id;
    private final String name;
    private final String zone;
    private boolean available;

    public Courier(String id, String name, String zone, boolean available) {
        this.id = Validations.requireText(id, "identificación");
        this.name = Validations.requireText(name, "nombre");
        this.zone = Validations.requireText(zone, "zona");
        this.available = available;
    }

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

    public void markBusy() {
        this.available = false;
    }

    public void markAvailable() {
        this.available = true;
    }

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
