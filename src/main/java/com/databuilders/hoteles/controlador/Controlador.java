package com.databuilders.hoteles.controlador;

import com.databuilders.hoteles.excepciones.DatosInvalidosException;
import com.databuilders.hoteles.excepciones.GestionHotelesException;
import com.databuilders.hoteles.modelo.Cliente;
import com.databuilders.hoteles.modelo.Datos;
import com.databuilders.hoteles.modelo.EstadoReserva;
import com.databuilders.hoteles.modelo.Estandar;
import com.databuilders.hoteles.modelo.Habitacion;
import com.databuilders.hoteles.modelo.Hotel;
import com.databuilders.hoteles.modelo.Suite;
import com.databuilders.hoteles.modelo.Superior;
import com.databuilders.hoteles.modelo.Reserva;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador de la aplicación (patrón MVC).
 *
 * Es el único puente entre la vista y el modelo: la vista le pasa datos simples
 * (números y textos) y recibe textos listos para mostrar. Así la vista no
 * conoce ninguna clase del modelo.
 *
 * Los errores llegan a la vista como GestionHotelesException (paquete
 * excepciones, fuera del modelo): la vista los captura y muestra el mensaje.
 *
 * Las fechas se reciben como LocalDate (java.time, no es una clase del modelo):
 * la vista se encarga de leerlas y comprobar su formato.
 *
 * Operaciones del controlador añadidas con ayuda de IA (Claude).
 */
public class Controlador {

    private final Datos datos;

    public Controlador(Datos datos) {
        if (datos == null) {
            throw new IllegalArgumentException("Los datos no pueden ser nulos.");
        }
        this.datos = datos;
    }

    // ---------- Clientes ----------

    public void altaCliente(int id, String nombre, String apellidos, String email)
            throws GestionHotelesException {
        datos.agregarCliente(new Cliente(id, nombre, apellidos, email));
    }

    public List<String> listarClientes() {
        return aTexto(datos.listarClientes());
    }

    public List<String> buscarClientesPorNombre(String texto) {
        return aTexto(datos.buscarClientesPorNombre(texto));
    }

    public List<String> consultarReservasCliente(int idCliente)
            throws GestionHotelesException {
        return aTexto(datos.consultarReservasCliente(idCliente));
    }

    public void modificarCliente(int id, String nombre, String apellidos, String email)
            throws GestionHotelesException {
        datos.modificarCliente(id, nombre, apellidos, email);
    }

    public void eliminarCliente(int id) throws GestionHotelesException {
        datos.eliminarCliente(id);
    }

    // ---------- Hoteles ----------

    public void altaHotel(int id, String nombre, String ciudad)
            throws GestionHotelesException {
        datos.agregarHotel(new Hotel(id, nombre, ciudad));
    }

    public List<String> listarHoteles() {
        return aTexto(datos.listarHoteles());
    }

    public List<String> buscarHotelesPorCiudad(String ciudad) {
        return aTexto(datos.buscarHotelesPorCiudad(ciudad));
    }

    /** Habitaciones del hotel libres hoy (para la noche de hoy a mañana). */
    public List<String> habitacionesDisponiblesHoy(int idHotel)
            throws GestionHotelesException {
        LocalDate hoy = LocalDate.now();
        return aTexto(datos.buscarHabitacionesDisponibles(idHotel, hoy, hoy.plusDays(1)));
    }

    /**
     * Habitaciones de una categoría. La vista envía el nombre de la categoría
     * ("estandar", "superior" o "suite") y aquí se traduce a la clase hija,
     * así la vista no necesita conocer las clases del modelo.
     */
    public List<String> habitacionesPorCategoria(int idHotel, String categoria)
            throws GestionHotelesException {
        return aTexto(datos.listarHabitacionesPorCategoria(idHotel, claseDeCategoria(categoria)));
    }

    public int contarHabitacionesDisponibles(int idHotel, LocalDate entrada, LocalDate salida)
            throws GestionHotelesException {
        return datos.contarHabitacionesDisponibles(idHotel, entrada, salida);
    }

    // ---------- Habitaciones ----------

    public void altaHabitacionEstandar(int idHotel, int id, int numero, int capacidad,
            double precioNoche, boolean tieneEscritorio, boolean tieneMinibar)
            throws GestionHotelesException {
        datos.agregarHabitacion(idHotel,
                new Estandar(id, numero, capacidad, precioNoche, tieneEscritorio, tieneMinibar));
    }

    public void altaHabitacionSuperior(int idHotel, int id, int numero, int capacidad,
            double precioNoche, boolean tieneZonaEstar, boolean tieneVistas)
            throws GestionHotelesException {
        datos.agregarHabitacion(idHotel,
                new Superior(id, numero, capacidad, precioNoche, tieneZonaEstar, tieneVistas));
    }

    public void altaHabitacionSuite(int idHotel, int id, int numero, int capacidad,
            double precioNoche, boolean tieneSalonIndependiente, int numeroBanos)
            throws GestionHotelesException {
        datos.agregarHabitacion(idHotel,
                new Suite(id, numero, capacidad, precioNoche, tieneSalonIndependiente, numeroBanos));
    }

