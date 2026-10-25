package modelo;

import motor.modelo.Entidad;
import motor.util.VecDouble2D;

public interface ModeloMoneda {
    VecDouble2D getPosicion();
    int getValor();
    boolean estaRecogida();
    boolean recoger();
    Entidad getEntidad();
}
