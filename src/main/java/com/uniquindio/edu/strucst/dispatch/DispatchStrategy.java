package com.uniquindio.edu.strucst.dispatch;

import java.util.Optional;

import com.uniquindio.edu.strucst.model.Parcel;

//Interface de la estrategia para despacho y lo que usara la base de cola de despacho
public interface DispatchStrategy {

    void enqueue(Parcel parcel);

    Optional<Parcel> peekNext();

    Optional<Parcel> pollNext();

    DispatchMode mode();

}
