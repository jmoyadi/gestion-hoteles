
package com.databuilders.hoteles.controlador;

import com.databuilders.hoteles.modelo.Datos;
import com.databuilders.hoteles.modelo.Hotel;
import com.databuilders.hoteles.modelo.Habitacion;
import com.databuilders.hoteles.modelo.Estandar;
import com.databuilders.hoteles.modelo.Superior;
import com.databuilders.hoteles.modelo.Suite;
import com.databuilders.hoteles.excepciones.GestionHotelesException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador encargado de gestionar las operaciones de hoteles.
 * Actúa como puente entre HotelView y el modelo.
 */
public class HotelController {

    private final Datos datos;

    public HotelController(Datos datos) {
        if (datos == null) {
            throw new IllegalArgumentException("Los datos no pueden ser nulos.");
        }
        this.datos = datos;
    }

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

    public List<String> habitacionesDisponiblesHoy(int idHotel)
            throws GestionHotelesException {
        Hotel hotel = datos.buscarHotelPorId(idHotel);
        LocalDate hoy = LocalDate.now();

        return aTexto(hotel.getHabitacionesDisponibles(hoy, hoy.plusDays(1)));
    }


    public List<String> habitacionesPorCategoria(int idHotel, String categoria)
            throws GestionHotelesException {

        Hotel hotel = datos.buscarHotelPorId(idHotel);
        Class<? extends Habitacion> clase = claseDeCategoria(categoria);

        List<Habitacion> resultado = new ArrayList<>();

        for (Habitacion habitacion : hotel.getHabitaciones()) {
            if (clase.isInstance(habitacion)) {
                resultado.add(habitacion);
            }
        }

        return aTexto(resultado);
    }


    public int contarHabitacionesDisponibles(
            int idHotel, LocalDate entrada, LocalDate salida)
            throws GestionHotelesException {
        Hotel hotel = datos.buscarHotelPorId(idHotel);

        return hotel.contarHabitacionesDisponibles(entrada, salida);
    }


    private Class<? extends Habitacion> claseDeCategoria(String categoria) {
        return switch (categoria.toUpperCase()) {
            case "ESTANDAR" -> Estandar.class;
            case "SUPERIOR" -> Superior.class;
            case "SUITE" -> Suite.class;
            default -> throw new IllegalArgumentException("Categoría no válida.");
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
