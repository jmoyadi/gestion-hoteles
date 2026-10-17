package com.databuilders.hoteles.modelo;

import com.databuilders.hoteles.excepciones.DatosInvalidosException;

/**
 * Representa una habitación suite.
 *
 * HEREDA de la clase Habitacion los atributos comunes
 * a todos los tipos de habitación.
 */
public class Suite extends Habitacion {

    // Atributos propios de una habitación suite
    private boolean tieneSalonIndependiente;
    private int numeroBanos;

    public Suite(int id, int numero, int capacidad, double precioNoche,
                 boolean tieneSalonIndependiente, int numeroBanos)
            throws DatosInvalidosException {
        super(id, numero, capacidad, precioNoche);

        if (numeroBanos <= 0) {
            throw new DatosInvalidosException("El número de baños debe ser positivo.");
        }

        this.tieneSalonIndependiente = tieneSalonIndependiente;
        this.numeroBanos = numeroBanos;
    }

    public boolean isTieneSalonIndependiente() {
        return tieneSalonIndependiente;
    }

    public void setTieneSalonIndependiente(boolean tieneSalonIndependiente) {
        this.tieneSalonIndependiente = tieneSalonIndependiente;
    }

    public int getNumeroBanos() {
        return numeroBanos;
    }

    public void setNumeroBanos(int numeroBanos) throws DatosInvalidosException {
        if (numeroBanos <= 0) {
            throw new DatosInvalidosException("El número de baños debe ser positivo.");
        }
        this.numeroBanos = numeroBanos;
    }

    @Override
    public String toString() {
        return "Suite · " + super.toString()
                + " · salón independiente: " + siNo(tieneSalonIndependiente)
                + " · baños: " + numeroBanos;
    }
}
