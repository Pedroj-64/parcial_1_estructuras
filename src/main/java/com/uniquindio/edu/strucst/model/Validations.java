package com.uniquindio.edu.strucst.model;

import com.uniquindio.edu.strucst.exception.ValidationException;
//Validaciones para los campos de los paquetes y repartidores, para que no sean nulos ni vacios
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
