
package com.databuilders.hoteles.vista;

import com.databuilders.hoteles.controlador.Controlador;
import com.databuilders.hoteles.excepciones.GestionHotelesException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Vista de consola (patrón MVC).
 *
 * Es la única clase que lee del teclado y escribe en pantalla. No conoce
 * ninguna clase del modelo: pide los datos al usuario, se los pasa al
 * controlador como datos simples y muestra los textos que este le devuelve.
 *
 * Los errores de la aplicación llegan como GestionHotelesException y se
 * muestran con su mensaje; los errores de formato al teclear (un número o
 * una fecha mal escritos) se controlan aquí y se vuelve a pedir el dato.
 *
 * Submenús de clientes, hoteles y habitaciones añadidos con ayuda de IA (Claude).
 */
public class VistaConsola {

    private static final DateTimeFormatter FORMATO_FECHA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

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
            opcion = leerEntero("Selecciona una opción: ");

            switch (opcion) {
                case 1 -> menuClientes();
                case 2 -> menuHoteles();
                case 3 -> menuHabitaciones();
                case 4 -> menuReservas();
                case 0 -> System.out.println("Saliendo del programa...");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }

    // ==================== CLIENTES ====================

    private void menuClientes() {
        int opcion;
        do {
            System.out.println("\n----- CLIENTES -----");
            System.out.println("1. Listar clientes");
            System.out.println("2. Buscar cliente por nombre o apellido");
            System.out.println("3. Añadir cliente");
            System.out.println("4. Modificar cliente");
            System.out.println("5. Eliminar cliente");
            System.out.println("6. Consultar reservas de un cliente");
            System.out.println("0. Volver");
            opcion = leerEntero("Selecciona una opción: ");

            try {
                switch (opcion) {
                    case 1 -> mostrarLista("Clientes registrados", controlador.listarClientes());
                    case 2 -> {
                        String texto = leerTexto("Nombre o apellido a buscar: ");
                        mostrarLista("Clientes encontrados",
                                controlador.buscarClientesPorNombre(texto));
                    }
                    case 3 -> {
                        int id = leerEntero("ID del cliente: ");
                        String nombre = leerTexto("Nombre: ");
                        String apellidos = leerTexto("Apellidos: ");
                        String email = leerTexto("Email: ");
                        controlador.altaCliente(id, nombre, apellidos, email);
                        System.out.println("Cliente añadido correctamente.");
                    }
                    case 4 -> {
                        int id = leerEntero("ID del cliente a modificar: ");
                        String nombre = leerTexto("Nuevo nombre: ");
                        String apellidos = leerTexto("Nuevos apellidos: ");
                        String email = leerTexto("Nuevo email: ");
                        controlador.modificarCliente(id, nombre, apellidos, email);
                        System.out.println("Cliente modificado correctamente.");
                    }
                    case 5 -> {
                        int id = leerEntero("ID del cliente a eliminar: ");
                        if (leerSiNo("¿Seguro que quieres eliminarlo? (s/n): ")) {
                            controlador.eliminarCliente(id);
                            System.out.println("Cliente eliminado correctamente.");
                        } else {
                            System.out.println("Operación cancelada.");
                        }
                    }
                    case 6 -> {
                        int id = leerEntero("ID del cliente: ");
                        mostrarLista("Reservas del cliente", controlador.consultarReservasCliente(id));
                    }
                    case 0 -> { }
                    default -> System.out.println("Opción no válida.");
                }
            } catch (GestionHotelesException e) {
                mostrarError(e);
            }
        } while (opcion != 0);
    }

    // ==================== HOTELES ====================

