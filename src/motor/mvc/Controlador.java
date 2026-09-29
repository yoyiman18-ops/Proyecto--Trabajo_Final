package motor.mvc;

import javafx.scene.Node;
import motor.modelo.Entidad;

public interface Controlador {
    Entidad getModelo();
    Node getVista();
    void tick(Double dt);
}
