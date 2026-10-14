package vista;

import javafx.scene.layout.Pane;
import motor.recursos.cache.CacheImagenes;

/** Fachada compatible del contenedor principal de la partida. */
public final class JuegoVista extends VistaJuego {
    public JuegoVista(CacheImagenes cacheImagenes, double tamanoTile) {
        super(cacheImagenes, tamanoTile);
        setPrefSize(800, 600);
    }

    public Pane getEscenario() {
        return getAreaJuego();
    }
}