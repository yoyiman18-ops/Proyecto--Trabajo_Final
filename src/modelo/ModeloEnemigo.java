package modelo;

import motor.modelo.Entidad;
import motor.util.VecDouble2D;

public interface ModeloEnemigo
extends Dañable,Dañador
{
    VecDouble2D getPosicion();
    VecDouble2D getVelocidad();
    double getVelocidadMaxima();
    double getVida();
    double getVidaMaxima();
    int getExperienciaRecompensa();
    Entidad getEntidad();
}
