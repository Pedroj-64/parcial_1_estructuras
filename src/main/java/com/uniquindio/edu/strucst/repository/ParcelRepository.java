package com.uniquindio.edu.strucst.repository;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.uniquindio.edu.strucst.exception.DuplicateEntityException;
import com.uniquindio.edu.strucst.model.Parcel;
import com.uniquindio.edu.strucst.model.ParcelStatus;


public class ParcelRepository {
    private final Map<String, Parcel> parcelsByCode = new HashMap<>();
    private final List<Parcel> registrationOrder = new ArrayList<>();
    private final Set<String> destinations = new LinkedHashSet<>();
    private final SortedSet<String> sortedDestinations;
    private final Map<String, List<Parcel>> parcelsByDestination = new LinkedHashMap<>();

    public ParcelRepository() {
        Collator spanish = Collator.getInstance(Locale.of("es", "CO"));
        spanish.setStrength(Collator.PRIMARY);
        this.sortedDestinations = new TreeSet<>(spanish);
    }

    public void save(Parcel parcel) {
        if (exists(parcel.getCode())) {
            throw new DuplicateEntityException("Ya existe un paquete con el código " + parcel.getCode());
        }
        parcelsByCode.put(parcel.getCode(), parcel);
        registrationOrder.add(parcel);
        destinations.add(parcel.getDestination());
        sortedDestinations.add(parcel.getDestination());
        parcelsByDestination.computeIfAbsent(parcel.getDestination(), d -> new ArrayList<>()).add(parcel);
    }

    public boolean exists(String code) {
        return code != null && parcelsByCode.containsKey(code.trim());
    }

    public Optional<Parcel> findByCode(String code) {
        return code == null ? Optional.empty() : Optional.ofNullable(parcelsByCode.get(code.trim()));
    }

    public LinkedList<Parcel> listOfPending() {
        LinkedList<Parcel> pendingList = new LinkedList<>();
        registrationOrder.stream().filter(parcel -> parcel.isPending()).forEach(pendingList::add);
        return pendingList;
    }

    public List<Parcel> findAllInRegistrationOrder() {
        return Collections.unmodifiableList(registrationOrder);
    }

    public List<Parcel> findPendingInRegistrationOrder() {
        return registrationOrder.stream()
                .filter(parcel -> parcel.getStatus() != ParcelStatus.DELIVERED)
                .toList();
    }

    public SortedSet<String> findDestinationsSorted() {
        return Collections.unmodifiableSortedSet(sortedDestinations);
    }

    public Map<String, List<Parcel>> findGroupedByDestination() {
        Map<String, List<Parcel>> view = new LinkedHashMap<>();
        parcelsByDestination
                .forEach((destination, parcels) -> view.put(destination, Collections.unmodifiableList(parcels)));
        return Collections.unmodifiableMap(view);
    }

    public List<Parcel> findByDestination(String destination) {
        return Collections.unmodifiableList(parcelsByDestination.getOrDefault(destination, List.of()));
    }

    public List<Parcel> findAllSortedByCode() {
        List<Parcel> copy = new ArrayList<>(registrationOrder);
        Collections.sort(copy);
        return copy;
    }

    public int count() {
        return registrationOrder.size();
    }

}
