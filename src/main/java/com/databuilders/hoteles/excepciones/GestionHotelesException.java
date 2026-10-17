package com.databuilders.hoteles.excepciones;

/**
 * Clase base de los errores propios de la aplicación de gestión hotelera.
 *
 * Es abstracta porque nunca se lanza directamente: siempre se lanza una de sus
 * subclases, que indica el tipo de error. Así la vista puede capturar todos los
 * errores con un único catch (GestionHotelesException e) y mostrar el mensaje.
 *
 * Es una excepción comprobada (extiende Exception): Java obliga a declararla con
 * throws y a capturarla, de modo que su gestión queda explícita en el código.
 *
 * Excepciones personalizadas creadas con ayuda de IA (Claude).
 */
public abstract class GestionHotelesException extends Exception {

    private static final long serialVersionUID = 1L;

    protected GestionHotelesException(String mensaje) {
        super(mensaje);
    }
}
