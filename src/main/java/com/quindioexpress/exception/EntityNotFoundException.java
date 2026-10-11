package com.quindioexpress.exception;

/**
 * Se lanza cuando se solicita un paquete o repartidor que no está registrado.
 */
public class EntityNotFoundException extends QuindioExpressException {

    public EntityNotFoundException(String message) {
        super(message);
    }
}
