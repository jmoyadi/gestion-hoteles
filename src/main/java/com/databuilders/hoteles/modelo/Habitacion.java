package com.databuilders.hoteles.modelo;

import java.time.LocalDate;

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
 */
public abstract class Habitacion {

    // Atributos privados comunes a todas las habitaciones
    private int id;
    private int numero;
    private int capacidad;
    private double precioNoche;

    public Habitacion(int id, int numero, int capacidad, double precioNoche) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser positivo.");
        }
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de habitación debe ser positivo.");
        }
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }
        if (precioNoche < 0) {
            throw new IllegalArgumentException("El precio por noche no puede ser negativo.");
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

    public void setNumero(int numero) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de habitación debe ser positivo.");
        }
        this.numero = numero;
    }

    public void setCapacidad(int capacidad) {
        if (capacidad <= 0) {
            throw new IllegalArgumentException("La capacidad debe ser mayor que cero.");
        }
        this.capacidad = capacidad;
    }

    public void setPrecioNoche(double precioNoche) {
        if (precioNoche < 0) {
            throw new IllegalArgumentException("El precio por noche no puede ser negativo.");
        }
        this.precioNoche = precioNoche;
    }

    public abstract boolean estaDisponible(LocalDate entrada, LocalDate salida);
}
