package modelo;

import motor.util.VecDouble2D;

public interface ObjetivoEnemigo {
    double getVida();
    VecDouble2D getPosicion();
    void recibirDanio(double cantidad);
}
