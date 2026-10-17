package com.databuilders.hoteles.controlador;

import com.databuilders.hoteles.excepciones.GestionHotelesException;
import com.databuilders.hoteles.modelo.Cliente;
import com.databuilders.hoteles.modelo.Datos;
import com.databuilders.hoteles.modelo.Hotel;

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

    // ---------- Utilidad ----------

    /** Convierte una lista de objetos del modelo en textos para la vista. */
    private List<String> aTexto(List<?> elementos) {
        List<String> textos = new ArrayList<>();
        for (Object elemento : elementos) {
            textos.add(elemento.toString());
        }
        return textos;
    }
}
