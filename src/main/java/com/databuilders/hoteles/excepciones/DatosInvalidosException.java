package com.databuilders.hoteles.excepciones;

/**
 * Datos introducidos que no cumplen las reglas: campos obligatorios vacíos,
 * email con formato incorrecto, fechas incorrectas, número de personas o
 * identificadores no válidos.
 */
public class DatosInvalidosException extends GestionHotelesException {

    private static final long serialVersionUID = 1L;

    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }
}
