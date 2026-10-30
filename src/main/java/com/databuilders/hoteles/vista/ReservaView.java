
package com.databuilders.hoteles.vista;

import com.databuilders.hoteles.controlador.ClienteController;
import com.databuilders.hoteles.controlador.HabitacionController;
import com.databuilders.hoteles.controlador.ReservaController;
import com.databuilders.hoteles.excepciones.GestionHotelesException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Vista encargada de mostrar el menú y las operaciones de reservas.
 */
public class ReservaView {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu")
                    .withResolverStyle(ResolverStyle.STRICT);

    private final ReservaController controlador;
    private final ClienteController clienteController;
    private final HabitacionController habitacionController;
    private final Scanner teclado;

    public ReservaView(
            ReservaController controlador,
            ClienteController clienteController,
            HabitacionController habitacionController,
            Scanner teclado) {

        this.controlador = controlador;
        this.clienteController = clienteController;
        this.habitacionController = habitacionController;
        this.teclado = teclado;
    }

    public void mostrarMenu() {
        int opcion;

        do {
            System.out.println("\n----- RESERVAS -----");
            System.out.println("1. Listar reservas");
            System.out.println("2. Consultar reserva por ID");
            System.out.println("3. Crear reserva");
            System.out.println("4. Cancelar reserva");
            System.out.println("5. Buscar reservas");
            System.out.println("6. Modificar reserva");
            System.out.println("0. Volver");

            opcion = leerEntero("Selecciona una opción: ");

            try {
                switch (opcion) {
                    case 1 -> mostrarLista(
                            "Reservas registradas",
                            controlador.listarReservas());

                    case 2 -> {
                        int id = leerEntero("ID de la reserva: ");
                        mostrarLista(
                                "Reserva consultada",
                                controlador.buscarReservaPorId(id));
                    }

                    case 3 -> altaReserva();

                    case 4 -> cancelarReserva();

                    case 5 -> buscarReservas();

                    case 6 -> modificarReserva();

                    case 0 -> { }

                    default -> System.out.println("Opción no válida.");
                }

            } catch (GestionHotelesException e) {
                mostrarError(e);
            }

        } while (opcion != 0);
    }

    private void altaReserva() throws GestionHotelesException {
        int idReserva = leerEntero("ID de la reserva: ");
        int idCliente = leerEntero("ID del cliente: ");
        int idHotel = leerEntero("ID del hotel: ");

        LocalDate entrada = leerFecha(
                "Fecha de entrada (dd/mm/aaaa): ");
        LocalDate salida = leerFecha(
                "Fecha de salida (dd/mm/aaaa): ");

        List<String> disponibles =
                habitacionController.buscarHabitacionesDisponibles(
                        idHotel, entrada, salida);

        if (disponibles.isEmpty()) {
            System.out.println(
                    "No hay habitaciones disponibles en esas fechas.");
            return;
        }

        mostrarLista("Habitaciones disponibles en esas fechas", disponibles);

        int personas = leerEntero("Número de personas: ");

        List<Integer> idsHabitaciones = leerIdsHabitaciones();

        if (idsHabitaciones.isEmpty()) {
            System.out.println(
                    "Debes seleccionar al menos una habitación.");
            return;
        }

        controlador.altaReserva(
                idReserva,
                idCliente,
                idHotel,
                entrada,
                salida,
                personas,
                idsHabitaciones
        );

        System.out.println("Reserva creada correctamente.");
    }


