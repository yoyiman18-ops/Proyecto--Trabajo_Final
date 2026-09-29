package controlador;

import javafx.scene.Node;
import motor.modelo.Entidad;

public interface Controlador {
    Entidad getModelo();
    Node getVista();
}
