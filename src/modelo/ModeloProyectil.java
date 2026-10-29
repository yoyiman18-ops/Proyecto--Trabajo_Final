package modelo;
import motor.modelo.Activable;
import motor.modelo.Dañable;
import motor.modelo.Dañador;
import motor.modelo.Ticking;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;

public interface ModeloProyectil
extends Dañador,Dañable,Ticking,Activable
{
    VecDouble2D getPosicion();
    Notificador<Integer> getNotificadorExpiracion();
}
