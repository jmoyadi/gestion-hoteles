
package com.databuilders.hoteles.modelo;

import java.util.ArrayList;
import java.util.List;

public class Datos {

    private final List<Cliente> clientes = new ArrayList<>();
    private final List<Hotel> hoteles = new ArrayList<>();
    private final List<Reserva> reservas = new ArrayList<>();

    public void agregarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }

        if (buscarClientePorId(cliente.getId()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un cliente con ese ID.");
        }

        clientes.add(cliente);
    }

    public void agregarHotel(Hotel hotel) {
        if (hotel == null) {
            throw new IllegalArgumentException("El hotel no puede ser nulo.");
        }

        if (buscarHotelPorId(hotel.getId()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe un hotel con ese ID.");
        }

        hoteles.add(hotel);
    }

    public void agregarReserva(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no puede ser nula.");
        }

        if (buscarReservaPorId(reserva.getId()) != null) {
            throw new IllegalArgumentException(
                    "Ya existe una reserva con ese ID.");
        }

        reservas.add(reserva);
        reserva.getCliente().agregarReserva(reserva);
        // La habitación también guarda la reserva para poder calcular su disponibilidad
        for (Habitacion habitacion : reserva.getHabitaciones()) {
            habitacion.agregarReserva(reserva);
        }
    }

    public Cliente buscarClientePorId(int id) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == id) {
                return cliente;
            }
        }
        return null;
    }

    public Hotel buscarHotelPorId(int id) {
        for (Hotel hotel : hoteles) {
            if (hotel.getId() == id) {
                return hotel;
            }
        }
        return null;
    }

    public Reserva buscarReservaPorId(int id) {
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
