
package com.databuilders.hoteles.controlador;

import com.databuilders.hoteles.excepciones.GestionHotelesException;
import com.databuilders.hoteles.modelo.Datos;
import com.databuilders.hoteles.modelo.EstadoReserva;
import com.databuilders.hoteles.modelo.Reserva;
import com.databuilders.hoteles.modelo.Cliente;
import com.databuilders.hoteles.modelo.Hotel;
import com.databuilders.hoteles.modelo.Habitacion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador encargado de gestionar las operaciones de reservas.
 * Actúa como puente entre ReservaView y el modelo.
 */
public class ReservaController {

    private final Datos datos;

    public ReservaController(Datos datos) {
        if (datos == null) {
            throw new IllegalArgumentException("Los datos no pueden ser nulos.");
        }
        this.datos = datos;
    }

    public List<String> listarReservas() {
        return aTexto(datos.listarReservas());
    }

    public List<String> buscarReservaPorId(int idReserva)
            throws GestionHotelesException {
        return aTexto(List.of(datos.buscarReservaPorId(idReserva)));
    }


    public void altaReserva(int idReserva, int idCliente, int idHotel,
                            LocalDate fechaEntrada, LocalDate fechaSalida,
                            int numPersonas, List<Integer> idsHabitaciones)
            throws GestionHotelesException {

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
                throw new IllegalArgumentException(
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


    public void cancelarReserva(int idReserva)
            throws GestionHotelesException {
        datos.cancelarReserva(idReserva);
    }

    public void modificarReserva(int idReserva, LocalDate entrada,
                                 LocalDate salida, int personas, List<Integer> idsHabitaciones)
            throws GestionHotelesException {

        datos.modificarReserva(idReserva, entrada, salida,
                personas, idsHabitaciones);
    }

    public List<String> buscarReservasPorHotel(int idHotel)
            throws GestionHotelesException {
        return aTexto(datos.buscarReservasPorHotel(idHotel));
    }

    public List<String> buscarReservasPorFechaEntrada(LocalDate fecha)
            throws GestionHotelesException {
        return aTexto(datos.buscarReservasPorFechaEntrada(fecha));
    }

    public List<String> buscarReservasPorFechaSalida(LocalDate fecha)
            throws GestionHotelesException {
        return aTexto(datos.buscarReservasPorFechaSalida(fecha));
    }

    public List<String> buscarReservasPorEstado(String estado)
            throws GestionHotelesException {

        EstadoReserva estadoReserva = estadoDeTexto(estado);
        return aTexto(datos.buscarReservasPorEstado(estadoReserva));
    }

    private EstadoReserva estadoDeTexto(String estado)
            throws GestionHotelesException {

        if (estado == null) {
            throw new IllegalArgumentException("El estado no puede ser nulo.");
        }

        return switch (estado.trim().toLowerCase()) {
            case "confirmada" -> EstadoReserva.CONFIRMADA;
            case "cancelada" -> EstadoReserva.CANCELADA;
            case "finalizada" -> EstadoReserva.FINALIZADA;
            default -> throw new IllegalArgumentException("Estado no válido.");
        };
    }

    private List<String> aTexto(List<?> elementos) {
        List<String> textos = new ArrayList<>();

        for (Object elemento : elementos) {
            textos.add(elemento.toString());
        }

        return textos;
    }
}
