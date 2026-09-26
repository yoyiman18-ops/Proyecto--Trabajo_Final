package vista;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import motor.recursos.Extension;
import motor.recursos.GestorRecursos;

public class MenuVista extends StackPane {
    private static final double ANCHO = 800;
    private static final double ALTO = 600;

    private final Pane interfaz = new Pane();
    private final ImageView fondo = new ImageView();
    private final ConfiguracionVista configuracion = new ConfiguracionVista();
    private final Button botonJugar = new Button("JUGAR");
    private final Button botonConfiguracion = new Button("CONFIGURACIÓN");
    private final VBox botonesMenu = new VBox(12, botonJugar, botonConfiguracion);

    public MenuVista() {
        setPrefSize(ANCHO, ALTO);

        interfaz.setPrefSize(ANCHO, ALTO);
        fondo.setImage(GestorRecursos.getInstancia()
            .getImagen("menu", Extension.Imagen.PNG));
        fondo.setFitWidth(ANCHO);
        fondo.setFitHeight(ALTO);
        fondo.setPreserveRatio(false);
        fondo.setMouseTransparent(true);
        interfaz.getChildren().add(fondo);

        botonesMenu.setAlignment(Pos.CENTER);

        configuracion.setVisible(false);
        configuracion.setManaged(false);
        botonConfiguracion.setOnAction(event -> mostrarConfiguracion());
        configuracion.getBotonVolver().setOnAction(event -> mostrarMenuPrincipal());

        getChildren().addAll(interfaz, botonesMenu, configuracion);
        StackPane.setAlignment(botonesMenu, Pos.CENTER);
        StackPane.setAlignment(configuracion, Pos.CENTER);
    }

    private void mostrarConfiguracion() {
        botonesMenu.setVisible(false);
        botonesMenu.setManaged(false);
        configuracion.setVisible(true);
        configuracion.setManaged(true);
    }

    public void mostrarMenuPrincipal() {
        configuracion.setVisible(false);
        configuracion.setManaged(false);
        botonesMenu.setVisible(true);
        botonesMenu.setManaged(true);
    }

    public Pane getInterfaz() {
        return interfaz;
    }

    public ConfiguracionVista getConfiguracion() {
        return configuracion;
    }
    public Button getBotonJugar() {
        return botonJugar;
    }

    public Button getBotonConfiguracion() {
        return botonConfiguracion;
    }
}
