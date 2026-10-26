package controlador;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.scene.Node;
import javafx.scene.image.Image;
import motor.modelo.graficos.Sheet;
import motor.persistencia.dao.DAOSoloLectura;
import motor.persistencia.dao.sqlite.Conexion;
import motor.persistencia.dao.sqlite.SheetDAOSQLite;
import motor.recursos.Extension;
import motor.recursos.GestorRecursos;
import vista.ConfiguracionMazmorra;
import vista.VistaFondo;
import vista.VistaHud;
import vista.VistaJuego;
import vista.VistaPartida;

public final class ControladorVistaJuego {

    public static final int ID_SHEET_HUD = 2;

    private static final Map<String, String> RECURSOS_ICONOS_HUD = Map.of(
        "corazonLleno", "0x72_DungeonTilesetII_v1.7/frames/ui_heart_full",
        "corazonVacio", "0x72_DungeonTilesetII_v1.7/frames/ui_heart_empty",
        "iconoExperiencia", "0x72_DungeonTilesetII_v1.7/frames/flask_big_blue",
        "gemaExperienciaVacia", "hud_gema_gris",
        "gemaExperienciaLlena", "hud_gema_verde",
        "iconoTiempo", "0x72_DungeonTilesetII_v1.7/frames/flask_big_yellow",
        "iconoOleada", "0x72_DungeonTilesetII_v1.7/frames/wall_banner_red",
        "iconoEnemigos", "0x72_DungeonTilesetII_v1.7/frames/skull",
        "iconoDanio", "0x72_DungeonTilesetII_v1.7/frames/weapon_red_magic_staff",
        "iconoPuntos", "0x72_DungeonTilesetII_v1.7/frames/coin_anim_f0"
    );

    private final VistaPartida vista;
    private final VistaFondo fondoMazmorra;

    public ControladorVistaJuego(VistaPartida vista) {
        this(vista, null);
    }

    public ControladorVistaJuego(VistaPartida vista, VistaFondo fondoMazmorra) {
        this.vista = Objects.requireNonNull(vista, "La vista del juego no puede ser nula");
        this.fondoMazmorra = fondoMazmorra;
    }

    public ControladorVistaJuego(
        GestorRecursos gestorRecursos,
        String hojaEstilosUrl,
        double tamanoTile
    ) {
        this(
            gestorRecursos,
            cargarSheetHudDesdeBaseDeDatos(gestorRecursos),
            hojaEstilosUrl,
            new ConfiguracionMazmorra(tamanoTile)
        );
    }

    public ControladorVistaJuego(
        GestorRecursos gestorRecursos,
        Sheet sheetHud,
        String hojaEstilosUrl,
        double tamanoTile
    ) {
        this(gestorRecursos, sheetHud, hojaEstilosUrl, new ConfiguracionMazmorra(tamanoTile));
    }

    public ControladorVistaJuego(
        GestorRecursos gestorRecursos,
        Sheet sheetHud,
        String hojaEstilosUrl,
        ConfiguracionMazmorra configuracionMazmorra
    ) {
        Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        Objects.requireNonNull(hojaEstilosUrl, "La URL de la hoja de estilos no puede ser nula");
        Objects.requireNonNull(configuracionMazmorra, "La configuración de mazmorra no puede ser nula");

        Map<String, Image> imagenesHud = new HashMap<>();
        for (Map.Entry<String, String> entrada : RECURSOS_ICONOS_HUD.entrySet()) {
            imagenesHud.put(
                entrada.getKey(),
                gestorRecursos.getImagen(entrada.getValue(), Extension.Imagen.PNG)
            );
        }
        Sheet recortesHud = sheetHud != null ? sheetHud : cargarSheetHudDesdeBaseDeDatos(gestorRecursos);
        if (recortesHud != null && recortesHud.getImagen() != null) {
            imagenesHud.put("hojaGuiRpg", recortesHud.getImagen());
        }

        VistaHud vistaHud = new VistaHud(imagenesHud, recortesHud);
        this.vista = new VistaJuego(vistaHud, hojaEstilosUrl);

        ControladorFondoMazmorra controladorFondo = new ControladorFondoMazmorra(
            this.vista.getAreaJuego(),
            gestorRecursos,
            configuracionMazmorra
        );
        this.fondoMazmorra = controladorFondo.getVista();
        this.vista.agregarNodoJuego(this.fondoMazmorra.getNodo());
    }

    public static Sheet cargarSheetHudDesdeBaseDeDatos(GestorRecursos gestorRecursos) {
        Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        DAOSoloLectura<Integer, Sheet> sheetDAO = new SheetDAOSQLite(
            Conexion.getInstancia().get(),
            gestorRecursos
        );
        return sheetDAO.cargar(ID_SHEET_HUD);
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
