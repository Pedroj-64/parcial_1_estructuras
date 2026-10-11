package com.uniquindio.edu.strucst.repository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.uniquindio.edu.strucst.exception.DuplicateEntityException;
import com.uniquindio.edu.strucst.exception.EntityNotFoundException;
import com.uniquindio.edu.strucst.model.Courier;

public class CourierRepository {

    private final Map<String, Courier> couriersById = new LinkedHashMap<>();
    private final Map<String, List<Courier>> couriersByZone = new HashMap<>();

    public void save(Courier courier) {
        if (couriersById.containsKey(courier.getId())) {
            throw new DuplicateEntityException("Ya existe un repartidor con la identificación " + courier.getId());
        }
        couriersById.put(courier.getId(), courier);
        couriersByZone.computeIfAbsent(zoneKey(courier.getZone()), z -> new ArrayList<>()).add(courier);
    }

    public Optional<Courier> findById(String id) {
        return id == null ? Optional.empty() : Optional.ofNullable(couriersById.get(id.trim()));
    }

    public Courier getById(String id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException("No existe un repartidor con la identificación " + id));
    }

    public Optional<Courier> findFirstAvailableInZone(String zone) {
        return findByZone(zone).stream().filter(Courier::isAvailable).findFirst();
    }

    public List<Courier> findByZone(String zone) {
        return Collections.unmodifiableList(couriersByZone.getOrDefault(zoneKey(zone), List.of()));
    }

    public Collection<Courier> findAll() {
        return Collections.unmodifiableCollection(couriersById.values());
    }

    public int count() {
        return couriersById.size();
    }

    private static String zoneKey(String zone) {
        return zone == null ? "" : zone.trim().toLowerCase();
    }
}
