package com.uniquindio.edu.strucst.exception;

/**
 * Se lanza cuando una asignación o entrega viola las reglas de despacho: repartidor no
 * disponible, zona distinta al destino, paquete en un estado que no lo permite, etc.
 */
public class AssignmentException extends QuindioExpressException {

    public AssignmentException(String message) {
        super(message);
    }
}
