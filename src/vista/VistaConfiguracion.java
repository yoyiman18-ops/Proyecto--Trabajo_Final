package vista;

import javafx.scene.Node;
import javafx.scene.Parent;

public interface VistaConfiguracion {
    Parent getRaiz();
    void agregarOpcion(Node opcion);
    void setOnVolver(Runnable accion);
}
