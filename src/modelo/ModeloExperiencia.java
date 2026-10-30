package modelo;

import motor.modelo.ModeloEntidad;
import motor.util.observer.Notificador;

public interface ModeloExperiencia
extends ModeloEntidad
{
    int getCantidad();
    boolean recoger(ModeloJugador jugador);
    Notificador<Integer> getNotificadorExpiracion();
}