    private void menuHoteles() {
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
                    case 1 -> mostrarLista("Hoteles de la cadena", controlador.listarHoteles());
                    case 2 -> {
                        int id = leerEntero("ID del hotel: ");
                        String nombre = leerTexto("Nombre: ");
                        String ciudad = leerTexto("Ciudad: ");
                        controlador.altaHotel(id, nombre, ciudad);
                        System.out.println("Hotel añadido correctamente.");
                    }
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
                        LocalDate entrada = leerFecha("Fecha de entrada (dd/mm/aaaa): ");
                        LocalDate salida = leerFecha("Fecha de salida (dd/mm/aaaa): ");
                        int libres = controlador.contarHabitacionesDisponibles(idHotel, entrada, salida);
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

    // ==================== RESERVAS ====================

    private void menuReservas() {
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
                            controlador.listarReservas()
                    );

                    case 2 -> {
                        int id = leerEntero("ID de la reserva: ");
                        mostrarLista(
                                "Reserva consultada",
                                controlador.buscarReservaPorId(id)
                        );
                    }

                    case 3 -> altaReserva();

                    case 4 -> {
                        int id = leerEntero("ID de la reserva que quieres cancelar: ");

                        if (leerSiNo("¿Seguro que quieres cancelar esta reserva? (s/n): ")) {
                            controlador.cancelarReserva(id);
                            System.out.println("Reserva cancelada correctamente.");
                        } else {
                            System.out.println("Operación cancelada.");
                        }
                    }

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


    /**
     * Busca reservas por un criterio (cliente, hotel, fecha de entrada,
     * fecha de salida o estado), como en el caso de uso «Buscar reservas».
     * Los resultados llegan ordenados por fecha de entrada.
     */
    private void buscarReservas() throws GestionHotelesException {
        System.out.println("Buscar por: 1. Cliente  2. Hotel  3. Fecha de entrada"
                + "  4. Fecha de salida  5. Estado");
        switch (leerEntero("Elige el criterio: ")) {
            case 1 -> {
                int idCliente = leerEntero("ID del cliente: ");
                mostrarLista("Reservas del cliente",
                        controlador.consultarReservasCliente(idCliente));
            }
            case 2 -> {
                int idHotel = leerEntero("ID del hotel: ");
                mostrarLista("Reservas del hotel",
                        controlador.buscarReservasPorHotel(idHotel));
            }
            case 3 -> {
                LocalDate fecha = leerFecha("Fecha de entrada (dd/mm/aaaa): ");
                mostrarLista("Reservas con entrada el " + fecha.format(FORMATO_FECHA),
                        controlador.buscarReservasPorFechaEntrada(fecha));
            }
            case 4 -> {
                LocalDate fecha = leerFecha("Fecha de salida (dd/mm/aaaa): ");
                mostrarLista("Reservas con salida el " + fecha.format(FORMATO_FECHA),
                        controlador.buscarReservasPorFechaSalida(fecha));
            }
            case 5 -> {
                String estado = leerEstado();
                mostrarLista("Reservas en estado " + estado,
                        controlador.buscarReservasPorEstado(estado));
            }
            default -> System.out.println("Criterio no válido.");
        }
    }

    /** Pide los datos necesarios para crear una reserva. */
    private void altaReserva() throws GestionHotelesException {
        int idReserva = leerEntero("ID de la reserva: ");
        int idCliente = leerEntero("ID del cliente: ");
        int idHotel = leerEntero("ID del hotel: ");

        LocalDate entrada = leerFecha("Fecha de entrada (dd/mm/aaaa): ");
        LocalDate salida = leerFecha("Fecha de salida (dd/mm/aaaa): ");

        int personas = leerEntero("Número de personas: ");

        List<Integer> idsHabitaciones = leerIdsHabitaciones();

        if (idsHabitaciones.isEmpty()) {
            System.out.println("Debes seleccionar al menos una habitación.");
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

    /**
     * Modifica una reserva confirmada. Primero muestra la reserva actual (si el
     * id no existe, el error sale aquí y no se pide nada más) y después pide
     * todos los datos nuevos. El cliente y el hotel no se pueden cambiar.
     */
    private void modificarReserva() throws GestionHotelesException {
        int idReserva = leerEntero("ID de la reserva que quieres modificar: ");
        mostrarLista("Reserva actual", controlador.buscarReservaPorId(idReserva));

        System.out.println("Introduce los datos nuevos (el cliente y el hotel no se pueden cambiar).");
        LocalDate entrada = leerFecha("Nueva fecha de entrada (dd/mm/aaaa): ");
        LocalDate salida = leerFecha("Nueva fecha de salida (dd/mm/aaaa): ");
        int personas = leerEntero("Número de personas: ");

        List<Integer> idsHabitaciones = leerIdsHabitaciones();

        if (idsHabitaciones.isEmpty()) {
            System.out.println("Debes seleccionar al menos una habitación.");
            return;
        }

        controlador.modificarReserva(idReserva, entrada, salida, personas, idsHabitaciones);
        System.out.println("Reserva modificada correctamente.");
    }

    /**
     * Pide los ID de las habitaciones hasta que el usuario escribe 0.
     * Lo usan el alta y la modificación de reservas.
     */
    private List<Integer> leerIdsHabitaciones() {
        List<Integer> idsHabitaciones = new ArrayList<>();

        System.out.println("Introduce los ID de las habitaciones que quieres reservar.");
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


    // ==================== HABITACIONES ====================

    private void menuHabitaciones() {
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
        String categoria = leerCategoria();
        int id = leerEntero("ID de la habitación (único en la cadena): ");
        int numero = leerEntero("Número de habitación: ");
        int capacidad = leerEntero("Capacidad (personas): ");
        double precio = leerDecimal("Precio por noche (€): ");

        switch (categoria) {
            case "estandar" -> controlador.altaHabitacionEstandar(idHotel, id, numero, capacidad,
                    precio, leerSiNo("¿Tiene escritorio? (s/n): "),
                    leerSiNo("¿Tiene minibar? (s/n): "));
            case "superior" -> controlador.altaHabitacionSuperior(idHotel, id, numero, capacidad,
                    precio, leerSiNo("¿Tiene zona de estar? (s/n): "),
                    leerSiNo("¿Tiene vistas? (s/n): "));
            default -> controlador.altaHabitacionSuite(idHotel, id, numero, capacidad,
                    precio, leerSiNo("¿Tiene salón independiente? (s/n): "),
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
                    controlador.buscarHabitacionesDisponibles(idHotel, entrada, salida, personas));
        } else {
            mostrarLista("Habitaciones libres",
                    controlador.buscarHabitacionesDisponibles(idHotel, entrada, salida));
        }
    }

    // ==================== LECTURA Y SALIDA ====================

    /** Pide un número entero hasta que el usuario escribe uno válido. */
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

    /** Pide un número decimal; acepta coma o punto como separador. */
    private double leerDecimal(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                return Double.parseDouble(teclado.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Debes introducir un número (por ejemplo 85,50).");
            }
        }
    }

    /** Pide un texto. Si está vacío lo valida el modelo y avisa con su mensaje. */
    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    /** Pide una respuesta de sí o no hasta que sea válida. */
    private boolean leerSiNo(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje).toLowerCase();
            if (respuesta.equals("s") || respuesta.equals("si") || respuesta.equals("sí")) {
                return true;
            }
            if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            }
            System.out.println("Responde s o n.");
        }
    }

