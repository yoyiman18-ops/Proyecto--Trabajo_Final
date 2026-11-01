package motor.modelo;

import motor.colisiones.Colisionable;
import motor.util.observer.Notificador;

public interface ModeloEntidad
extends Colisionable, Ticking, Activable, IdGlobal, Instanciador<ModeloEntidad>, Animable {
    Notificador<Integer> getNotificadorExpiracion();
}