package com.databuilders.hoteles.modelo;

/**
 * Contrato de los elementos que se identifican por un id numérico.
 * Lo implementan Cliente, Hotel, Habitacion y Reserva.
 *
 * Sirve de cota (bound) para la clase genérica Repositorio: al exigir
 * T extends Identificable, el compilador garantiza que cualquier tipo
 * guardado tiene getId().
 *
 * Creada con ayuda de IA (Claude).
 */
public interface Identificable {

    /** Devuelve el identificador único del elemento. */
    int getId();
}
