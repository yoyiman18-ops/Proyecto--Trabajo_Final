package controlador;

import javafx.scene.Node;
import motor.entrada.EstadoAcciones;
import motor.modelo.Entidad;
import motor.util.observer.Observador;

public interface Controlador {
    Entidad getModelo();
    Node getVista();
    Observador<EstadoAcciones> getObservadorAcciones();
    void tick(Double dt);
}
