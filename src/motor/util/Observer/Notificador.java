package motor.util.observer;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/*
un notificador que encola las modificaciones y solo las aplica al comienzo de cada ciclo de notificacion.
tiene el constructor protegido para diferenciar claramente el notificador con un set de referencias fuertes
al notificador con referencias débiles
*/

public abstract class Notificador<T> {
    private final Set<Observador<T>> observadores;
    private final Queue<Runnable> colaModificaciones;

    protected Notificador(Set<Observador<T>> observadores) {
        this.observadores = observadores;
        this.colaModificaciones = new ConcurrentLinkedQueue<>();
    }

    public void notificar(T estado) { 
        Runnable modificacion;
        while ((modificacion = colaModificaciones.poll()) != null) { modificacion.run(); }
        for (Observador<T> o : observadores) { o.cambio(estado);}
    }

    public void suscribirObservador(Observador<T> o) { colaModificaciones.add(() -> observadores.add(o)); }
    public void desuscribirObservador(Observador<T> o) { colaModificaciones.add(() -> observadores.remove(o)); }
    public void limpiarObservadores() { colaModificaciones.add(() -> observadores.clear()); }
}
