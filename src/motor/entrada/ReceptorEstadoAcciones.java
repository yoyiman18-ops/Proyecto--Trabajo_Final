package motor.entrada;
import motor.util.observer.Observador;

public interface ReceptorEstadoAcciones {
    public Observador<EstadoAcciones> getObservadorAcciones();
}