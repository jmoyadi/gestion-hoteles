package com.databuilders.hoteles.modelo;

/**
 * Representa un hotel de la cadena hotelera.
 *
 * CLASE DE PRUEBA para comprobar que el repositorio compartido funciona.
 * Se basa en el diagrama de clases de referencia y puede cambiar cuando
 * el grupo cierre el diagrama consolidado del Producto 1.
 * Generada con ayuda de IA (Claude): hay que citarlo en la memoria.
 *
 * Pendiente del consenso: la lista de habitaciones (composición
 * Hotel 1 —— 1..* Habitacion) se añadirá cuando exista la clase Habitacion.
 */
public class Hotel {

    // Atributos privados: solo se accede a ellos mediante getters y setters
    // (encapsulación).
    private int idHotel;
    private String nombre;
    private String direccion;
    private String ciudad;

    /**
     * Constructor vacío. Será útil más adelante para la persistencia
     * (JDBC y, sobre todo, ORM en el Producto 4, que lo exige).
     */
    public Hotel() {
    }

    /**
     * Constructor con todos los atributos.
     *
     * @param idHotel   identificador del hotel
     * @param nombre    nombre comercial del hotel
     * @param direccion dirección postal
     * @param ciudad    ciudad (el caso pide buscar hoteles por ciudad)
     */
    public Hotel(int idHotel, String nombre, String direccion, String ciudad) {
        this.idHotel = idHotel;
        this.nombre = nombre;
        this.direccion = direccion;
        this.ciudad = ciudad;
    }

    // ----- Getters y setters -----

    public int getIdHotel() {
        return idHotel;
    }

    public void setIdHotel(int idHotel) {
        this.idHotel = idHotel;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * Representación en texto del hotel, útil para mostrarlo por consola
     * y para depurar.
     */
    @Override
    public String toString() {
        return "Hotel{" +
                "idHotel=" + idHotel +
                ", nombre='" + nombre + '\'' +
                ", direccion='" + direccion + '\'' +
                ", ciudad='" + ciudad + '\'' +
                '}';
    }
}
