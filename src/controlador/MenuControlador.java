package controlador;

import java.util.Objects;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import motor.recursos.Extension;
import motor.recursos.GestorAudio;
import motor.recursos.GestorRecursos;
import vista.VistaMenu;

public final class MenuControlador {

    public static final String RECURSO_IMAGEN_MENU = "menu";

    private final VistaMenu vista;

    public MenuControlador(
        GestorRecursos gestorRecursos,
        String hojaEstilosUrl,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this(
            crearVistaMenu(gestorRecursos, hojaEstilosUrl),
            null,
            onJugar,
            onSalir
        );
    }

    public MenuControlador(
        GestorRecursos gestorRecursos,
        GestorAudio gestorAudio,
        String hojaEstilosUrl,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this(
            crearVistaMenu(gestorRecursos, hojaEstilosUrl),
            gestorAudio,
            onJugar,
            onSalir
        );
    }

    public MenuControlador(
        VistaMenu vista,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this(vista, null, onJugar, onSalir);
    }

    public MenuControlador(
        VistaMenu vista,
        GestorAudio gestorAudio,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this.vista = Objects.requireNonNull(vista, "La vista del menú no puede ser nula");
        Objects.requireNonNull(onJugar, "La acción de jugar no puede ser nula");
        Objects.requireNonNull(onSalir, "La acción de salir no puede ser nula");

        MusicaJuego.MENU.reproducirEn(gestorAudio);

        this.vista.setOnJugar(() -> {
            SonidoJuego.BOTON.reproducirEn(gestorAudio);
            onJugar.run();
        });
        this.vista.setOnSalir(() -> {
            SonidoJuego.BOTON.reproducirEn(gestorAudio);
            onSalir.run();
        });
    }

    private static VistaMenu crearVistaMenu(GestorRecursos gestorRecursos, String hojaEstilosUrl) {
        Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        Objects.requireNonNull(hojaEstilosUrl, "La URL de la hoja de estilos no puede ser nula");
        Image imagenMenu = gestorRecursos.getImagen(RECURSO_IMAGEN_MENU, Extension.Imagen.PNG);
        return new VistaMenu(imagenMenu, hojaEstilosUrl);
    }

    public Parent getVista() {
        return vista.getRaiz();
    }
}
