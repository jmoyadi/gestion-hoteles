
package com.databuilders.hoteles.modelo;

import com.databuilders.hoteles.excepciones.DatosInvalidosException;
import com.databuilders.hoteles.excepciones.ElementoDuplicadoException;
import com.databuilders.hoteles.excepciones.ElementoNoEncontradoException;
import com.databuilders.hoteles.excepciones.HabitacionNoDisponibleException;

import java.util.ArrayList;
import java.util.List;

/**
 * Contiene todos los datos de la aplicación y las operaciones sobre ellos (MVC).
 *
 * Gestión de excepciones propias añadida con ayuda de IA (Claude).
 */
public class Datos {

    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Hotel> hoteles = new ArrayList<>();
    private final List<Reserva> reservas = new ArrayList<>();

    public void agregarCliente(Cliente cliente) throws ElementoDuplicadoException {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }

        if (encontrarCliente(cliente.getId()) != null) {
            throw new ElementoDuplicadoException(
                    "Ya existe un cliente con ese ID.");
        }

        clientes.add(cliente);
    }

    public void agregarHotel(Hotel hotel) throws ElementoDuplicadoException {
        if (hotel == null) {
            throw new IllegalArgumentException("El hotel no puede ser nulo.");
        }

        if (encontrarHotel(hotel.getId()) != null) {
            throw new ElementoDuplicadoException(
                    "Ya existe un hotel con ese ID.");
        }

        hoteles.add(hotel);
    }

    /**
     * Registra una reserva después de comprobar las reglas que dependen del
     * estado del sistema. Todo se comprueba ANTES de registrar nada, para que
     * un error no deje la reserva registrada a medias.
     */
    public void agregarReserva(Reserva reserva) throws ElementoDuplicadoException,
            ElementoNoEncontradoException, HabitacionNoDisponibleException,
            DatosInvalidosException {
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no puede ser nula.");
        }

        if (encontrarReserva(reserva.getId()) != null) {
            throw new ElementoDuplicadoException(
                    "Ya existe una reserva con ese ID.");
        }

        // El cliente y el hotel tienen que ser los mismos objetos dados de alta
        // (se compara con != para exigir el mismo objeto, no solo el mismo id)
        if (encontrarCliente(reserva.getCliente().getId()) != reserva.getCliente()) {
            throw new ElementoNoEncontradoException(
                    "El cliente de la reserva no está registrado.");
        }
        if (encontrarHotel(reserva.getHotel().getId()) != reserva.getHotel()) {
            throw new ElementoNoEncontradoException(
                    "El hotel de la reserva no está registrado.");
        }

        // Todas las habitaciones libres en esas fechas
        for (Habitacion habitacion : reserva.getHabitaciones()) {
            if (!habitacion.estaDisponible(reserva.getFechaEntrada(), reserva.getFechaSalida())) {
                throw new HabitacionNoDisponibleException("La habitación "
                        + habitacion.getNumero() + " ya está reservada en esas fechas.");
            }
        }

        // Solo si todo es correcto se registra la reserva
        reservas.add(reserva);
        reserva.getCliente().agregarReserva(reserva);
        // La habitación también guarda la reserva para poder calcular su disponibilidad
        for (Habitacion habitacion : reserva.getHabitaciones()) {
            habitacion.agregarReserva(reserva);
        }
    }

    /**
     * Devuelve el cliente con ese id o lanza una excepción si no existe,
     * en lugar de devolver null (evita NullPointerException en quien lo llama).
     */
    public Cliente buscarClientePorId(int id) throws ElementoNoEncontradoException {
        Cliente cliente = encontrarCliente(id);
        if (cliente == null) {
            throw new ElementoNoEncontradoException(
                    "No existe un cliente con el ID " + id + ".");
        }
        return cliente;
    }

    /** Búsqueda interna: devuelve null si no existe (solo para comprobar duplicados). */
    private Cliente encontrarCliente(int id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;
    }

    /**
     * Devuelve el hotel con ese id o lanza una excepción si no existe,
     * en lugar de devolver null (evita NullPointerException en quien lo llama).
     */
    public Hotel buscarHotelPorId(int id) throws ElementoNoEncontradoException {
        Hotel hotel = encontrarHotel(id);
        if (hotel == null) {
            throw new ElementoNoEncontradoException(
                    "No existe un hotel con el ID " + id + ".");
        }
        return hotel;
    }

    /** Búsqueda interna: devuelve null si no existe (solo para comprobar duplicados). */
    private Hotel encontrarHotel(int id) {
        for (Hotel hotel : hoteles) {
            if (hotel.getId() == id) {
                return hotel;
            }
        }
        return null;
    }

    /**
     * Devuelve el reserva con ese id o lanza una excepción si no existe,
     * en lugar de devolver null (evita NullPointerException en quien lo llama).
     */
    public Reserva buscarReservaPorId(int id) throws ElementoNoEncontradoException {
        Reserva reserva = encontrarReserva(id);
        if (reserva == null) {
            throw new ElementoNoEncontradoException(
                    "No existe una reserva con el ID " + id + ".");
        }
        return reserva;
    }

    /** Búsqueda interna: devuelve null si no existe (solo para comprobar duplicados). */
    private Reserva encontrarReserva(int id) {
        for (Reserva reserva : reservas) {
            if (reserva.getId() == id) {
                return reserva;
            }
        }
        return null;
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes);
    }

    public List<Hotel> listarHoteles() {
        return new ArrayList<>(hoteles);
    }

    public List<Reserva> listarReservas() {
        return new ArrayList<>(reservas);
    }

    public List<Cliente> buscarClientesPorNombre(String texto) {
        List<Cliente> resultado = new ArrayList<>();

        if (texto == null || texto.isBlank()) {
            return resultado;
        }

        String busqueda = texto.trim().toLowerCase();

        for (Cliente cliente : clientes) {
            if (cliente.getNombre().toLowerCase().contains(busqueda)
                    || cliente.getApellidos().toLowerCase().contains(busqueda)) {
                resultado.add(cliente);
            }
        }

        return resultado;
    }

    public List<Hotel> buscarHotelesPorCiudad(String ciudad) {
        List<Hotel> resultado = new ArrayList<>();

        if (ciudad == null || ciudad.isBlank()) {
            return resultado;
        }

        for (Hotel hotel : hoteles) {
            if (hotel.getCiudad().equalsIgnoreCase(ciudad.trim())) {
                resultado.add(hotel);
            }
        }

        return resultado;
    }

    public List<Reserva> consultarReservasCliente(int idCliente) {
        List<Reserva> resultado = new ArrayList<>();

        for (Reserva reserva : reservas) {
            if (reserva.getCliente().getId() == idCliente) {
                resultado.add(reserva);
            }
        }

        return resultado;
    }
}