    public List<String> listarHabitacionesHotel(int idHotel) throws GestionHotelesException {
        return aTexto(datos.listarHabitacionesHotel(idHotel));
    }

    public List<String> buscarHabitacionesDisponibles(int idHotel, LocalDate entrada,
            LocalDate salida) throws GestionHotelesException {
        return aTexto(datos.buscarHabitacionesDisponibles(idHotel, entrada, salida));
    }

    public List<String> buscarHabitacionesDisponibles(int idHotel, LocalDate entrada,
            LocalDate salida, int personas) throws GestionHotelesException {
        return aTexto(datos.buscarHabitacionesDisponibles(idHotel, entrada, salida, personas));
    }

    // ---------- Utilidad ----------

    /** Traduce el nombre de una categoría a su clase hija de Habitacion. */
    private Class<? extends Habitacion> claseDeCategoria(String categoria)
            throws DatosInvalidosException {
        if (categoria == null) {
            throw new DatosInvalidosException("La categoría es obligatoria.");
        }
        return switch (categoria.trim().toLowerCase()) {
            case "estandar" -> Estandar.class;
            case "superior" -> Superior.class;
            case "suite" -> Suite.class;
            default -> throw new DatosInvalidosException(
                    "Categoría no válida: usa estándar, superior o suite.");
        };
    }

    /** Convierte una lista de objetos del modelo en textos para la vista. */
    private List<String> aTexto(List<?> elementos) {
        List<String> textos = new ArrayList<>();
        for (Object elemento : elementos) {
            textos.add(elemento.toString());
        }
        return textos;
    }

    // ==================== RESERVAS ====================

    /** Devuelve todas las reservas registradas. */
    public List<String> listarReservas() {
        return aTexto(datos.listarReservas());
    }

    /** Busca una reserva por su identificador. */
    public List<String> buscarReservaPorId(int idReserva)
            throws GestionHotelesException {
        return aTexto(List.of(datos.buscarReservaPorId(idReserva)));
    }

    /** Crea y registra una reserva. */
    public void altaReserva(
            int idReserva,
            int idCliente,
            int idHotel,
            LocalDate fechaEntrada,
            LocalDate fechaSalida,
            int numPersonas,
            List<Integer> idsHabitaciones
    ) throws GestionHotelesException {

        Cliente cliente = datos.buscarClientePorId(idCliente);
        Hotel hotel = datos.buscarHotelPorId(idHotel);

        List<Habitacion> habitacionesHotel = hotel.getHabitaciones();
        List<Habitacion> habitacionesReserva = new ArrayList<>();

        for (int idHabitacion : idsHabitaciones) {
            Habitacion encontrada = null;

            for (Habitacion habitacion : habitacionesHotel) {
                if (habitacion.getId() == idHabitacion) {
                    encontrada = habitacion;
                    break;
                }
            }

            if (encontrada == null) {
                throw new DatosInvalidosException(
                        "La habitación con ID " + idHabitacion
                                + " no pertenece al hotel seleccionado."
                );
            }

            habitacionesReserva.add(encontrada);
        }

        Reserva reserva = new Reserva(
                idReserva,
                fechaEntrada,
                fechaSalida,
                numPersonas,
                cliente,
                hotel,
                habitacionesReserva
        );

        datos.agregarReserva(reserva);
    }

    /**
     * Cancela una reserva. La operación la hace Datos, como el resto de
     * cambios en el modelo: el controlador solo pasa el id.
     */
    public void cancelarReserva(int idReserva)
            throws GestionHotelesException {
        datos.cancelarReserva(idReserva);
    }

    // Búsqueda de reservas por criterio (caso de uso «Buscar reservas»).
    // La búsqueda por cliente es consultarReservasCliente(), en Clientes.

    public List<String> buscarReservasPorHotel(int idHotel)
            throws GestionHotelesException {
        return aTexto(datos.buscarReservasPorHotel(idHotel));
    }

    public List<String> buscarReservasPorFechaEntrada(LocalDate fecha) {
        return aTexto(datos.buscarReservasPorFechaEntrada(fecha));
    }

    public List<String> buscarReservasPorFechaSalida(LocalDate fecha) {
        return aTexto(datos.buscarReservasPorFechaSalida(fecha));
    }

    /**
     * Reservas en un estado. La vista envía el estado como texto
     * ("confirmada", "cancelada" o "finalizada") y aquí se traduce al
     * enumerado, así la vista no necesita conocer las clases del modelo.
     */
    public List<String> buscarReservasPorEstado(String estado)
            throws GestionHotelesException {
        return aTexto(datos.buscarReservasPorEstado(estadoDeTexto(estado)));
    }

    /** Traduce el nombre de un estado a su valor de EstadoReserva. */
    private EstadoReserva estadoDeTexto(String estado) throws DatosInvalidosException {
        if (estado == null) {
            throw new DatosInvalidosException("El estado es obligatorio.");
        }
        return switch (estado.trim().toLowerCase()) {
            case "confirmada" -> EstadoReserva.CONFIRMADA;
            case "cancelada" -> EstadoReserva.CANCELADA;
            case "finalizada" -> EstadoReserva.FINALIZADA;
            default -> throw new DatosInvalidosException(
                    "Estado no válido: usa confirmada, cancelada o finalizada.");
        };
    }

}