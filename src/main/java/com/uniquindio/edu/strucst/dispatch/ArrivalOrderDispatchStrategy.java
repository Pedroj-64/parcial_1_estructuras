package com.uniquindio.edu.strucst.dispatch;

import java.util.ArrayDeque;

//Implementacion de estrategia por orden de llegada usando en su constructor un ArrayDeque
public class ArrivalOrderDispatchStrategy extends QueueDispatchStrategy {

    public ArrivalOrderDispatchStrategy() {
        super(new ArrayDeque<>());
    }

    @Override
    public DispatchMode mode() {
        return DispatchMode.ARRIVAL_ORDER;
    }

}
