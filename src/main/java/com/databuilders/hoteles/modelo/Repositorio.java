package com.databuilders.hoteles.modelo;

import com.databuilders.hoteles.excepciones.ElementoDuplicadoException;
import com.databuilders.hoteles.excepciones.ElementoNoEncontradoException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Clase genérica que guarda elementos identificables por su id.
 *
 * Reúne en un solo sitio el código de alta, búsqueda, borrado y listado
 * que antes estaba repetido en Datos para clientes, hoteles y reservas.
 * T es el tipo que se guarda (Cliente, Hotel, Reserva...) y tiene que
 * implementar Identificable para poder usar getId() (tipo genérico acotado).
 *
 * Por dentro usa un LinkedHashMap con el id como clave:
 *  - búsqueda por id directa, sin recorrer la colección (O(1));
 *  - containsKey detecta los ids duplicados;
 *  - mantiene el orden de alta, así los listados salen en un orden lógico
 *    (un HashMap los devolvería en orden imprevisible y un TreeMap
 *    ordenaría por id con un coste O(log n)).
 *
 * Solo guarda y busca: las reglas de negocio siguen en Datos.
 *
 * Creada con ayuda de IA (Claude).
 *
 * @param <T> tipo de elemento guardado
 */
public class Repositorio<T extends Identificable> {

    private final Map<Integer, T> elementos = new LinkedHashMap<>();

    /** Descripción con artículo para los mensajes: "un cliente", "una reserva"... */
    private final String descripcion;

    public Repositorio(String descripcion) {
        if (descripcion == null || descripcion.isBlank()) {
            throw new IllegalArgumentException("La descripción no puede estar vacía.");
        }
        this.descripcion = descripcion;
    }

    /** Añade el elemento; lanza una excepción si ya hay otro con el mismo id. */
    public void agregar(T elemento) throws ElementoDuplicadoException {
        if (elemento == null) {
            throw new IllegalArgumentException("No se puede guardar un elemento nulo.");
        }
        if (elementos.containsKey(elemento.getId())) {
            throw new ElementoDuplicadoException(
                    "Ya existe " + descripcion + " con el ID " + elemento.getId() + ".");
        }
        elementos.put(elemento.getId(), elemento);
    }

    /** Devuelve el elemento con ese id o lanza una excepción si no existe. */
    public T buscarPorId(int id) throws ElementoNoEncontradoException {
        T elemento = elementos.get(id);
        if (elemento == null) {
            throw new ElementoNoEncontradoException(
                    "No existe " + descripcion + " con el ID " + id + ".");
        }
        return elemento;
    }

    /** Indica si hay un elemento con ese id. */
    public boolean existe(int id) {
        return elementos.containsKey(id);
    }

    /**
     * Indica si este objeto concreto es el que está guardado con su id
     * (no basta con que otro objeto tenga el mismo id).
     */
    public boolean contiene(T elemento) {
        return elemento != null && elementos.get(elemento.getId()) == elemento;
    }

    /** Quita el elemento con ese id y lo devuelve. */
    public T eliminar(int id) throws ElementoNoEncontradoException {
        T elemento = elementos.remove(id);
        if (elemento == null) {
            throw new ElementoNoEncontradoException(
                    "No existe " + descripcion + " con el ID " + id + ".");
        }
        return elemento;
    }

    /** Devuelve todos los elementos en orden de alta, en una lista que no se puede modificar. */
    public List<T> listar() {
        return Collections.unmodifiableList(new ArrayList<>(elementos.values()));
    }

    /** Devuelve los elementos que cumplen el criterio (se usa en las búsquedas). */
    public List<T> filtrar(Predicate<T> criterio) {
        if (criterio == null) {
            throw new IllegalArgumentException("El criterio no puede ser nulo.");
        }
        List<T> resultado = new ArrayList<>();
        for (T elemento : elementos.values()) {
            if (criterio.test(elemento)) {
                resultado.add(elemento);
            }
        }
        return resultado;
    }

    /** Número de elementos guardados. */
    public int contar() {
        return elementos.size();
    }
}
