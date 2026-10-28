package motor.controlador;

import javafx.scene.Node;
import motor.modelo.Entidad;
import motor.util.Ticking;

public interface Controlador extends Ticking {
    Entidad getModelo();
    Node getVista();
}
