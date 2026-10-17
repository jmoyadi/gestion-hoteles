package com.databuilders.hoteles.modelo;

import com.databuilders.hoteles.excepciones.DatosInvalidosException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una habitación de un hotel.
 *
 * CLASE PADRE de los diferentes tipos de habitación:
 * Estandar, Superior y Suite.
 *
 * Contiene los atributos comunes a todas las habitaciones.
 *
 * Es ABSTRACTA: no se puede crear una habitación genérica,
 * solo de un tipo concreto (Estandar, Superior o Suite).
 *
 * estaDisponible() y la lista de reservas corregidos con ayuda de IA (Claude).
 */
public abstract class Habitacion {

    // Atributos privados comunes a todas las habitaciones
    private int id;
    private int numero;
    private int capacidad;
    private double precioNoche;

    // Relaciones (líneas del diagrama de clases)
    // Reserva 0..* —incluye— 1..* Habitacion: reservas en las que está esta habitación.
    // La asociación no tiene flecha en el diagrama, así que se puede recorrer en los
    // dos sentidos; la habitación necesita conocer sus reservas para saber si está libre.
    private final List<Reserva> reservas = new ArrayList<>();

    public Habitacion(int id, int numero, int capacidad, double precioNoche)
            throws DatosInvalidosException {
        if (id <= 0) {
            throw new DatosInvalidosException("El ID debe ser positivo.");
        }
        if (numero <= 0) {
            throw new DatosInvalidosException("El número de habitación debe ser positivo.");
        }
        if (capacidad <= 0) {
            throw new DatosInvalidosException("La capacidad debe ser mayor que cero.");
        }
        if (precioNoche < 0) {
            throw new DatosInvalidosException("El precio por noche no puede ser negativo.");
        }

        this.id = id;
        this.numero = numero;
        this.capacidad = capacidad;
        this.precioNoche = precioNoche;
    }

    public int getId() {
        return id;
    }

    public int getNumero() {
        return numero;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public double getPrecioNoche() {
        return precioNoche;
    }

    public void setNumero(int numero) throws DatosInvalidosException {
        if (numero <= 0) {
            throw new DatosInvalidosException("El número de habitación debe ser positivo.");
        }
        this.numero = numero;
    }

    public void setCapacidad(int capacidad) throws DatosInvalidosException {
        if (capacidad <= 0) {
            throw new DatosInvalidosException("La capacidad debe ser mayor que cero.");
        }
        this.capacidad = capacidad;
    }

    public void setPrecioNoche(double precioNoche) throws DatosInvalidosException {
        if (precioNoche < 0) {
            throw new DatosInvalidosException("El precio por noche no puede ser negativo.");
        }
        this.precioNoche = precioNoche;
    }

    /**
     * Registra una reserva que incluye esta habitación.
     * Visibilidad de paquete: solo la llama Datos al dar de alta la reserva,
     * para que la vista no pueda alterar la ocupación directamente.
     */
    void agregarReserva(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no puede ser nula.");
        }
        if (!reservas.contains(reserva)) {
            reservas.add(reserva);
        }
    }

    /**
     * Indica si la habitación está libre entre dos fechas.
     *
     * Está ocupada si alguna de sus reservas NO cancelada se solapa con el
     * periodo pedido (regla de la nota del diagrama). Dos periodos se solapan
     * cuando uno empieza antes de que acabe el otro y viceversa; el día de
     * salida queda libre para una nueva entrada.
     *
     * Es un método concreto (no abstracto) porque la regla es la misma para
     * Estandar, Superior y Suite, como en el diagrama de clases.
     */
    public boolean estaDisponible(LocalDate entrada, LocalDate salida)
            throws DatosInvalidosException {
        if (entrada == null || salida == null || !salida.isAfter(entrada)) {
            throw new DatosInvalidosException("Las fechas no son válidas.");
        }

        for (Reserva reserva : reservas) {
            boolean activa = reserva.getEstado() != EstadoReserva.CANCELADA;
            boolean seSolapa = entrada.isBefore(reserva.getFechaSalida())
                    && salida.isAfter(reserva.getFechaEntrada());
            if (activa && seSolapa) {
                return false;
            }
        }
        return true;
    }

    /**
     * Parte común del texto de una habitación. Las subclases la reutilizan
     * con super.toString() y añaden sus atributos propios.
     */
    @Override
    public String toString() {
        return String.format("Hab. %d · %d persona(s) · %.2f €/noche",
                numero, capacidad, precioNoche);
    }

    /** Convierte un booleano en «sí» o «no» para mostrarlo al usuario. */
    protected static String siNo(boolean valor) {
        return valor ? "sí" : "no";
    }
}
