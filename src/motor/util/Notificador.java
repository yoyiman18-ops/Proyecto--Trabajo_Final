package motor.util;
import java.util.ArrayList;
import java.util.List;

public class Notificador<T> {
    private final List<Observador<T>> observadores;

    public Notificador() {
        this.observadores = new ArrayList<>();
    }

    public void notificar(T estado) {
        for (Observador<T> o : observadores) {
            o.cambio(estado);
        }
    }
    
    public void suscribir(Observador<T> observador) { this.observadores.add(observador); }
    public void desuscribir(Observador<T> observador) { this.observadores.remove(observador); }
    
    
}
