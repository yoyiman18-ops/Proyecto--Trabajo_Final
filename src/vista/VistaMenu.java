package vista;

import java.util.Objects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public final class VistaMenu extends StackPane {

    public static final double ANCHO_MAX_BOTONES = 220;
    public static final double ESPACIADO_BOTONES = 12;
    public static final double MARGEN_INFERIOR_BOTONES = 48;

    private final Button botonJugar;
    private final Button botonSalir;

    public VistaMenu(Image imagenFondo, String hojaEstilosUrl) {
        Objects.requireNonNull(imagenFondo, "La imagen de fondo del menú no puede ser nula");
        Objects.requireNonNull(hojaEstilosUrl, "La URL de la hoja de estilos del menú no puede ser nula");
        this.botonJugar = new Button("JUGAR");
        this.botonSalir = new Button("SALIR");

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

        VBox botones = new VBox(ESPACIADO_BOTONES, botonJugar, botonSalir);
        botones.setAlignment(Pos.CENTER);
        botones.setMaxWidth(ANCHO_MAX_BOTONES);
        getChildren().add(botones);
        StackPane.setAlignment(botones, Pos.BOTTOM_CENTER);
        StackPane.setMargin(botones, new Insets(0, 0, MARGEN_INFERIOR_BOTONES, 0));

        getStylesheets().add(hojaEstilosUrl);
    }

    public Parent getRaiz() {
        return this;
    }

    public void setOnJugar(Runnable accion) {
        Objects.requireNonNull(accion, "La acción de jugar no puede ser nula");
        botonJugar.setOnAction(event -> accion.run());
    }

    public void setOnSalir(Runnable accion) {
        Objects.requireNonNull(accion, "La acción de salir no puede ser nula");
        botonSalir.setOnAction(event -> accion.run());
    }
}
