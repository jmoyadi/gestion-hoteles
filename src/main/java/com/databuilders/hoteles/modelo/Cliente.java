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

}