    private void cancelarReserva() throws GestionHotelesException {
        int id = leerEntero(
                "ID de la reserva que quieres cancelar: ");

        mostrarLista(
                "Reserva a cancelar",
                controlador.buscarReservaPorId(id));

        if (leerSiNo()) {
            controlador.cancelarReserva(id);
            System.out.println("Reserva cancelada correctamente.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }


    private void buscarReservas() throws GestionHotelesException {
        System.out.println(
                "Buscar por: 1. Cliente  2. Hotel  3. Fecha de entrada"
                        + "  4. Fecha de salida  5. Estado");

        switch (leerEntero("Elige el criterio: ")) {

            case 1 -> {
                int idCliente = leerEntero("ID del cliente: ");

                mostrarLista(
                        "Reservas del cliente",
                        clienteController.consultarReservasCliente(idCliente));
            }

            case 2 -> {
                int idHotel = leerEntero("ID del hotel: ");

                mostrarLista(
                        "Reservas del hotel",
                        controlador.buscarReservasPorHotel(idHotel));
            }

            case 3 -> {
                LocalDate fecha = leerFecha(
                        "Fecha de entrada (dd/mm/aaaa): ");

                mostrarLista(
                        "Reservas con entrada el "
                                + fecha.format(FORMATO_FECHA),
                        controlador.buscarReservasPorFechaEntrada(fecha));
            }

            case 4 -> {
                LocalDate fecha = leerFecha(
                        "Fecha de salida (dd/mm/aaaa): ");

                mostrarLista(
                        "Reservas con salida el "
                                + fecha.format(FORMATO_FECHA),
                        controlador.buscarReservasPorFechaSalida(fecha));
            }

            case 5 -> {
                String estado = leerEstado();

                mostrarLista(
                        "Reservas en estado " + estado,
                        controlador.buscarReservasPorEstado(estado));
            }

            default -> System.out.println("Criterio no válido.");
        }
    }

    private void modificarReserva() throws GestionHotelesException {
        int idReserva = leerEntero(
                "ID de la reserva que quieres modificar: ");

        mostrarLista(
                "Reserva actual",
                controlador.buscarReservaPorId(idReserva));

        System.out.println(
                "Introduce los datos nuevos "
                        + "(el cliente y el hotel no se pueden cambiar).");

        LocalDate entrada = leerFecha(
                "Nueva fecha de entrada (dd/mm/aaaa): ");

        LocalDate salida = leerFecha(
                "Nueva fecha de salida (dd/mm/aaaa): ");

        int personas = leerEntero("Número de personas: ");

        List<Integer> idsHabitaciones = leerIdsHabitaciones();

        if (idsHabitaciones.isEmpty()) {
            System.out.println(
                    "Debes seleccionar al menos una habitación.");
            return;
        }

        controlador.modificarReserva(
                idReserva,
                entrada,
                salida,
                personas,
                idsHabitaciones);

        System.out.println("Reserva modificada correctamente.");
    }

    private List<Integer> leerIdsHabitaciones() {
        List<Integer> idsHabitaciones = new ArrayList<>();

        System.out.println(
                "Introduce los ID de las habitaciones que quieres reservar.");
        System.out.println("Escribe 0 cuando hayas terminado.");

        while (true) {
            int idHabitacion = leerEntero("ID de habitación: ");

            if (idHabitacion == 0) {
                break;
            }

            idsHabitaciones.add(idHabitacion);
        }

        return idsHabitaciones;
    }

    private int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);

            try {
                return Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(
                        "Debes introducir un número entero.");
            }
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }


    private boolean leerSiNo() {
        while (true) {
            String respuesta = leerTexto(
                    "¿Seguro que quieres cancelar esta reserva? (s/n): "
            ).toLowerCase();

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
                return LocalDate.parse(
                        leerTexto(mensaje), FORMATO_FECHA);

            } catch (DateTimeParseException e) {
                System.out.println(
                        "Fecha no válida. Usa el formato dd/mm/aaaa.");
            }
        }
    }

    private String leerEstado() {
        while (true) {
            System.out.println(
                    "Estados: confirmada, cancelada o finalizada");

            String estado = leerTexto("Introduce el estado: ")
                    .toLowerCase();

            if (estado.equals("confirmada")
                    || estado.equals("cancelada")
                    || estado.equals("finalizada")) {
                return estado;
            }

            System.out.println("Estado no válido.");
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
