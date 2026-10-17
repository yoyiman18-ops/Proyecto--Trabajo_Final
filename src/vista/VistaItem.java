package vista;

import javafx.scene.Node;

public interface VistaItem {
    Node getNodo();
    void actualizarPosicion(double x, double y);
}
