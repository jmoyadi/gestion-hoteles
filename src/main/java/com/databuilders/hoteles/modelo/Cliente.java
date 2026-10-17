package com.databuilders.hoteles.modelo;

import com.databuilders.hoteles.excepciones.DatosInvalidosException;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un cliente de la cadena hotelera.
 *
 * Contiene los datos básicos necesarios para identificar a cada cliente.
 *
 * Atributos de relación añadidos con ayuda de IA (Claude).
 */
public class Cliente {

    // Atributos privados de Cliente
    private int id;
    private String nombre;
    private String apellidos;
    private String email;

    // Relaciones (líneas del diagrama de clases)
    // Cliente 1 —realiza— 0..* Reserva: reservas hechas por este cliente
    private List<Reserva> reservas = new ArrayList<>();

    public Cliente(int id, String nombre, String apellidos, String email)
            throws DatosInvalidosException {
        if (id <= 0) {
            throw new DatosInvalidosException("El ID debe ser positivo.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre es obligatorio.");
        }
        if (apellidos == null || apellidos.isBlank()) {
            throw new DatosInvalidosException("Los apellidos son obligatorios.");
        }
        if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new DatosInvalidosException("El email no es válido.");
        }

        this.id = id;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
    }

    public void agregarReserva(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no puede ser nula.");
        }
        if (!reservas.contains(reserva)) {
            reservas.add(reserva);
        }
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getEmail() {
        return email;
    }

    public List<Reserva> getReservas() {
        return new ArrayList<>(reservas);
    }

    public void setNombre(String nombre) throws DatosInvalidosException {
        if (nombre == null || nombre.isBlank()) {
            throw new DatosInvalidosException("El nombre es obligatorio.");
        }
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) throws DatosInvalidosException {
        if (apellidos == null || apellidos.isBlank()) {
            throw new DatosInvalidosException("Los apellidos son obligatorios.");
        }
        this.apellidos = apellidos;
    }

    public void setEmail(String email) throws DatosInvalidosException {
        if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new DatosInvalidosException("El email no es válido.");
        }
        this.email = email;
    }

    /** Texto legible para mostrar el cliente en la consola. */
    @Override
    public String toString() {
        return String.format("[%d] %s %s · %s · %d reserva(s)",
                id, nombre, apellidos, email, reservas.size());
    }
}
