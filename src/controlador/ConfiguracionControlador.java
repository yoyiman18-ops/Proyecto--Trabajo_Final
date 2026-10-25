package controlador;

import java.util.Objects;

import javafx.scene.Node;
import javafx.scene.Parent;
import vista.VistaConfiguracion;

public final class ConfiguracionControlador {
    private final VistaConfiguracion vista;

    public ConfiguracionControlador(VistaConfiguracion vista, Runnable onVolver) {
        this.vista = Objects.requireNonNull(vista, "La vista de configuración no puede ser nula");
        Objects.requireNonNull(onVolver, "La acción de volver no puede ser nula");
        this.vista.setOnVolver(onVolver);
    }

    public void agregarOpcion(Node opcion) {
        vista.agregarOpcion(opcion);
    }

    public Parent getVista() {
        return vista.getRaiz();
    }
}
