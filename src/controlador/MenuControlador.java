package controlador;

import java.util.Objects;

import javafx.scene.Parent;
import vista.VistaMenu;

public final class MenuControlador {
    private final VistaMenu vista;

    public MenuControlador(
        VistaMenu vista,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this.vista = Objects.requireNonNull(vista, "La vista del menú no puede ser nula");
        Objects.requireNonNull(onJugar, "La acción de jugar no puede ser nula");
        Objects.requireNonNull(onSalir, "La acción de salir no puede ser nula");

        this.vista.setOnJugar(onJugar);
        this.vista.setOnSalir(onSalir);
    }

    public Parent getVista() {
        return vista.getRaiz();
    }
}
