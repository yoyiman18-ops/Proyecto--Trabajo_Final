package vista;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.animation.AnimationTimer;

public final class FondoMazmorra extends Canvas {
    public static final double TAMANO_TILE = 32;
    public static final double MARGEN_HORIZONTAL_JUGABLE = TAMANO_TILE;
    public static final double MARGEN_SUPERIOR_JUGABLE = TAMANO_TILE * 3;
    public static final double MARGEN_INFERIOR_JUGABLE = TAMANO_TILE;
    private static final long DURACION_MARCO_FUENTE = 180_000_000L;
    private static final String RUTA_TILES =
        "/recursos/imagenes/0x72_DungeonTilesetII_v1.7/frames/";
    private static final Image[] SUELOS = {
        cargarTile("floor_1.png"),
        cargarTile("floor_2.png"),
        cargarTile("floor_3.png"),
        cargarTile("floor_4.png"),
        cargarTile("floor_5.png"),
        cargarTile("floor_6.png"),
        cargarTile("floor_7.png"),
        cargarTile("floor_8.png")
    };
    private static final Image PARED_SUPERIOR_IZQUIERDA = cargarTile("wall_top_left.png");
    private static final Image PARED_SUPERIOR = cargarTile("wall_top_mid.png");
    private static final Image PARED_SUPERIOR_DERECHA = cargarTile("wall_top_right.png");
    private static final Image PARED_IZQUIERDA = cargarTile("wall_left.png");
    private static final Image PARED = cargarTile("wall_mid.png");
    private static final Image PARED_DERECHA = cargarTile("wall_right.png");
    private static final Image ESQUINA_INFERIOR_IZQUIERDA = cargarTile("wall_edge_bottom_left.png");
    private static final Image ESQUINA_INFERIOR_DERECHA = cargarTile("wall_edge_bottom_right.png");
    private static final Image COLUMNA = cargarTile("column.png");
    private static final Image COLUMNA_CON_PARED = cargarTile("column_wall.png");
    private static final Image FUENTE_SUPERIOR = cargarTile("wall_fountain_top_2.png");
    private static final Image[] FUENTE_CENTRO = {
        cargarTile("wall_fountain_mid_red_anim_f0.png"),
        cargarTile("wall_fountain_mid_red_anim_f1.png"),
        cargarTile("wall_fountain_mid_red_anim_f2.png")
    };
    private static final Image[] FUENTE_BASE = {
        cargarTile("wall_fountain_basin_red_anim_f0.png"),
        cargarTile("wall_fountain_basin_red_anim_f1.png"),
        cargarTile("wall_fountain_basin_red_anim_f2.png")
    };
    private int marcoFuente;
    private long ultimaActualizacionFuente;
    private final AnimationTimer animacionFuente = new AnimationTimer() {
        @Override
        public void handle(long ahora) {
            if (ultimaActualizacionFuente == 0) {
                ultimaActualizacionFuente = ahora;
            } else if (ahora - ultimaActualizacionFuente >= DURACION_MARCO_FUENTE) {
                marcoFuente = (marcoFuente + 1) % FUENTE_CENTRO.length;
                ultimaActualizacionFuente = ahora;
                dibujar();
            }
        }
    };

    public FondoMazmorra(Pane areaJuego) {
        setMouseTransparent(true);
        setFocusTraversable(false);
        widthProperty().bind(areaJuego.widthProperty());
        heightProperty().bind(areaJuego.heightProperty());
        widthProperty().addListener((observable, anterior, nuevo) -> dibujar());
        heightProperty().addListener((observable, anterior, nuevo) -> dibujar());
        sceneProperty().addListener((observable, anterior, escena) -> {
            ultimaActualizacionFuente = 0;
            if (escena == null) {
                animacionFuente.stop();
            } else {
                animacionFuente.start();
            }
        });
        dibujar();
    }

