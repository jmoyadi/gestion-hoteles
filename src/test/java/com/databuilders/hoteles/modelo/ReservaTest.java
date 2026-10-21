
package com.databuilders.hoteles.modelo;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ReservaTest {

    @Test
    void calcularImporte_DosNoches_DosHabitaciones() throws Exception {
        // Arrange: preparamos los datos de la prueba.
        Cliente cliente = new Cliente(
                1, "Alicia", "Prueba", "alicia@example.com");

        Hotel hotel = new Hotel(1, "Hotel de prueba", "Barcelona");

        Estandar habitacion1 = new Estandar(
                1, 101, 2, 80.0, true, true);

        Estandar habitacion2 = new Estandar(
                2, 102, 2, 100.0, true, true);

        Datos datos = new Datos();
        datos.agregarCliente(cliente);
        datos.agregarHotel(hotel);
        datos.agregarHabitacion(hotel.getId(), habitacion1);
        datos.agregarHabitacion(hotel.getId(), habitacion2);

        LocalDate entrada = LocalDate.of(2026, 11, 10);
        LocalDate salida = LocalDate.of(2026, 11, 12);

        Reserva reserva = new Reserva(
                1, entrada, salida, 4,
                cliente, hotel, List.of(habitacion1, habitacion2));

        // Act: obtenemos el importe calculado por la reserva.
        double importe = reserva.calcularImporte();

        // Assert: 2 noches × (80 € + 100 €) = 360 €.
        assertEquals(360.0, importe, 0.01);
    }


    @Test
    void estaDisponible_SinReservas_DevuelveTrue() throws Exception {
        Estandar habitacion = new Estandar(
                10, 110, 2, 80.0, true, true);

        LocalDate entrada = LocalDate.of(2026, 11, 10);
        LocalDate salida = LocalDate.of(2026, 11, 12);

        assertTrue(habitacion.estaDisponible(entrada, salida));
    }

    @Test
    void estaDisponible_DespuesDeCancelarReserva_DevuelveTrue()
            throws Exception {
        Cliente cliente = new Cliente(
                2, "Cliente", "Prueba", "cliente@example.com");

        Hotel hotel = new Hotel(2, "Hotel de prueba", "Barcelona");

        Estandar habitacion = new Estandar(
                20, 120, 2, 80.0, true, true);

        Datos datos = new Datos();
        datos.agregarCliente(cliente);
        datos.agregarHotel(hotel);
        datos.agregarHabitacion(hotel.getId(), habitacion);

        LocalDate entrada = LocalDate.of(2026, 11, 10);
        LocalDate salida = LocalDate.of(2026, 11, 12);

        Reserva reserva = new Reserva(
                20, entrada, salida, 2,
                cliente, hotel, List.of(habitacion));

        datos.agregarReserva(reserva);

        assertFalse(habitacion.estaDisponible(entrada, salida));

        datos.cancelarReserva(reserva.getId());

        assertTrue(habitacion.estaDisponible(entrada, salida));
    }


    @Test
    void estaDisponible_ConReservaActiva_DevuelveFalse()
            throws Exception {
        Cliente cliente = new Cliente(
                3, "Cliente", "Prueba", "cliente3@example.com");

        Hotel hotel = new Hotel(3, "Hotel de prueba", "Barcelona");

        Estandar habitacion = new Estandar(
                30, 130, 2, 80.0, true, true);

        Datos datos = new Datos();
        datos.agregarCliente(cliente);
        datos.agregarHotel(hotel);
        datos.agregarHabitacion(hotel.getId(), habitacion);

        LocalDate entrada = LocalDate.of(2026, 11, 10);
        LocalDate salida = LocalDate.of(2026, 11, 12);

        Reserva reserva = new Reserva(
                30, entrada, salida, 2,
                cliente, hotel, List.of(habitacion));

        datos.agregarReserva(reserva);

        assertFalse(habitacion.estaDisponible(entrada, salida));
    }

}
