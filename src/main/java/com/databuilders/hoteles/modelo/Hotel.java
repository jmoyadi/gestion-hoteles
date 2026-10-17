package com.databuilders.hoteles.modelo;

import com.databuilders.hoteles.excepciones.DatosInvalidosException;
import com.databuilders.hoteles.excepciones.ElementoDuplicadoException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un hotel de la cadena hotelera.
 *
 * Contiene los datos básicos para identificar
 * cada hotel.
 *
 * Atributos de relación añadidos con ayuda de IA (Claude).
 */
public class Hotel implements Identificable {

    // Atributos privados de Hotel
    private int id;
    private String nombre;
    private String ciudad;

    // Relaciones (líneas del diagrama de clases)
    // Hotel 1 ◆—tiene— 1..* Habitacion (composición): habitaciones del hotel
    private List<Habitacion> habitaciones = new ArrayList<>();

    public Hotel(int id, String nombre, String ciudad) throws DatosInvalidosException {
        if (id <= 0) {
            throw new DatosInvalidosException("El ID debe ser positivo.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre del hotel es obligatorio.");
        }
        if (ciudad == null || ciudad.isBlank()) {
            throw new DatosInvalidosException("La ciudad es obligatoria.");
        }

        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
    }

    public void agregarHabitacion(Habitacion habitacion) throws ElementoDuplicadoException {
        if (habitacion == null) {
            throw new IllegalArgumentException("La habitación no puede ser nula.");
        }

        for (Habitacion existente : habitaciones) {
            if (existente.getId() == habitacion.getId()
                    || existente.getNumero() == habitacion.getNumero()) {
                throw new ElementoDuplicadoException(
                        "Ya existe una habitación con ese ID o número en este hotel.");
            }
        }

        habitaciones.add(habitacion);
    }

    public List<Habitacion> getHabitacionesDisponibles(
            LocalDate entrada, LocalDate salida) throws DatosInvalidosException {

        validarFechas(entrada, salida);

        List<Habitacion> disponibles = new ArrayList<>();

        for (Habitacion habitacion : habitaciones) {
            if (habitacion.estaDisponible(entrada, salida)) {
                disponibles.add(habitacion);
            }
        }

        return disponibles;
    }

    public int contarHabitacionesDisponibles(
            LocalDate entrada, LocalDate salida) throws DatosInvalidosException {

        return getHabitacionesDisponibles(entrada, salida).size();
    }

    private void validarFechas(LocalDate entrada, LocalDate salida) throws DatosInvalidosException {
        if (entrada == null || salida == null || !salida.isAfter(entrada)) {
            throw new DatosInvalidosException("Las fechas no son válidas.");
        }
    }

    @Override
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public List<Habitacion> getHabitaciones() {
        return new ArrayList<>(habitaciones);
    }

    public void setNombre(String nombre) throws DatosInvalidosException {
        if (nombre == null || nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre del hotel es obligatorio.");
        }
        this.nombre = nombre;
    }

    public void setCiudad(String ciudad) throws DatosInvalidosException {
        if (ciudad == null || ciudad.isBlank()) {
            throw new DatosInvalidosException("La ciudad es obligatoria.");
        }
        this.ciudad = ciudad;
    }

    /** Texto legible para mostrar el hotel en la consola. */
    @Override
    public String toString() {
        return String.format("[%d] %s (%s) · %d habitación(es)",
                id, nombre, ciudad, habitaciones.size());
    }
}
