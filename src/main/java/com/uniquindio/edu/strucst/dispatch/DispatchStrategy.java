package com.uniquindio.edu.strucst.dispatch;

import java.util.Optional;

public interface DispatchStrategy {

    void enqueue(Parcel parcel);

    Optional<Parcel> peekNext();

    Optional<Parcel> pollNext();

    DispatchMode mode();

}
