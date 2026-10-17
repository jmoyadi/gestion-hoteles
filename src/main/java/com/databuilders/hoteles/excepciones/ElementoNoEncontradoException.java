package com.databuilders.hoteles.excepciones;

/**
 * Se busca un cliente, hotel, habitación o reserva que no existe.
 */
public class ElementoNoEncontradoException extends GestionHotelesException {

    private static final long serialVersionUID = 1L;

    public ElementoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
