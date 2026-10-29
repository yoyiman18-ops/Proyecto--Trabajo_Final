package modelo;

import motor.modelo.Entidad;
import motor.modelo.Ticking;
import motor.util.VecDouble2D;

public interface ModeloProyectil
extends Dañador,Ticking
{
    VecDouble2D getPosicion();
    Entidad getEntidad();
}
