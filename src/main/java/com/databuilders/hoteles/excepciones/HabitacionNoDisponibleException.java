package com.databuilders.hoteles.excepciones;

/**
 * Se intenta reservar una habitación que ya está ocupada en esas fechas.
 */
public class HabitacionNoDisponibleException extends GestionHotelesException {

    private static final long serialVersionUID = 1L;

    public HabitacionNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
