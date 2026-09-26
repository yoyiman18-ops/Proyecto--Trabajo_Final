package motor.util.observer;
import java.util.Collections;
import java.util.WeakHashMap;

// un notificador que solo guarda referencias debiles a los observadores.
public class NotificadorDebil<T> extends Notificador<T> {

    public NotificadorDebil() {
        super(Collections.newSetFromMap(new WeakHashMap<>()));
    }
}
