package controlador;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.image.Image;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import motor.recursos.Extension;
import motor.recursos.cache.CacheImagenes;
import vista.FondoMazmorra;
import vista.VistaJuego;

public final class ControladorVistaJuego implements Controlador {
    private final String[] nombresImagenesHud = {
        "led-heart-red", "led-heart", "hud_xp", "hud_gema_gris", "hud_gema_verde"
    };
    private final String[] clavesImagenesHud = {
        "corazonLleno", "corazonVacio", "iconoExperiencia",
        "gemaExperienciaVacia", "gemaExperienciaLlena"
    };
    private final VistaJuego vista;

    public ControladorVistaJuego(CacheImagenes cacheImagenes, double tamanoTile) {
        Map<String, Image> imagenesHud = new HashMap<>();
        for (int indice = 0; indice < nombresImagenesHud.length; indice++) {
            imagenesHud.put(
                clavesImagenesHud[indice],
                cacheImagenes.getRecurso(nombresImagenesHud[indice], Extension.Imagen.PNG));
        }
        vista = new VistaJuego(Map.copyOf(imagenesHud));
        ControladorFondoMazmorra controladorFondo =
            new ControladorFondoMazmorra(vista.getAreaJuego(), cacheImagenes, tamanoTile);
        vista.getAreaJuego().getChildren().add(controladorFondo.getVista());
    }

    public VistaJuego getVistaJuego() {
        return vista;
    }

    @Override
    public Entidad getModelo() {
        return null;
    }

    @Override
    public Node getVista() {
        return vista;
    }

    @Override
    public void tick(Double dt) {
    }
}
