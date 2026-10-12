package com.uniquindio.edu.strucst.model;

import com.uniquindio.edu.strucst.exception.ValidationException;


final class Validations {

    private Validations() {
    }


    static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException("El campo '" + fieldName + "' no puede ser nulo ni vacío");
        }
        return value.trim();
    }
}
