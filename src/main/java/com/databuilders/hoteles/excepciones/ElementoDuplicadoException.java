package com.databuilders.hoteles.excepciones;

/**
 * Se intenta dar de alta un elemento con un identificador que ya existe.
 */
public class ElementoDuplicadoException extends GestionHotelesException {

    private static final long serialVersionUID = 1L;

    public ElementoDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
