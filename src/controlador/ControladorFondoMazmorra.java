package controlador;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import motor.recursos.Extension;
import motor.recursos.GestorRecursos;
import vista.FondoMazmorra;
import vista.VistaFondo;

public final class ControladorFondoMazmorra {
    private final double duracionMarcoFuente;
    private final String carpetaTiles;
    private final String[] nombresTiles;
    private final VistaFondo vista;

    public ControladorFondoMazmorra(VistaFondo vista) {
        this.duracionMarcoFuente = 0.18;
        this.carpetaTiles = "0x72_DungeonTilesetII_v1.7/frames/";
        this.nombresTiles = new String[] {
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
        this.vista = Objects.requireNonNull(vista, "La vista del fondo de mazmorra no puede ser nula");
    }

    public ControladorFondoMazmorra(Pane areaJuego, GestorRecursos gestorRecursos, double tamanoTile) {
        Objects.requireNonNull(areaJuego, "El área de juego no puede ser nula");
        Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        if (!Double.isFinite(tamanoTile) || tamanoTile <= 0) {
            throw new IllegalArgumentException("El tamaño de tile debe ser positivo y finito.");
        }
        this.duracionMarcoFuente = 0.18;
        this.carpetaTiles = "0x72_DungeonTilesetII_v1.7/frames/";
        this.nombresTiles = new String[] {
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
        Map<String, Image> imagenes = new HashMap<>();
        for (String nombre : nombresTiles) {
            imagenes.put(
                nombre,
                gestorRecursos.getImagen(carpetaTiles + nombre, Extension.Imagen.PNG)
            );
        }
        this.vista = new FondoMazmorra(areaJuego, tamanoTile, duracionMarcoFuente, Map.copyOf(imagenes));
    }

    public VistaFondo getVista() {
        return vista;
    }

    public void actualizar(double dt) {
        vista.actualizarAnimacion(dt);
    }
}

