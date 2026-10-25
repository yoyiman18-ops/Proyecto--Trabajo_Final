package vista;

import javafx.scene.Node;

public interface VistaFondo {
    Node getNodo();
    void actualizarAnimacion(double dt);
    double getMargenHorizontalJugable();
    double getMargenSuperiorJugable();
    double getMargenInferiorJugable();
}
