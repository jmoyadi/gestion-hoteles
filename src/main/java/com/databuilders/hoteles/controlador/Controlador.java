
package com.databuilders.hoteles.controlador;

import com.databuilders.hoteles.modelo.Datos;

public class Controlador {

    private final Datos datos;

    public Controlador(Datos datos) {
        if (datos == null) {
            throw new IllegalArgumentException("Los datos no pueden ser nulos.");
        }
        this.datos = datos;
    }

    public Datos getDatos() {
        return datos;
    }
}