    /** Pide una fecha en formato dd/mm/aaaa hasta que sea una fecha real. */
    private LocalDate leerFecha(String mensaje) {
        while (true) {
            try {
                return LocalDate.parse(leerTexto(mensaje), FORMATO_FECHA);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha no válida. Usa el formato dd/mm/aaaa, por ejemplo 15/11/2026.");
            }
        }
    }

    /** Pide la categoría de habitación y la devuelve como texto para el controlador. */
    private String leerCategoria() {
        while (true) {
            System.out.println("Categoría: 1. Estándar  2. Superior  3. Suite");
            switch (leerEntero("Elige la categoría: ")) {
                case 1 -> { return "estandar"; }
                case 2 -> { return "superior"; }
                case 3 -> { return "suite"; }
                default -> System.out.println("Categoría no válida.");
            }
        }
    }

    /** Pide el estado de una reserva y lo devuelve como texto para el controlador. */
    private String leerEstado() {
        while (true) {
            System.out.println("Estado: 1. Confirmada  2. Cancelada  3. Finalizada");
            switch (leerEntero("Elige el estado: ")) {
                case 1 -> { return "confirmada"; }
                case 2 -> { return "cancelada"; }
                case 3 -> { return "finalizada"; }
                default -> System.out.println("Estado no válido.");
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