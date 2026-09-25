package vista;

import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/** Contenedor visual de la partida: escenario y HUD superpuesto. */
public class JuegoVista extends StackPane {
    private static final double ANCHO = 800;
    private static final double ALTO = 600;

    private final Pane escenario = new Pane();
    private final HudVista hud = new HudVista();

    public JuegoVista() {
        setPrefSize(ANCHO, ALTO);

        escenario.setPrefSize(ANCHO, ALTO);
        escenario.setStyle("-fx-background-color: #18212b;");

        hud.setMouseTransparent(true);

        getChildren().addAll(escenario, hud);
        StackPane.setAlignment(hud, Pos.TOP_LEFT);
    }

    public Pane getEscenario() {
        return escenario;
    }

    public HudVista getHud() {
        return hud;
    }
}