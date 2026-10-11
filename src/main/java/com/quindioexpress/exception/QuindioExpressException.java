package com.quindioexpress.exception;

/**
 * Excepción base del dominio. Todas las violaciones de reglas de negocio de
 * QuindíoExpress extienden esta clase, lo que permite capturarlas de forma uniforme.
 */
public class QuindioExpressException extends RuntimeException {

    public QuindioExpressException(String message) {
        super(message);
    }
}
