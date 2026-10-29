package modelo;

import motor.modelo.Entidad;
import motor.util.VecDouble2D;

public interface ModeloEnemigo
extends Dañable,Dañador
{
    VecDouble2D getPosicion();
    VecDouble2D getVelocidad();
    double getVelocidadMaxima();
    int getExperienciaRecompensa();
    void mover(double dt);
    Entidad getEntidad();
}
