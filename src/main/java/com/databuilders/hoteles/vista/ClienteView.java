
package com.databuilders.hoteles.vista;

import com.databuilders.hoteles.controlador.ClienteController;
import com.databuilders.hoteles.excepciones.GestionHotelesException;

import java.util.List;
import java.util.Scanner;

/**
 * Vista encargada de mostrar el menú y las operaciones de clientes.
 */
public class ClienteView {

    private final ClienteController controlador;
    private final Scanner teclado;

    public ClienteView(ClienteController controlador, Scanner teclado) {
        this.controlador = controlador;
        this.teclado = teclado;
    }

    public void mostrarMenu() {
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
                    case 1 -> mostrarLista(
                            "Clientes registrados",
                            controlador.listarClientes());

                    case 2 -> {
                        String texto = leerTexto("Nombre o apellido a buscar: ");
                        mostrarLista("Clientes encontrados",
                                controlador.buscarClientesPorNombre(texto));
                    }

                    case 3 -> altaCliente();

                    case 4 -> modificarCliente();

                    case 5 -> eliminarCliente();

                    case 6 -> {
                        int id = leerEntero("ID del cliente: ");
                        mostrarLista("Reservas del cliente",
                                controlador.consultarReservasCliente(id));
                    }

                    case 0 -> { }

                    default -> System.out.println("Opción no válida.");
                }

            } catch (GestionHotelesException e) {
                mostrarError(e);
            }

        } while (opcion != 0);
    }

    private void altaCliente() throws GestionHotelesException {
        int id = leerEntero("ID del cliente: ");
        String nombre = leerTexto("Nombre: ");
        String apellidos = leerTexto("Apellidos: ");
        String email = leerTexto("Email: ");

        controlador.altaCliente(id, nombre, apellidos, email);

        System.out.println("Cliente añadido correctamente.");
    }

    private void modificarCliente() throws GestionHotelesException {
        int id = leerEntero("ID del cliente a modificar: ");

        mostrarLista("Cliente actual", controlador.buscarClientePorId(id));

        String nombre = leerTexto("Nuevo nombre: ");
        String apellidos = leerTexto("Nuevos apellidos: ");
        String email = leerTexto("Nuevo email: ");

        controlador.modificarCliente(id, nombre, apellidos, email);

        System.out.println("Cliente modificado correctamente.");
    }

    private void eliminarCliente() throws GestionHotelesException {
        int id = leerEntero("ID del cliente a eliminar: ");

        mostrarLista("Cliente a eliminar", controlador.buscarClientePorId(id));

        if (leerSiNo("¿Seguro que quieres eliminarlo? (s/n): ")) {
            controlador.eliminarCliente(id);
            System.out.println("Cliente eliminado correctamente.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

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

    private String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    private boolean leerSiNo(String mensaje) {
        while (true) {
            String respuesta = leerTexto(mensaje).toLowerCase();

            if (respuesta.equals("s") || respuesta.equals("si")
                    || respuesta.equals("sí")) {
                return true;
            }

            if (respuesta.equals("n") || respuesta.equals("no")) {
                return false;
            }

            System.out.println("Responde s o n.");
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
