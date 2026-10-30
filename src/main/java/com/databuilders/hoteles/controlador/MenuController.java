
package com.databuilders.hoteles.controlador;

import com.databuilders.hoteles.modelo.Datos;

/**
 * Controlador principal encargado de proporcionar los controladores
 * de cada apartado de la aplicación.
 */
public class MenuController {

    private final ClienteController clienteController;
    private final HotelController hotelController;
    private final HabitacionController habitacionController;
    private final ReservaController reservaController;

    public MenuController(Datos datos) {
        if (datos == null) {
            throw new IllegalArgumentException("Los datos no pueden ser nulos.");
        }

        this.clienteController = new ClienteController(datos);
        this.hotelController = new HotelController(datos);
        this.habitacionController = new HabitacionController(datos);
        this.reservaController = new ReservaController(datos);
    }

    public ClienteController getClienteController() {
        return clienteController;
    }

    public HotelController getHotelController() {
        return hotelController;
    }

    public HabitacionController getHabitacionController() {
        return habitacionController;
    }

    public ReservaController getReservaController() {
        return reservaController;
    }
}