    private void dibujar() {
        double ancho = getWidth();
        double alto = getHeight();
        if (ancho <= 0 || alto <= 0) {
            return;
        }

        GraphicsContext graficos = getGraphicsContext2D();
        graficos.setImageSmoothing(false);
        graficos.clearRect(0, 0, ancho, alto);

        int columnas = (int) Math.ceil(ancho / TAMANO_TILE);
        int filas = (int) Math.ceil(alto / TAMANO_TILE);
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                dibujarTile(
                    graficos,
                    SUELOS[tipoSuelo(fila, columna)],
                    columna * TAMANO_TILE,
                    fila * TAMANO_TILE);
            }
        }

        dibujarParedSuperior(graficos, columnas);
        dibujarParedesLaterales(graficos, columnas, filas);
        dibujarParedInferior(graficos, columnas, filas);
        dibujarDecoraciones(graficos, ancho, columnas);
    }

    private void dibujarParedSuperior(GraphicsContext graficos, int columnas) {
        for (int columna = 0; columna < columnas; columna++) {
            Image tile = columna == 0
                ? PARED_SUPERIOR_IZQUIERDA
                : columna == columnas - 1
                    ? PARED_SUPERIOR_DERECHA
                    : PARED_SUPERIOR;
            dibujarTile(graficos, tile, columna * TAMANO_TILE, 0);

            Image interior = columna == 0
                ? PARED_IZQUIERDA
                : columna == columnas - 1
                    ? PARED_DERECHA
                    : PARED;
            dibujarTile(graficos, interior, columna * TAMANO_TILE, TAMANO_TILE);
            dibujarTile(graficos, PARED, columna * TAMANO_TILE, TAMANO_TILE * 2);
        }
    }

    private void dibujarParedesLaterales(GraphicsContext graficos, int columnas, int filas) {
        if (columnas < 2) {
            return;
        }
        for (int fila = 3; fila < filas - 1; fila++) {
            double y = fila * TAMANO_TILE;
            dibujarTile(graficos, PARED_IZQUIERDA, 0, y);
            dibujarTile(graficos, PARED_DERECHA, (columnas - 1) * TAMANO_TILE, y);
        }
    }

    private void dibujarDecoraciones(GraphicsContext graficos, double ancho, int columnas) {
        dibujarTile(graficos, COLUMNA_CON_PARED, columnaEn(ancho * 0.25, columnas), 0,
            TAMANO_TILE, TAMANO_TILE * 3);
        dibujarTile(graficos, COLUMNA, columnaEn(ancho * 0.75, columnas), 0,
            TAMANO_TILE, TAMANO_TILE * 3);

        double fuenteX = Math.floor((columnas / 2.0) - 0.5) * TAMANO_TILE;
        dibujarTile(graficos, FUENTE_SUPERIOR, fuenteX, 0);
        dibujarTile(graficos, FUENTE_CENTRO[marcoFuente], fuenteX, TAMANO_TILE);
        dibujarTile(graficos, FUENTE_BASE[marcoFuente], fuenteX, TAMANO_TILE * 2);
    }

    private static double columnaEn(double posicion, int columnas) {
        int columna = (int) Math.floor(posicion / TAMANO_TILE);
        columna = Math.max(1, Math.min(columna, columnas - 2));
        return columna * TAMANO_TILE;
    }

    private void dibujarParedInferior(GraphicsContext graficos, int columnas, int filas) {
        if (filas < 2) {
            return;
        }
        int fila = filas - 1;
        for (int columna = 0; columna < columnas; columna++) {
            Image tile = columna == 0
                ? ESQUINA_INFERIOR_IZQUIERDA
                : columna == columnas - 1
                    ? ESQUINA_INFERIOR_DERECHA
                    : PARED;
            dibujarTile(graficos, tile, columna * TAMANO_TILE, fila * TAMANO_TILE);
        }
    }

    private static int tipoSuelo(int fila, int columna) {
        int patron = Math.floorMod(fila * 17 + columna * 31, 53);
        return patron < 7 ? patron + 1 : 0;
    }

    private static void dibujarTile(GraphicsContext graficos, Image tile, double x, double y) {
        graficos.drawImage(tile, x, y, TAMANO_TILE, TAMANO_TILE);
    }

    private static void dibujarTile(
        GraphicsContext graficos,
        Image tile,
        double x,
        double y,
        double ancho,
        double alto
    ) {
        graficos.drawImage(tile, x, y, ancho, alto);
    }

    private static Image cargarTile(String nombre) {
        var recurso = FondoMazmorra.class.getResource(RUTA_TILES + nombre);
        if (recurso == null) {
            throw new IllegalStateException("No se encontró el tile de mazmorra: " + nombre);
        }
        Image imagen = new Image(recurso.toExternalForm(), false);
        if (imagen.isError()) {
            throw new IllegalStateException("No se pudo cargar el tile de mazmorra: " + nombre, imagen.getException());
        }
        return imagen;
    }
}
