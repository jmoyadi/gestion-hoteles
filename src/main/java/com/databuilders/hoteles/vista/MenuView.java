
package com.databuilders.hoteles.vista;

import com.databuilders.hoteles.controlador.MenuController;

import java.util.Scanner;

/**
 * Vista principal de la aplicación Gestor Hotel.
 * Permite acceder a los diferentes módulos del sistema.
 */
public class MenuView {

    private final ClienteView clienteView;
    private final HotelView hotelView;
    private final HabitacionView habitacionView;
    private final ReservaView reservaView;
    private final Scanner teclado;

    public MenuView(MenuController controlador, Scanner teclado) {
        this.teclado = teclado;

        this.clienteView = new ClienteView(
                controlador.getClienteController(), teclado);

        this.hotelView = new HotelView(
                controlador.getHotelController(), teclado);

        this.habitacionView = new HabitacionView(
                controlador.getHabitacionController(), teclado);

        this.reservaView = new ReservaView(
                controlador.getReservaController(),
                controlador.getClienteController(),
                controlador.getHabitacionController(),
                teclado);
    }

    public void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n===== GESTOR HOTEL =====");
            System.out.println("1. Gestionar clientes");
            System.out.println("2. Gestionar hoteles");
            System.out.println("3. Gestionar habitaciones");
            System.out.println("4. Gestionar reservas");
            System.out.println("0. Salir");

            opcion = leerOpcion();

            switch (opcion) {
                case 1 -> clienteView.mostrarMenu();
                case 2 -> hotelView.mostrarMenu();
                case 3 -> habitacionView.mostrarMenu();
                case 4 -> reservaView.mostrarMenu();
                case 0 -> System.out.println(
                        "Saliendo del programa...");
                default -> System.out.println(
                        "Opción no válida.");
            }

        } while (opcion != 0);
    }

    private int leerOpcion() {
        while (true) {
            System.out.print("Selecciona una opción: ");

            try {
                return Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(
                        "Debes introducir un número entero.");
            }
        }
    }
}
