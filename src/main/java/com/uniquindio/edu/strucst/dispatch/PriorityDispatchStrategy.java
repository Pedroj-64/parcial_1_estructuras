package com.uniquindio.edu.strucst.dispatch;

import java.util.PriorityQueue;

import com.uniquindio.edu.strucst.model.comparator.ParcelComparators;

//Implementacion de estrategia por prioridad usando en su constructor el comparator dado en model
public class PriorityDispatchStrategy extends QueueDispatchStrategy {

    public PriorityDispatchStrategy() {
        super(new PriorityQueue<>(ParcelComparators.DISPATCH_ORDER));
    }

    @Override
    public DispatchMode mode() {
        return DispatchMode.PRIORITY;
    }
}
