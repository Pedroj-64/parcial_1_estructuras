package com.uniquindio.edu.strucst.dispatch;

import java.util.PriorityQueue;

public class PriorityDispatchStrategy extends QueueDispatchStrategy {
    
        public PriorityDispatchStrategy() {
        super(new PriorityQueue<>(ParcelComparators.DISPATCH_ORDER));
    }

    @Override
    public DispatchMode mode() {
        return DispatchMode.PRIORITY;
    }
}
