package vista;

import javafx.scene.layout.Pane;

/** Fachada compatible del contenedor principal de la partida. */
public final class JuegoVista extends VistaJuego {
    public JuegoVista() {
        setPrefSize(800, 600);
    }

    public Pane getEscenario() {
        return getAreaJuego();
    }
}