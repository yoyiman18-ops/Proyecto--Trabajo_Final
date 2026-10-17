package vista;

import javafx.scene.Parent;

public interface VistaMenu {
    Parent getRaiz();
    void setOnJugar(Runnable accion);
    void setOnSalir(Runnable accion);
}
