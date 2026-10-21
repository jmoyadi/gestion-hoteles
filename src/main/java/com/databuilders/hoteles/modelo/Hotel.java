package com.databuilders.hoteles.modelo;

import com.databuilders.hoteles.excepciones.DatosInvalidosException;
import com.databuilders.hoteles.excepciones.ElementoDuplicadoException;
import com.databuilders.hoteles.excepciones.ElementoNoEncontradoException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa un hotel de la cadena hotelera.
 *
 * Contiene los datos básicos para identificar
 * cada hotel.
 *
 * Atributos de relación y uso de Repositorio para las habitaciones
 * añadidos con ayuda de IA (Claude).
 */
public class Hotel implements Identificable {

    // Atributos privados de Hotel
    private int id;
    private String nombre;
    private String ciudad;

    // Relaciones (líneas del diagrama de clases)
    // Hotel 1 ◆—tiene— 1..* Habitacion (composición): habitaciones del hotel
    // Se guardan en un Repositorio (LinkedHashMap por id), igual que en Datos
    private final Repositorio<Habitacion> habitaciones = new Repositorio<>("una habitación");

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

    /**
     * Añade una habitación a este hotel.
     * Visibilidad de paquete: solo la llama Datos.agregarHabitacion, que antes
     * comprueba que el id no se use en ningún otro hotel de la cadena.
     * Aquí se comprueba que el número de puerta no se repita en este hotel
     * (el mismo número sí puede existir en otro hotel).
     */
    void agregarHabitacion(Habitacion habitacion) throws ElementoDuplicadoException {
        if (habitacion == null) {
            throw new IllegalArgumentException("La habitación no puede ser nula.");
        }

        for (Habitacion existente : habitaciones.listar()) {
            if (existente.getNumero() == habitacion.getNumero()) {
                throw new ElementoDuplicadoException("Ya existe la habitación número "
                        + habitacion.getNumero() + " en el hotel " + nombre + ".");
            }
        }

        // El Repositorio rechaza además un id repetido dentro del hotel
        habitaciones.agregar(habitacion);
    }

    /** Indica si este hotel tiene una habitación con ese id (lo usa Datos). */
    boolean tieneHabitacion(int idHabitacion) {
        return habitaciones.existe(idHabitacion);
    }

    /**
     * Devuelve la habitación de este hotel con ese id, o lanza una excepción
     * si no es de este hotel. Visibilidad de paquete: la usa Datos.
     */
    Habitacion buscarHabitacion(int idHabitacion) throws ElementoNoEncontradoException {
        if (!habitaciones.existe(idHabitacion)) {
            throw new ElementoNoEncontradoException("La habitación con ID " + idHabitacion
                    + " no pertenece al hotel " + nombre + ".");
        }
        return habitaciones.buscarPorId(idHabitacion);
    }

    public List<Habitacion> getHabitacionesDisponibles(
            LocalDate entrada, LocalDate salida) throws DatosInvalidosException {

        validarFechas(entrada, salida);

        List<Habitacion> disponibles = new ArrayList<>();

        for (Habitacion habitacion : habitaciones.listar()) {
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

    /** Habitaciones del hotel en orden de alta (lista no modificable). */
    public List<Habitacion> getHabitaciones() {
        return habitaciones.listar();
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
                id, nombre, ciudad, habitaciones.contar());
    }
}
