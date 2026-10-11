package com.quindioexpress.exception;

/**
 * Se lanza al intentar registrar un paquete o repartidor cuyo identificador ya existe.
 */
public class DuplicateEntityException extends QuindioExpressException {

    public DuplicateEntityException(String message) {
        super(message);
    }
}
