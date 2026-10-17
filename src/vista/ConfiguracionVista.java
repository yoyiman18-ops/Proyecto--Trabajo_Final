package vista;

import java.util.Objects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ConfiguracionVista extends VBox implements VistaConfiguracion {
    private final double ancho;
    private final double alto;

    private final Label titulo;
    private final VBox opciones;
    private final Button botonVolver;

    public ConfiguracionVista() {
        this.ancho = 360;
        this.alto = 460;
        this.titulo = new Label("Configuración");
        this.opciones = new VBox();
        this.botonVolver = new Button("VOLVER");

        setPrefSize(ancho, alto);
        setMaxSize(ancho, alto);
        setSpacing(18);
        setPadding(new Insets(24));
        setAlignment(Pos.TOP_LEFT);
        setStyle("-fx-background-color: rgba(24,33,43,0.96);"
                + "-fx-background-radius: 12;"
                + "-fx-border-color: #526779;"
                + "-fx-border-width: 1;"
                + "-fx-border-radius: 12;");

        titulo.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold;");

        opciones.setSpacing(12);
        opciones.setFillWidth(true);

        getChildren().addAll(titulo, opciones, botonVolver);
    }

    @Override
    public Parent getRaiz() {
        return this;
    }

    @Override
    public void agregarOpcion(Node opcion) {
        Objects.requireNonNull(opcion, "La opción a agregar no puede ser nula");
        opciones.getChildren().add(opcion);
    }

    @Override
    public void setOnVolver(Runnable accion) {
        Objects.requireNonNull(accion, "La acción de volver no puede ser nula");
        botonVolver.setOnAction(event -> accion.run());
    }
}
