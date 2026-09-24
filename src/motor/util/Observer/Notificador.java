package motor.util.Observer;

public interface Notificador<T> {
    void notificar(T estado);
    boolean suscribirObservador(Observador<T> o);
    boolean desuscribirObservadores(Observador<T> o);
    void limpiarObservadores();
}
