package vista;

import javafx.scene.Node;

public interface VistaEnemigo {
    Node getNodo();
    void actualizarPosicion(double x, double y);
    void actualizarAnimacion(double dt);
    void mostrarReaccionDanio(double proporcionVida);
}
