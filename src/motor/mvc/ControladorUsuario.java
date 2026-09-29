package motor.mvc;

import motor.entrada.EstadoAcciones;
import motor.util.observer.Observador;

public interface ControladorUsuario extends Controlador {
    public Observador<EstadoAcciones> getObservadorAcciones();
}
