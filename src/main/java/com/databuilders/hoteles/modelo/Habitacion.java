package com.databuilders.hoteles.modelo;

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

}
