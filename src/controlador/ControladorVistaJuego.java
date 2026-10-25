package controlador;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.scene.Node;
import javafx.scene.image.Image;
import motor.modelo.graficos.Recorte;
import motor.modelo.graficos.Sheet;
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
        this.nombresImagenesHud = crearNombresImagenesHud();
        this.clavesImagenesHud = crearClavesImagenesHud();
        this.vista = Objects.requireNonNull(vista, "La vista del juego no puede ser nula");
        this.fondoMazmorra = fondoMazmorra;
    }

    public ControladorVistaJuego(
        GestorRecursos gestorRecursos,
        String hojaEstilosUrl,
        double tamanoTile
    ) {
        this(gestorRecursos, crearSheetHud(gestorRecursos), hojaEstilosUrl, tamanoTile);
    }

    public ControladorVistaJuego(
        GestorRecursos gestorRecursos,
        Sheet sheetHud,
        String hojaEstilosUrl,
        double tamanoTile
    ) {
        Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        Objects.requireNonNull(hojaEstilosUrl, "La URL de la hoja de estilos no puede ser nula");
        this.nombresImagenesHud = crearNombresImagenesHud();
        this.clavesImagenesHud = crearClavesImagenesHud();
        Map<String, Image> imagenesHud = new HashMap<>();
        for (int indice = 0; indice < nombresImagenesHud.length; indice++) {
            imagenesHud.put(
                clavesImagenesHud[indice],
                gestorRecursos.getImagen(nombresImagenesHud[indice], Extension.Imagen.PNG)
            );
        }
        Sheet recortesHud = sheetHud != null ? sheetHud : crearSheetHud(gestorRecursos);
        this.vista = new VistaJuego(Map.copyOf(imagenesHud), recortesHud, hojaEstilosUrl);
        ControladorFondoMazmorra controladorFondo = new ControladorFondoMazmorra(
            this.vista.getAreaJuego(),
            gestorRecursos,
            tamanoTile
        );
        this.fondoMazmorra = controladorFondo.getVista();
        this.vista.agregarNodoJuego(this.fondoMazmorra.getNodo());
    }

    private static String[] crearNombresImagenesHud() {
        return new String[] {
            "0x72_DungeonTilesetII_v1.7/frames/ui_heart_full",
            "0x72_DungeonTilesetII_v1.7/frames/ui_heart_empty",
            "0x72_DungeonTilesetII_v1.7/frames/flask_big_blue",
            "hud_gema_gris",
            "hud_gema_verde",
            "rpg_gui_v1/RPG_GUI_v1",
            "0x72_DungeonTilesetII_v1.7/frames/flask_big_yellow",
            "0x72_DungeonTilesetII_v1.7/frames/wall_banner_red",
            "0x72_DungeonTilesetII_v1.7/frames/skull",
            "0x72_DungeonTilesetII_v1.7/frames/weapon_red_magic_staff",
            "0x72_DungeonTilesetII_v1.7/frames/coin_anim_f0"
        };
    }

    private static String[] crearClavesImagenesHud() {
        return new String[] {
            "corazonLleno",
            "corazonVacio",
            "iconoExperiencia",
            "gemaExperienciaVacia",
            "gemaExperienciaLlena",
            "hojaGuiRpg",
            "iconoTiempo",
            "iconoOleada",
            "iconoEnemigos",
            "iconoDanio",
            "iconoPuntos"
        };
    }

    public static Sheet crearSheetHud(GestorRecursos gestorRecursos) {
        String path = "rpg_gui_v1/RPG_GUI_v1";
        Extension.Imagen extension = Extension.Imagen.PNG;
        Image imagen = gestorRecursos != null ? gestorRecursos.getImagen(path, extension) : null;
        int idSheet = 1;
        Map<String, Recorte> recortes = new HashMap<>();
        recortes.put("placa_ancha", new Recorte(idSheet, "placa_ancha", 14, 127, 283, 54));
        recortes.put("placa_recta", new Recorte(idSheet, "placa_recta", 39, 127, 232, 54));
        recortes.put("placa_brillante", new Recorte(idSheet, "placa_brillante", 14, 283, 283, 54));
        recortes.put("marco_icono", new Recorte(idSheet, "marco_icono", 39, 488, 44, 44));
        recortes.put("marco_panel", new Recorte(idSheet, "marco_panel", 118, 451, 80, 80));
        return new Sheet(idSheet, path, imagen, extension, Collections.emptyMap(), recortes);
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
