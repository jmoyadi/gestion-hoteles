package com.databuilders.hoteles.modelo;

import com.databuilders.hoteles.excepciones.DatosInvalidosException;

/**
 * Representa una habitación estándar.
 *
 * HEREDA de la clase Habitacion los atributos comunes
 * a todos los tipos de habitación.
 */
public class Estandar extends Habitacion {

    // Atributos propios de una habitación estándar
    private boolean tieneEscritorio;
    private boolean tieneMinibar;

    public Estandar(int id, int numero, int capacidad, double precioNoche,
                    boolean tieneEscritorio, boolean tieneMinibar)
            throws DatosInvalidosException {
        super(id, numero, capacidad, precioNoche);
        this.tieneEscritorio = tieneEscritorio;
        this.tieneMinibar = tieneMinibar;
    }

    public boolean isTieneEscritorio() {
        return tieneEscritorio;
    }

    public void setTieneEscritorio(boolean tieneEscritorio) {
        this.tieneEscritorio = tieneEscritorio;
    }

    public boolean isTieneMinibar() {
        return tieneMinibar;
    }

    public void setTieneMinibar(boolean tieneMinibar) {
        this.tieneMinibar = tieneMinibar;
    }

    @Override
    public String toString() {
        return "Estándar · " + super.toString()
                + " · escritorio: " + siNo(tieneEscritorio)
                + " · minibar: " + siNo(tieneMinibar);
    }
}
