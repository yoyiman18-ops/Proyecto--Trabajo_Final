package motor.colisiones;

import motor.modelo.Interactuable;

/**
 * Interfaz encargada de resolver las interacciones mutuas entre dos objetos interactuables.
 */
public interface ResolvedorInteraccion {

    /**
     * Resuelve las interacciones entre dos interactuables, evitando condiciones de carrera
     * durante la iteración mutua.
     *
     * @param i1 Primer interactuable.
     * @param i2 Segundo interactuable.
     */
    void resolverInteracciones(Interactuable i1, Interactuable i2);

    /**
     * Alias de conveniencia para {@link #resolverInteracciones(Interactuable, Interactuable)}.
     */
    default void resolver(Interactuable i1, Interactuable i2) {
        resolverInteracciones(i1, i2);
    }
}
