package motor.modelo;

import motor.entrada.EstadoAcciones;
import motor.util.observer.Observador;

/**
 * Define la capacidad de una entidad de ser controlada reactivamente
 * mediante el flujo de acciones de entrada del motor.
 */
public interface Controlable extends Observador<EstadoAcciones> {

    /**
     * Retorna el observador encargado de recibir los cambios de estado de acciones.
     * Por defecto retorna la propia instancia, que implementa {@link Observador#cambio(Object)}.
     *
     * @return Observador receptor de EstadoAcciones.
     */
    default Observador<EstadoAcciones> getObservadorEstadoAcciones() {
        return this;
    }
}
