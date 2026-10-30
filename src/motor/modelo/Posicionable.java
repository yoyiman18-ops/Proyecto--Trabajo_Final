package motor.modelo;

import motor.util.VecDouble2D;
import motor.util.observer.Notificador;

public interface Posicionable {
    VecDouble2D getPosicion();
    Notificador<VecDouble2D> getNotificadorPosicion();
}
