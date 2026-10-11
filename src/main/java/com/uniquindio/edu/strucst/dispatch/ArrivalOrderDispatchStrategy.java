package com.uniquindio.edu.strucst.dispatch;

import java.util.ArrayDeque;

public class ArrivalOrderDispatchStrategy extends QueueDispatchStrategy {

    public ArrivalOrderDispatchStrategy() {
        super(new ArrayDeque<>());
    }

    @Override
    public DispatchMode mode() {
        return DispatchMode.ARRIVAL_ORDER;
    }
    
}
