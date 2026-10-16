package com.databuilders.hoteles.modelo;

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

    public Cliente(int id, String nombre, String apellidos, String email) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser positivo.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        if (apellidos == null || apellidos.isBlank()) {
            throw new IllegalArgumentException("Los apellidos son obligatorios.");
        }
        if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("El email no es válido.");
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

    public void setNombre(String nombre) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre es obligatorio.");
        }
        this.nombre = nombre;
    }

    public void setApellidos(String apellidos) {
        if (apellidos == null || apellidos.isBlank()) {
            throw new IllegalArgumentException("Los apellidos son obligatorios.");
        }
        this.apellidos = apellidos;
    }

    public void setEmail(String email) {
        if (email == null || !email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("El email no es válido.");
        }
        this.email = email;
    }
}
