package controlador;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.scene.Node;
import javafx.scene.image.Image;
import motor.recursos.Extension;
import motor.recursos.GestorRecursos;
import vista.FondoMazmorra;
import vista.VistaJuego;

public final class ControladorVistaJuego {
    private final String[] nombresImagenesHud = {
        "led-heart-red", "led-heart", "hud_xp", "hud_gema_gris", "hud_gema_verde"
    };
    private final String[] clavesImagenesHud = {
        "corazonLleno", "corazonVacio", "iconoExperiencia",
        "gemaExperienciaVacia", "gemaExperienciaLlena"
    };

    private final VistaJuego vista;
    private final FondoMazmorra fondoMazmorra;

    public ControladorVistaJuego(VistaJuego vista) {
        this(vista, null);
    }

    public ControladorVistaJuego(VistaJuego vista, FondoMazmorra fondoMazmorra) {
        this.vista = Objects.requireNonNull(vista, "La vista del juego no puede ser nula");
        this.fondoMazmorra = fondoMazmorra;
    }

    public ControladorVistaJuego(
        GestorRecursos gestorRecursos,
        String hojaEstilosUrl,
        double tamanoTile
    ) {
        Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        Objects.requireNonNull(hojaEstilosUrl, "La URL de la hoja de estilos no puede ser nula");
        Map<String, Image> imagenesHud = new HashMap<>();
        for (int indice = 0; indice < nombresImagenesHud.length; indice++) {
            imagenesHud.put(
                clavesImagenesHud[indice],
                gestorRecursos.getImagen(nombresImagenesHud[indice], Extension.Imagen.PNG)
            );
        }
        this.vista = new VistaJuego(Map.copyOf(imagenesHud), hojaEstilosUrl);
        ControladorFondoMazmorra controladorFondo = new ControladorFondoMazmorra(
            this.vista.getAreaJuego(),
            gestorRecursos,
            tamanoTile
        );
        this.fondoMazmorra = controladorFondo.getVista();
        this.vista.getAreaJuego().getChildren().add(this.fondoMazmorra);
    }

    public VistaJuego getVistaJuego() {
        return vista;
    }

    public FondoMazmorra getFondoMazmorra() {
        return fondoMazmorra;
    }

    public Node getVista() {
        return vista;
    }
}

