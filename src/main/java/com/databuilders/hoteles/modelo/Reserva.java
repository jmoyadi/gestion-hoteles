
package com.databuilders.hoteles.modelo;

import com.databuilders.hoteles.excepciones.DatosInvalidosException;
import com.databuilders.hoteles.excepciones.OperacionNoPermitidaException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Representa una reserva de la cadena hotelera.
 *
 * Contiene los datos básicos necesarios para
 * identificar y consultar las reservas.
 *
 * Es final (no admite subclases): el constructor llama a calcularImporte()
 * y así ninguna subclase puede cambiar ese cálculo antes de terminar de construirse.
 *
 * Atributos de relación y validaciones de la reserva añadidos con ayuda de IA (Claude).
 */
public final class Reserva {

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

    public Reserva(int id, LocalDate fechaEntrada, LocalDate fechaSalida,
                   int numPersonas, Cliente cliente, Hotel hotel,
                   List<Habitacion> habitaciones) throws DatosInvalidosException {

        if (id <= 0) {
            throw new DatosInvalidosException("El ID debe ser positivo.");
        }
        if (fechaEntrada == null || fechaSalida == null) {
            throw new DatosInvalidosException("Las fechas son obligatorias.");
        }
        if (!fechaSalida.isAfter(fechaEntrada)) {
            throw new DatosInvalidosException(
                    "La fecha de salida debe ser posterior a la entrada.");
        }
        if (numPersonas <= 0) {
            throw new DatosInvalidosException(
                    "El número de personas debe ser positivo.");
        }
        if (cliente == null || hotel == null) {
            throw new DatosInvalidosException(
                    "La reserva debe tener un cliente y un hotel.");
        }
        if (habitaciones == null || habitaciones.isEmpty()) {
            throw new DatosInvalidosException(
                    "La reserva debe incluir al menos una habitación.");
        }

        // Ninguna habitación repetida (un Set no admite duplicados)
        if (new HashSet<>(habitaciones).size() != habitaciones.size()) {
            throw new DatosInvalidosException(
                    "La reserva incluye una habitación repetida.");
        }

        // Todas las habitaciones pertenecen al hotel de la reserva (nota del diagrama)
        // y se suma su capacidad para comprobar el número de personas
        List<Habitacion> delHotel = hotel.getHabitaciones();
        int capacidadTotal = 0;
        for (Habitacion habitacion : habitaciones) {
            if (habitacion == null) {
                throw new IllegalArgumentException("La lista contiene una habitación nula.");
            }
            if (!delHotel.contains(habitacion)) {
                throw new DatosInvalidosException("La habitación " + habitacion.getNumero()
                        + " no pertenece al hotel " + hotel.getNombre() + ".");
            }
            capacidadTotal += habitacion.getCapacidad();
        }

        // numPersonas <= suma de capacidades (nota del diagrama)
        if (numPersonas > capacidadTotal) {
            throw new DatosInvalidosException("Las habitaciones elegidas admiten "
                    + capacidadTotal + " personas y la reserva es para " + numPersonas + ".");
        }

        this.id = id;
        this.fechaEntrada = fechaEntrada;
        this.fechaSalida = fechaSalida;
        this.numPersonas = numPersonas;
        this.cliente = cliente;
        this.hotel = hotel;
        this.habitaciones = new ArrayList<>(habitaciones);
        this.estado = EstadoReserva.CONFIRMADA;

        this.importe = calcularImporte();
    }

    public long calcularNoches() {
        return ChronoUnit.DAYS.between(fechaEntrada, fechaSalida);
    }

    public double calcularImporte() {
        long noches = calcularNoches();
        double total = 0;

        for (Habitacion habitacion : habitaciones) {
            total += habitacion.getPrecioNoche() * noches;
        }

        return total;
    }

    public void cancelar() throws OperacionNoPermitidaException {
        if (estado == EstadoReserva.CANCELADA) {
            throw new OperacionNoPermitidaException(
                    "La reserva ya está cancelada.");
        }
        if (estado == EstadoReserva.FINALIZADA) {
            throw new OperacionNoPermitidaException(
                    "No se puede cancelar una reserva finalizada.");
        }

        estado = EstadoReserva.CANCELADA;
    }

    public int getId() {
        return id;
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public int getNumPersonas() {
        return numPersonas;
    }

    public double getImporte() {
        return importe;
    }

    public EstadoReserva getEstado() {
        return estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public List<Habitacion> getHabitaciones() {
        return new ArrayList<>(habitaciones);
    }

    /**
     * Texto legible de la reserva. Del cliente y del hotel solo se muestra el
     * nombre: imprimir el objeto entero provocaría un bucle infinito, porque el
     * cliente también conoce sus reservas.
     */
    @Override
    public String toString() {
        return String.format("[%d] %s → %s (%d noches) · %s %s · %s · %d hab. · %d pers. · %.2f € · %s",
                id, fechaEntrada, fechaSalida, calcularNoches(),
                cliente.getNombre(), cliente.getApellidos(), hotel.getNombre(),
                habitaciones.size(), numPersonas, importe, estado);
    }
}
