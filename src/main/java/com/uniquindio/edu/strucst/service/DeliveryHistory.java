package com.uniquindio.edu.strucst.service;

import com.uniquindio.edu.strucst.model.Parcel;
import com.uniquindio.edu.strucst.structure.SimpleList;
import com.uniquindio.edu.strucst.structure.SinglyLinkedList;

import java.util.Iterator;

public class DeliveryHistory implements Iterable<Parcel> {

    private final SimpleList<Parcel> deliveries = new SinglyLinkedList<>();

    public void record(Parcel parcel) {
        deliveries.add(parcel);
    }

    public Parcel get(int index) {
        return deliveries.get(index);
    }

    public boolean contains(Parcel parcel) {
        return deliveries.contains(parcel);
    }

    public int size() {
        return deliveries.size();
    }

    public boolean isEmpty() {
        return deliveries.isEmpty();
    }

    @Override
    public Iterator<Parcel> iterator() {
        return deliveries.iterator();
    }

    @Override
    public String toString() {
        return deliveries.toString();
    }
}
