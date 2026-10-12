package com.uniquindio.edu.strucst.dispatch;

import java.util.Optional;
import java.util.Queue;

import com.uniquindio.edu.strucst.model.Parcel;

//Clase abstracta que implementa la interfaz DispatchStrategy y proporciona una implementación básica para las operaciones de despacho utilizando una cola.
public abstract class QueueDispatchStrategy implements DispatchStrategy {
    private final Queue<Parcel> queue;
    //Clase base para las estregias usa metodos simples
    protected QueueDispatchStrategy(Queue<Parcel> queue) {
        this.queue = queue;
    }

    @Override
    public void enqueue(Parcel parcel) {
        queue.offer(parcel);
    }

    @Override
    public Optional<Parcel> peekNext() {
        discardNonPendingHead();
        return Optional.ofNullable(queue.peek());
    }

    @Override
    public Optional<Parcel> pollNext() {
        discardNonPendingHead();
        return Optional.ofNullable(queue.poll());
    }

    private void discardNonPendingHead() {
        while (!queue.isEmpty() && !queue.peek().isPending()) {
            queue.poll();
        }
    }
}
