package modelo;

import motor.modelo.Entidad;
import motor.util.VecDouble2D;

public interface ModeloProyectil
extends Dañador
{
    VecDouble2D getPosicion();
    boolean estaExpirado();
    Entidad getEntidad();
}
