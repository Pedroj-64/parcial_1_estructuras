package com.uniquindio.edu.strucst.dispatch;

import java.util.PriorityQueue;

import com.uniquindio.edu.strucst.model.comparator.ParcelComparators;

public class PriorityDispatchStrategy extends QueueDispatchStrategy {
    
        public PriorityDispatchStrategy() {
        super(new PriorityQueue<>(ParcelComparators.DISPATCH_ORDER));
    }

    @Override
    public DispatchMode mode() {
        return DispatchMode.PRIORITY;
    }
}
