package com.quindioexpress.model;

import com.quindioexpress.exception.ValidationException;

/**
 * Utilidades de validación compartidas por las entidades del dominio.
 * Centralizarlas evita repetir las mismas reglas en varias clases.
 */
final class Validations {

    private Validations() {
    }

    /**
     * Verifica que el texto no sea nulo ni vacío y lo devuelve sin espacios sobrantes.
     *
     * @param value     texto a validar
     * @param fieldName nombre del campo, usado en el mensaje de error
     * @return el texto recortado
     */
    static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("El campo '" + fieldName + "' no puede ser nulo ni vacío");
        }
        return value.trim();
    }
}
