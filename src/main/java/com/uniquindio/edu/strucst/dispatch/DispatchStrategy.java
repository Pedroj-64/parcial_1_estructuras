package com.uniquindio.edu.strucst.dispatch;

import java.util.Optional;

import com.uniquindio.edu.strucst.model.Parcel;

public interface DispatchStrategy {

    void enqueue(Parcel parcel);

    Optional<Parcel> peekNext();

    Optional<Parcel> pollNext();

    DispatchMode mode();

}
