package controlador;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.scene.Node;
import javafx.scene.image.Image;
import motor.recursos.Extension;
import motor.recursos.GestorRecursos;
import vista.VistaFondo;
import vista.VistaJuego;
import vista.VistaPartida;

public final class ControladorVistaJuego {
    private final String[] nombresImagenesHud;
    private final String[] clavesImagenesHud;
    private final VistaPartida vista;
    private final VistaFondo fondoMazmorra;

    public ControladorVistaJuego(VistaPartida vista) {
        this(vista, null);
    }

    public ControladorVistaJuego(VistaPartida vista, VistaFondo fondoMazmorra) {
        this.nombresImagenesHud = new String[] {
            "led-heart-red", "led-heart", "hud_xp", "hud_gema_gris", "hud_gema_verde"
        };
        this.clavesImagenesHud = new String[] {
            "corazonLleno", "corazonVacio", "iconoExperiencia",
            "gemaExperienciaVacia", "gemaExperienciaLlena"
        };
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
        this.nombresImagenesHud = new String[] {
            "led-heart-red", "led-heart", "hud_xp", "hud_gema_gris", "hud_gema_verde"
        };
        this.clavesImagenesHud = new String[] {
            "corazonLleno", "corazonVacio", "iconoExperiencia",
            "gemaExperienciaVacia", "gemaExperienciaLlena"
        };
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
        this.vista.agregarNodoJuego(this.fondoMazmorra.getNodo());
    }

    public VistaPartida getVistaJuego() {
        return vista;
    }

    public VistaFondo getFondoMazmorra() {
        return fondoMazmorra;
    }

    public Node getVista() {
        return vista.getRaiz();
    }
}

