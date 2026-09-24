package motor.util.Observer;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

// un notificador que solo guarda referencias debiles a los observadores.
public class NotificadorDebil<T> implements Notificador<T> {
    private final Set<Observador<T>> observadores;

    public NotificadorDebil() {
        this.observadores = Collections.newSetFromMap(new WeakHashMap<>());
    }

    @Override 
    public void notificar(T estado) {
        for (Observador<T> o : observadores) { o.cambio(estado); }
    }
    
    @Override 
    public boolean suscribirObservador(Observador<T> o) { 
        return observadores.add(o);
    }

    @Override 
    public boolean desuscribirObservadores(Observador<T> o) { 
        return observadores.remove(o);
    }

    @Override 
    public void limpiarObservadores() {
        this.observadores.clear();
    }
}
