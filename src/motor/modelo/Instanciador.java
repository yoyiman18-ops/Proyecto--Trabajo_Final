package motor.modelo;
import motor.util.observer.Notificador;

public interface Instanciador<T> {
    Notificador<T> getNotificadorInstanciacion();
}
