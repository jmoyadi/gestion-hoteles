
package com.databuilders.hoteles.modelo;

import java.time.LocalDate;

/**
 * Representa una reserva de la cadena hotelera.
 *
 * Contiene los datos básicos necesarios para
 * identificar y consultar las reservas.
 */
public class Reserva {

    // Atributos privados de Reserva
    private int id;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private int numPersonas;
    private double importe;
    private EstadoReserva estado;

}
