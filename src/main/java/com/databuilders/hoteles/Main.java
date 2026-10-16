package com.databuilders.hoteles;

import com.databuilders.hoteles.modelo.Datos;
import com.databuilders.hoteles.controlador.Controlador;
import com.databuilders.hoteles.vista.VistaConsola;

public class Main {

    public static void main(String[] args) {
        Datos datos = new Datos();
        Controlador controlador = new Controlador(datos);
        VistaConsola vista = new VistaConsola(controlador);

        vista.iniciar();
    }
}
