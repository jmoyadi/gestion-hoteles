package com.databuilders.hoteles.excepciones;

/**
 * La operación no se puede hacer en el estado actual: por ejemplo,
 * cancelar una reserva ya cancelada o finalizada.
 */
public class OperacionNoPermitidaException extends GestionHotelesException {

    private static final long serialVersionUID = 1L;

    public OperacionNoPermitidaException(String mensaje) {
        super(mensaje);
    }
}
