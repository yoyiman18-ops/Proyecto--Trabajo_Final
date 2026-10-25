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
    public static final double ANCHO_DEFAULT = 360;
    public static final double ALTO_DEFAULT = 460;
    public static final String RUTA_ESTILOS_DEFAULT = "/vista/menu-vista.css";

    private final double ancho;
    private final double alto;

    private final Label titulo;
    private final VBox opciones;
    private final Button botonVolver;

    public ConfiguracionVista() {
        this(ANCHO_DEFAULT, ALTO_DEFAULT, null);
    }

    public ConfiguracionVista(double ancho, double alto, String hojaEstilosUrl) {
        if (!Double.isFinite(ancho) || ancho <= 0 || !Double.isFinite(alto) || alto <= 0) {
            throw new IllegalArgumentException("Las dimensiones de la vista de configuración deben ser positivas y finitas.");
        }
        this.ancho = ancho;
        this.alto = alto;
        this.titulo = new Label("Configuración");
        this.opciones = new VBox();
        this.botonVolver = new Button("VOLVER");

        setPrefSize(ancho, alto);
        setMaxSize(ancho, alto);
        setSpacing(18);
        setPadding(new Insets(24));
        setAlignment(Pos.TOP_LEFT);

        getStyleClass().add("config-panel");
        titulo.getStyleClass().add("config-title");
        botonVolver.getStyleClass().add("menu-button");

        if (hojaEstilosUrl != null && !hojaEstilosUrl.isBlank()) {
            getStylesheets().add(hojaEstilosUrl);
        } else {
            var resource = getClass().getResource(RUTA_ESTILOS_DEFAULT);
            if (resource != null) {
                getStylesheets().add(resource.toExternalForm());
            }
        }

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
