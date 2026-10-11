package com.quindioexpress.exception;

/**
 * Se lanza cuando un dato no cumple las reglas de validación (peso, prioridad,
 * tiempo estimado, destino, identificadores vacíos, etc.).
 */
public class ValidationException extends QuindioExpressException {

    public ValidationException(String message) {
        super(message);
    }
}
