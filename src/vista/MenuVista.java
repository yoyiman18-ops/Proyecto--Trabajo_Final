package vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public final class MenuVista extends StackPane {
    private final Button botonJugar = new Button("JUGAR");
    private final Button botonSalir = new Button("SALIR");

    public MenuVista() {
        var recurso = MenuVista.class.getResource("/recursos/imagenes/menu.png");
        if (recurso == null) {
            throw new IllegalStateException("No se encontró el fondo del menú: menu.png");
        }

        BackgroundImage fondo = new BackgroundImage(
            new javafx.scene.image.Image(recurso.toExternalForm()),
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(1, 1, true, true, false, true)
        );
        setBackground(new Background(fondo));

        botonJugar.getStyleClass().add("menu-button");
        botonSalir.getStyleClass().add("menu-button");
        botonJugar.setMaxWidth(Double.MAX_VALUE);
        botonSalir.setMaxWidth(Double.MAX_VALUE);

        VBox botones = new VBox(12, botonJugar, botonSalir);
        botones.setAlignment(Pos.CENTER);
        botones.setMaxWidth(220);
        getChildren().add(botones);
        StackPane.setAlignment(botones, Pos.BOTTOM_CENTER);
        StackPane.setMargin(botones, new Insets(0, 0, 48, 0));

        var hojaEstilos = MenuVista.class.getResource("menu-vista.css");
        if (hojaEstilos == null) {
            throw new IllegalStateException("No se encontró menu-vista.css");
        }
        getStylesheets().add(hojaEstilos.toExternalForm());
    }

    public Button getBotonJugar() {
        return botonJugar;
    }

    public Button getBotonSalir() {
        return botonSalir;
    }
}