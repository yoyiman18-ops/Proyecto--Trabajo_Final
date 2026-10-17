package modelo;

import motor.modelo.Entidad;
import motor.util.VecDouble2D;

public interface ModeloEnemigo {
    VecDouble2D getPosicion();
    VecDouble2D getVelocidad();
    double getVelocidadMaxima();
    double getVida();
    double getVidaMaxima();
    double getDanio();
    int getExperienciaRecompensa();
    boolean recibirDanio(double cantidad);
    void mover(double dt);
    Entidad getEntidad();
}
