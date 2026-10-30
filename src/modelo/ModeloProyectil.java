package modelo;
import motor.modelo.Dañable;
import motor.modelo.Dañador;
import motor.modelo.ModeloEntidad;
import motor.util.observer.Notificador;

public interface ModeloProyectil
extends Dañador,Dañable,ModeloEntidad
{
    Notificador<Integer> getNotificadorExpiracion();
}
