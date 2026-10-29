package modelo;

import motor.modelo.Entidad;
import motor.util.VecDouble2D;

public interface ModeloExperiencia {
    VecDouble2D getPosicion();
    int getCantidad();
    boolean fueRecogida();
    boolean recoger(ModeloJugador jugador);
    Entidad getEntidad();
}
