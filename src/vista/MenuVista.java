package vista;

import java.util.Objects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
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

    public MenuVista(Image imagenFondo, String hojaEstilosUrl) {
        Objects.requireNonNull(imagenFondo, "La imagen de fondo del menú no puede ser nula");
        Objects.requireNonNull(hojaEstilosUrl, "La URL de la hoja de estilos del menú no puede ser nula");

        BackgroundImage fondo = new BackgroundImage(
            imagenFondo,
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

        getStylesheets().add(hojaEstilosUrl);
    }

    public Button getBotonJugar() {
        return botonJugar;
    }

    public Button getBotonSalir() {
        return botonSalir;
    }
}