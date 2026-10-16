package com.databuilders.hoteles.modelo;

import java.time.LocalDate;

/**
 * Representa una habitación superior.
 *
 * HEREDA de la clase Habitacion los atributos comunes
 * a todos los tipos de habitación.
 */
public class Superior extends Habitacion {

    // Atributos propios de una habitación superior
    private boolean tieneZonaEstar;
    private boolean tieneVistas;

    public Superior(int id, int numero, int capacidad, double precioNoche,
                    boolean tieneZonaEstar, boolean tieneVistas) {
        super(id, numero, capacidad, precioNoche);
        this.tieneZonaEstar = tieneZonaEstar;
        this.tieneVistas = tieneVistas;
    }

    public boolean isTieneZonaEstar() {
        return tieneZonaEstar;
    }

    public void setTieneZonaEstar(boolean tieneZonaEstar) {
        this.tieneZonaEstar = tieneZonaEstar;
    }

    public boolean isTieneVistas() {
        return tieneVistas;
    }

    public void setTieneVistas(boolean tieneVistas) {
        this.tieneVistas = tieneVistas;
    }

    @Override
    public boolean estaDisponible(LocalDate entrada, LocalDate salida) {
        return true;
    }
}
