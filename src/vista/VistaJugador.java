package vista;

import javafx.scene.Node;
import motor.modelo.Direccion;

public interface VistaJugador {
    Node getNodo();
    double getAncho();
    double getAlto();
    void actualizarPosicion(double x, double y);
    void actualizarAnimacion(Direccion direccion, boolean activa, double dt);
}
