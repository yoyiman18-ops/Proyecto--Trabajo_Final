package controlador;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.scene.Node;
import javafx.scene.image.Image;
import motor.recursos.Extension;
import motor.recursos.GestorRecursos;
import motor.util.graficos.SheetLibre;
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
        SheetLibre recortesHud = crearSheetLibreHud();
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

    private static SheetLibre crearSheetLibreHud() {
        SheetLibre sheet = new SheetLibre();
        sheet.addRecorte("placa_ancha", new Rectangle2D.Double(14, 127, 283, 54));
        sheet.addRecorte("placa_recta", new Rectangle2D.Double(39, 127, 232, 54));
        sheet.addRecorte("placa_brillante", new Rectangle2D.Double(14, 283, 283, 54));
        sheet.addRecorte("marco_icono", new Rectangle2D.Double(39, 488, 44, 44));
        sheet.addRecorte("marco_panel", new Rectangle2D.Double(118, 451, 80, 80));
        return sheet;
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


