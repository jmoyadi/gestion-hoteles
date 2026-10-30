package com.databuilders.hoteles;

import com.databuilders.hoteles.modelo.Datos;
import com.databuilders.hoteles.controlador.MenuController;
import com.databuilders.hoteles.vista.MenuView;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Datos datos = new Datos();

        MenuController controlador = new MenuController(datos);

        Scanner teclado = new Scanner(System.in);
        MenuView vista = new MenuView(controlador, teclado);

        vista.mostrarMenu();
    }
}
