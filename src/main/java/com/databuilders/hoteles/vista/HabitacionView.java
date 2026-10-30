
package com.databuilders.hoteles.vista;

import com.databuilders.hoteles.controlador.HabitacionController;
import com.databuilders.hoteles.excepciones.GestionHotelesException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.List;
import java.util.Scanner;

/**
 * Vista encargada de mostrar el menú y las operaciones de habitaciones.
 */
public class HabitacionView {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private final HabitacionController controlador;
    private final Scanner teclado;

    public HabitacionView(HabitacionController controlador, Scanner teclado) {
        this.controlador = controlador;
        this.teclado = teclado;
    }

    public void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n----- HABITACIONES -----");
            System.out.println("1. Añadir habitación a un hotel");
            System.out.println("2. Listar habitaciones de un hotel");
            System.out.println("3. Buscar habitaciones disponibles por fechas");
            System.out.println("0. Volver");

            opcion = leerEntero("Selecciona una opción: ");

            try {
                switch (opcion) {
                    case 1 -> altaHabitacion();

                    case 2 -> {
                        int idHotel = leerEntero("ID del hotel: ");
                        mostrarLista("Habitaciones del hotel",
                                controlador.listarHabitacionesHotel(idHotel));
                    }

                    case 3 -> buscarDisponibles();

                    case 0 -> { }

                    default -> System.out.println("Opción no válida.");
                }

            } catch (GestionHotelesException e) {
                mostrarError(e);
            }

        } while (opcion != 0);
    }

    private void altaHabitacion() throws GestionHotelesException {
        int idHotel = leerEntero("ID del hotel: ");

        mostrarLista("Habitaciones actuales del hotel",
                controlador.listarHabitacionesHotel(idHotel));

        String categoria = leerCategoria();

        int id = leerEntero("ID de la habitación (único en la cadena): ");
        int numero = leerEntero("Número de habitación: ");
        int capacidad = leerEntero("Capacidad (personas): ");
        double precio = leerDecimal("Precio por noche (€): ");

        switch (categoria) {
            case "estandar" -> controlador.altaHabitacionEstandar(
                    idHotel, id, numero, capacidad, precio,
                    leerSiNo("¿Tiene escritorio? (s/n): "),
                    leerSiNo("¿Tiene minibar? (s/n): "));

            case "superior" -> controlador.altaHabitacionSuperior(
                    idHotel, id, numero, capacidad, precio,
                    leerSiNo("¿Tiene zona de estar? (s/n): "),
                    leerSiNo("¿Tiene vistas? (s/n): "));

            case "suite" -> controlador.altaHabitacionSuite(
                    idHotel, id, numero, capacidad, precio,
                    leerSiNo("¿Tiene salón independiente? (s/n): "),
                    leerEntero("Número de baños: "));
        }

        System.out.println("Habitación añadida correctamente.");
    }

    private void buscarDisponibles() throws GestionHotelesException {
        int idHotel = leerEntero("ID del hotel: ");

        LocalDate entrada = leerFecha("Fecha de entrada (dd/mm/aaaa): ");
        LocalDate salida = leerFecha("Fecha de salida (dd/mm/aaaa): ");

        if (leerSiNo("¿Filtrar por número de personas? (s/n): ")) {
            int personas = leerEntero("Número de personas: ");

            mostrarLista("Habitaciones libres para " + personas + " persona(s)",
                    controlador.buscarHabitacionesDisponibles(
                            idHotel, entrada, salida, personas));

        } else {
            mostrarLista("Habitaciones libres",
                    controlador.buscarHabitacionesDisponibles(
                            idHotel, entrada, salida));
        }
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

    private double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);

            try {
                return Double.parseDouble(
                        teclado.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println(
                        "Debes introducir un número (por ejemplo 85,50).");
            }
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    private boolean leerSiNo(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje).toLowerCase();

            if (respuesta.equals("s") || respuesta.equals("si")
                    || respuesta.equals("sí")) {
                return true;
            }

            if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            }

            System.out.println("Responde s o n.");
        }
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
