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
 * Los clientes, hoteles y reservas se guardan en tres repositorios genéricos
 * (Repositorio<T>), que se encargan de guardar y buscar por id. Datos se
 * queda con las reglas de negocio (por ejemplo, que el cliente de una
 * reserva esté registrado o que las habitaciones estén libres).
 *
 * Gestión de excepciones propias y uso de la clase genérica Repositorio
 * añadidos con ayuda de IA (Claude).
 */
public class Datos {

    private final Repositorio<Cliente> clientes = new Repositorio<>("un cliente");
    private final Repositorio<Hotel> hoteles = new Repositorio<>("un hotel");
    private final Repositorio<Reserva> reservas = new Repositorio<>("una reserva");

    // ---------- Altas ----------

    public void agregarCliente(Cliente cliente) throws ElementoDuplicadoException {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }
        clientes.agregar(cliente);
    }

    public void agregarHotel(Hotel hotel) throws ElementoDuplicadoException {
        if (hotel == null) {
            throw new IllegalArgumentException("El hotel no puede ser nulo.");
        }
        hoteles.agregar(hotel);
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

        if (reservas.existe(reserva.getId())) {
            throw new ElementoDuplicadoException(
                    "Ya existe una reserva con el ID " + reserva.getId() + ".");
        }

        // El cliente y el hotel tienen que ser los mismos objetos dados de alta
        // (contiene() exige el mismo objeto, no solo el mismo id)
        if (!clientes.contiene(reserva.getCliente())) {
            throw new ElementoNoEncontradoException(
                    "El cliente de la reserva no está registrado.");
        }
        if (!hoteles.contiene(reserva.getHotel())) {
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
        reservas.agregar(reserva);
        reserva.getCliente().agregarReserva(reserva);
        // La habitación también guarda la reserva para poder calcular su disponibilidad
        for (Habitacion habitacion : reserva.getHabitaciones()) {
            habitacion.agregarReserva(reserva);
        }
    }

    // ---------- Búsquedas por id ----------
    // Lanzan ElementoNoEncontradoException en lugar de devolver null
    // (evita NullPointerException en quien las llama).

    public Cliente buscarClientePorId(int id) throws ElementoNoEncontradoException {
        return clientes.buscarPorId(id);
    }

    public Hotel buscarHotelPorId(int id) throws ElementoNoEncontradoException {
        return hoteles.buscarPorId(id);
    }

    public Reserva buscarReservaPorId(int id) throws ElementoNoEncontradoException {
        return reservas.buscarPorId(id);
    }

    // ---------- Listados (en orden de alta, no modificables) ----------

    public List<Cliente> listarClientes() {
        return clientes.listar();
    }

    public List<Hotel> listarHoteles() {
        return hoteles.listar();
    }

    public List<Reserva> listarReservas() {
        return reservas.listar();
    }

    // ---------- Búsquedas por criterio ----------

    public List<Cliente> buscarClientesPorNombre(String texto) {
        if (texto == null || texto.isBlank()) {
            return new ArrayList<>();
        }
        String busqueda = texto.trim().toLowerCase();
        return clientes.filtrar(cliente ->
                cliente.getNombre().toLowerCase().contains(busqueda)
                        || cliente.getApellidos().toLowerCase().contains(busqueda));
    }

    public List<Hotel> buscarHotelesPorCiudad(String ciudad) {
        if (ciudad == null || ciudad.isBlank()) {
            return new ArrayList<>();
        }
        String busqueda = ciudad.trim();
        return hoteles.filtrar(hotel -> hotel.getCiudad().equalsIgnoreCase(busqueda));
    }

    public List<Reserva> consultarReservasCliente(int idCliente) {
        return reservas.filtrar(reserva -> reserva.getCliente().getId() == idCliente);
    }
}
