package com.databuilders.hoteles.modelo;

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
public class Hotel {

    // Atributos privados de Hotel
    private int id;
    private String nombre;
    private String ciudad;

    // Relaciones (líneas del diagrama de clases)
    // Hotel 1 ◆—tiene— 1..* Habitacion (composición): habitaciones del hotel
    private List<Habitacion> habitaciones = new ArrayList<>();

}
