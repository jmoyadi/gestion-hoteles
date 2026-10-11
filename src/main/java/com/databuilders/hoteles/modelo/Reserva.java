
package com.databuilders.hoteles.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una reserva de la cadena hotelera.
 *
 * Contiene los datos básicos necesarios para
 * identificar y consultar las reservas.
 *
 * Atributos de relación añadidos con ayuda de IA (Claude).
 */
public class Reserva {

    // Atributos privados de Reserva
    private int id;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private int numPersonas;
    private double importe;
    private EstadoReserva estado;

    // Relaciones (líneas del diagrama de clases)
    // Cliente 1 —realiza— 0..* Reserva: cliente que hace la reserva
    private Cliente cliente;
    // Hotel 1 —recibe— 0..* Reserva: hotel de la reserva
    private Hotel hotel;
    // Reserva 0..* —incluye— 1..* Habitacion: habitaciones reservadas
    private List<Habitacion> habitaciones = new ArrayList<>();

}
