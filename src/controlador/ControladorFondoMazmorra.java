package controlador;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import motor.recursos.Extension;
import motor.recursos.cache.CacheImagenes;
import vista.FondoMazmorra;

public final class ControladorFondoMazmorra implements Controlador {
    private final long duracionMarcoFuente = 180_000_000L;
    private final String carpetaTiles = "0x72_DungeonTilesetII_v1.7/frames/";
    private final String[] nombresTiles = {
        "floor_1", "floor_2", "floor_3", "floor_4",
        "floor_5", "floor_6", "floor_7", "floor_8",
        "wall_top_left", "wall_top_mid", "wall_top_right",
        "wall_left", "wall_mid", "wall_right",
        "wall_edge_bottom_left", "wall_edge_bottom_right",
        "column", "column_wall", "wall_fountain_top_2",
        "wall_fountain_mid_red_anim_f0", "wall_fountain_mid_red_anim_f1",
        "wall_fountain_mid_red_anim_f2", "wall_fountain_basin_red_anim_f0",
        "wall_fountain_basin_red_anim_f1", "wall_fountain_basin_red_anim_f2"
    };
    private final FondoMazmorra vista;

    public ControladorFondoMazmorra(Pane areaJuego, CacheImagenes cacheImagenes, double tamanoTile) {
        Map<String, Image> imagenes = new HashMap<>();
        for (String nombre : nombresTiles) {
            imagenes.put(
                nombre,
                cacheImagenes.getRecurso(carpetaTiles + nombre, Extension.Imagen.PNG));
        }
        vista = new FondoMazmorra(areaJuego, tamanoTile, duracionMarcoFuente, imagenes);
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
