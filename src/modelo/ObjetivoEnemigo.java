package modelo;

import motor.modelo.Dañable;
import motor.modelo.Entidad;
import motor.util.VecDouble2D;

public interface ObjetivoEnemigo extends Dañable {
    double getVida();
    VecDouble2D getPosicion();
    Entidad getEntidad();
}
