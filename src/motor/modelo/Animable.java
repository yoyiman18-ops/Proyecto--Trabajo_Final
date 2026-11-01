package motor.modelo;

import motor.util.observer.Notificador;

public interface Animable {
    String getAnimacionDefault();
    String getAnimacionExpirar();
    Notificador<String> getNotificadorAnimacion();
}
