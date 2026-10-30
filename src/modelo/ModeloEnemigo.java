package modelo;

import motor.modelo.Dañable;
import motor.modelo.Dañador;
import motor.modelo.ModeloEntidad;
import motor.util.observer.Notificador;

public interface ModeloEnemigo
extends Dañable, Dañador, ModeloEntidad
{
    double getVida();
    double getVidaMaxima();
    int getExperienciaRecompensa();
    Notificador<Double> getNotificadorDaño();
    Notificador<Integer> getNotificadorExpiracion();
}
