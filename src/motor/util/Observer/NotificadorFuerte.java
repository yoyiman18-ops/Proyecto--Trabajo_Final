package motor.util.Observer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificadorFuerte<T> implements Notificador<T> {
    private final List<Observador<T>> observadores;
    
    public NotificadorFuerte() {
        this.observadores = new CopyOnWriteArrayList<>();
    }

    @Override 
    public boolean suscribirObservador(Observador<T> o) {
        
    }

}
