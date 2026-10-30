
package com.databuilders.hoteles.controlador;

import com.databuilders.hoteles.excepciones.GestionHotelesException;
import com.databuilders.hoteles.modelo.Cliente;
import com.databuilders.hoteles.modelo.Datos;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador encargado de gestionar las operaciones de clientes.
 * Actúa como puente entre ClienteView y el modelo.
 */
public class ClienteController {

    private final Datos datos;

    public ClienteController(Datos datos) {
        if (datos == null) {
            throw new IllegalArgumentException("Los datos no pueden ser nulos.");
        }
        this.datos = datos;
    }

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

    public List<String> buscarClientePorId(int id)
            throws GestionHotelesException {
        return aTexto(List.of(datos.buscarClientePorId(id)));
    }

    public void modificarCliente(int id, String nombre, String apellidos, String email)
            throws GestionHotelesException {
        datos.modificarCliente(id, nombre, apellidos, email);
    }

    public void eliminarCliente(int id) throws GestionHotelesException {
        datos.eliminarCliente(id);
    }

    private List<String> aTexto(List<?> elementos) {
        List<String> textos = new ArrayList<>();

        for (Object elemento : elementos) {
            textos.add(elemento.toString());
        }

        return textos;
    }
}
