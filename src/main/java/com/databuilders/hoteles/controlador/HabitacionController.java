
package com.databuilders.hoteles.controlador;

import com.databuilders.hoteles.excepciones.GestionHotelesException;
import com.databuilders.hoteles.modelo.Datos;
import com.databuilders.hoteles.modelo.Estandar;
import com.databuilders.hoteles.modelo.Suite;
import com.databuilders.hoteles.modelo.Superior;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador encargado de gestionar las operaciones de habitaciones.
 * Actúa como puente entre HabitacionView y el modelo.
 */
public class HabitacionController {

    private final Datos datos;

    public HabitacionController(Datos datos) {
        if (datos == null) {
            throw new IllegalArgumentException("Los datos no pueden ser nulos.");
        }
        this.datos = datos;
    }

    public void altaHabitacionEstandar(int idHotel, int id, int numero,
                                       int capacidad, double precioNoche,
                                       boolean tieneEscritorio, boolean tieneMinibar)
            throws GestionHotelesException {

        datos.agregarHabitacion(idHotel,
                new Estandar(id, numero, capacidad, precioNoche,
                        tieneEscritorio, tieneMinibar));
    }

    public void altaHabitacionSuperior(int idHotel, int id, int numero,
                                       int capacidad, double precioNoche,
                                       boolean tieneZonaEstar, boolean tieneVistas)
            throws GestionHotelesException {

        datos.agregarHabitacion(idHotel,
                new Superior(id, numero, capacidad, precioNoche,
                        tieneZonaEstar, tieneVistas));
    }

    public void altaHabitacionSuite(int idHotel, int id, int numero,
                                    int capacidad, double precioNoche,
                                    boolean tieneSalonIndependiente, int numeroBanos)
            throws GestionHotelesException {

        datos.agregarHabitacion(idHotel,
                new Suite(id, numero, capacidad, precioNoche,
                        tieneSalonIndependiente, numeroBanos));
    }

    public List<String> listarHabitacionesHotel(int idHotel)
            throws GestionHotelesException {
        return aTexto(datos.listarHabitacionesHotel(idHotel));
    }

    public List<String> buscarHabitacionesDisponibles(
            int idHotel, LocalDate entrada, LocalDate salida)
            throws GestionHotelesException {

        return aTexto(datos.buscarHabitacionesDisponibles(
                idHotel, entrada, salida));
    }

    public List<String> buscarHabitacionesDisponibles(
            int idHotel, LocalDate entrada, LocalDate salida, int personas)
            throws GestionHotelesException {

        return aTexto(datos.buscarHabitacionesDisponibles(
                idHotel, entrada, salida, personas));
    }

    private List<String> aTexto(List<?> elementos) {
        List<String> textos = new ArrayList<>();

        for (Object elemento : elementos) {
            textos.add(elemento.toString());
        }

        return textos;
    }
}
