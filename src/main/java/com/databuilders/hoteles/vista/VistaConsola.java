
package com.databuilders.hoteles.vista;

import com.databuilders.hoteles.controlador.Controlador;

import java.util.Scanner;

public class VistaConsola {

    private final Controlador controlador;
    private final Scanner teclado;

    public VistaConsola(Controlador controlador) {
        this.controlador = controlador;
        this.teclado = new Scanner(System.in);
    }

    public void iniciar() {
        int opcion;

        do {
            System.out.println("\n===== GESTOR HOTEL =====");
            System.out.println("1. Gestionar clientes");
            System.out.println("2. Gestionar hoteles");
            System.out.println("3. Gestionar habitaciones");
            System.out.println("4. Gestionar reservas");
            System.out.println("0. Salir");
            System.out.print("Selecciona una opción: ");

            try {
                opcion = Integer.parseInt(teclado.nextLine());

                switch (opcion) {
                    case 1 -> System.out.println("Gestión de clientes");
                    case 2 -> System.out.println("Gestión de hoteles");
                    case 3 -> System.out.println("Gestión de habitaciones");
                    case 4 -> System.out.println("Gestión de reservas");
                    case 0 -> System.out.println("Saliendo del programa...");
                    default -> System.out.println("Opción no válida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Debes introducir un número.");
                opcion = -1;
            }

        } while (opcion != 0);
    }
}
