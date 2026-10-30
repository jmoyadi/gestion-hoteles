
package com.databuilders.hoteles.vista;

import com.databuilders.hoteles.controlador.HotelController;
import com.databuilders.hoteles.excepciones.GestionHotelesException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Scanner;

/**
 * Vista encargada de mostrar el menú y las operaciones de hoteles.
 */
public class HotelView {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private final HotelController controlador;
    private final Scanner teclado;

    public HotelView(HotelController controlador, Scanner teclado) {
        this.controlador = controlador;
        this.teclado = teclado;
    }

    public void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n----- HOTELES -----");
            System.out.println("1. Mostrar hoteles");
            System.out.println("2. Añadir hotel");
            System.out.println("3. Buscar hoteles por ciudad");
            System.out.println("4. Habitaciones disponibles hoy en un hotel");
            System.out.println("5. Habitaciones de una categoría");
            System.out.println("6. Número de habitaciones disponibles");
            System.out.println("0. Volver");

            opcion = leerEntero("Selecciona una opción: ");

            try {
                switch (opcion) {
                    case 1 -> mostrarLista(
                            "Hoteles de la cadena",
                            controlador.listarHoteles());

                    case 2 -> altaHotel();

                    case 3 -> {
                        String ciudad = leerTexto("Ciudad: ");
                        mostrarLista("Hoteles en " + ciudad,
                                controlador.buscarHotelesPorCiudad(ciudad));
                    }

                    case 4 -> {
                        int idHotel = leerEntero("ID del hotel: ");
                        mostrarLista("Habitaciones libres hoy",
                                controlador.habitacionesDisponiblesHoy(idHotel));
                    }

                    case 5 -> {
                        int idHotel = leerEntero("ID del hotel: ");
                        String categoria = leerCategoria();

                        mostrarLista("Habitaciones de categoría " + categoria,
                                controlador.habitacionesPorCategoria(idHotel, categoria));
                    }

                    case 6 -> {
                        int idHotel = leerEntero("ID del hotel: ");
                        LocalDate entrada = leerFecha(
                                "Fecha de entrada (dd/mm/aaaa): ");
                        LocalDate salida = leerFecha(
                                "Fecha de salida (dd/mm/aaaa): ");

                        int libres = controlador.contarHabitacionesDisponibles(
                                idHotel, entrada, salida);

                        System.out.println("Habitaciones disponibles: " + libres);
                    }

                    case 0 -> { }

                    default -> System.out.println("Opción no válida.");
                }

            } catch (GestionHotelesException e) {
                mostrarError(e);
            }

        } while (opcion != 0);
    }

    private void altaHotel() throws GestionHotelesException {
        int id = leerEntero("ID del hotel: ");
        String nombre = leerTexto("Nombre: ");
        String ciudad = leerTexto("Ciudad: ");

        controlador.altaHotel(id, nombre, ciudad);

        System.out.println("Hotel añadido correctamente.");
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);

            try {
                return Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Debes introducir un número entero.");
            }
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    private LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                return LocalDate.parse(leerTexto(mensaje), FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                System.out.println(
                        "Fecha no válida. Usa el formato dd/mm/aaaa.");
            }
        }
    }

    private String leerCategoria() {
        while (true) {
            System.out.println(
                    "Categoría: 1. Estándar  2. Superior  3. Suite");

            switch (leerEntero("Elige la categoría: ")) {
                case 1 -> {
                    return "estandar";
                }
                case 2 -> {
                    return "superior";
                }
                case 3 -> {
                    return "suite";
                }
                default -> System.out.println("Categoría no válida.");
            }
        }
    }

    private void mostrarLista(String titulo, List<String> elementos) {
        System.out.println("\n" + titulo + ":");

        if (elementos.isEmpty()) {
            System.out.println("  (no hay resultados)");
        } else {
            for (String elemento : elementos) {
                System.out.println("  " + elemento);
            }
        }
    }

    private void mostrarError(GestionHotelesException e) {
        System.out.println("Error: " + e.getMessage());
    }
}
