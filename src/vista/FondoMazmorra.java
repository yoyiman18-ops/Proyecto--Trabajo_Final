package vista;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.animation.AnimationTimer;
import java.util.Map;

public final class FondoMazmorra extends Canvas {
    private final double tamanoTile;
    private final double margenHorizontalJugable;
    private final double margenSuperiorJugable;
    private final double margenInferiorJugable;
    private final long duracionMarcoFuente;
    private final Map<String, Image> imagenes;
    private int marcoFuente;
    private long ultimaActualizacionFuente;
    private final AnimationTimer animacionFuente = new AnimationTimer() {
        @Override
        public void handle(long ahora) {
            if (ultimaActualizacionFuente == 0) {
                ultimaActualizacionFuente = ahora;
            } else if (ahora - ultimaActualizacionFuente >= duracionMarcoFuente) {
                marcoFuente = (marcoFuente + 1) % 3;
                ultimaActualizacionFuente = ahora;
                dibujar();
            }
        }
    };

    public FondoMazmorra(
        Pane areaJuego,
        double tamanoTile,
        long duracionMarcoFuente,
        Map<String, Image> imagenes
    ) {
        this.tamanoTile = tamanoTile;
        this.margenHorizontalJugable = tamanoTile;
        this.margenSuperiorJugable = tamanoTile * 3;
        this.margenInferiorJugable = tamanoTile;
        this.duracionMarcoFuente = duracionMarcoFuente;
        this.imagenes = Map.copyOf(imagenes);
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

    public double getMargenHorizontalJugable() {
        return margenHorizontalJugable;
    }

    public double getMargenSuperiorJugable() {
        return margenSuperiorJugable;
    }

    public double getMargenInferiorJugable() {
        return margenInferiorJugable;
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

        int columnas = (int) Math.ceil(ancho / tamanoTile);
        int filas = (int) Math.ceil(alto / tamanoTile);
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                dibujarTile(
                    graficos,
                    imagenes.get("floor_" + (tipoSuelo(fila, columna) + 1)),
                    columna * tamanoTile,
                    fila * tamanoTile);
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
                ? imagenes.get("wall_top_left")
                : columna == columnas - 1
                    ? imagenes.get("wall_top_right")
                    : imagenes.get("wall_top_mid");
            dibujarTile(graficos, tile, columna * tamanoTile, 0);

            Image interior = columna == 0
                ? imagenes.get("wall_left")
                : columna == columnas - 1
                    ? imagenes.get("wall_right")
                    : imagenes.get("wall_mid");
            dibujarTile(graficos, interior, columna * tamanoTile, tamanoTile);
            dibujarTile(graficos, imagenes.get("wall_mid"), columna * tamanoTile, tamanoTile * 2);
        }
    }

    private void dibujarParedesLaterales(GraphicsContext graficos, int columnas, int filas) {
        if (columnas < 2) {
            return;
        }
        for (int fila = 3; fila < filas - 1; fila++) {
            double y = fila * tamanoTile;
            dibujarTile(graficos, imagenes.get("wall_left"), 0, y);
            dibujarTile(graficos, imagenes.get("wall_right"), (columnas - 1) * tamanoTile, y);
        }
    }

    private void dibujarDecoraciones(GraphicsContext graficos, double ancho, int columnas) {
        dibujarTile(graficos, imagenes.get("column_wall"), columnaEn(ancho * 0.25, columnas), 0,
            tamanoTile, tamanoTile * 3);
        dibujarTile(graficos, imagenes.get("column"), columnaEn(ancho * 0.75, columnas), 0,
            tamanoTile, tamanoTile * 3);

        double fuenteX = Math.floor((columnas / 2.0) - 0.5) * tamanoTile;
        dibujarTile(graficos, imagenes.get("wall_fountain_top_2"), fuenteX, 0);
        dibujarTile(graficos, imagenes.get("wall_fountain_mid_red_anim_f" + marcoFuente), fuenteX, tamanoTile);
        dibujarTile(graficos, imagenes.get("wall_fountain_basin_red_anim_f" + marcoFuente), fuenteX, tamanoTile * 2);
    }

    private double columnaEn(double posicion, int columnas) {
        int columna = (int) Math.floor(posicion / tamanoTile);
        columna = Math.max(1, Math.min(columna, columnas - 2));
        return columna * tamanoTile;
    }

    private void dibujarParedInferior(GraphicsContext graficos, int columnas, int filas) {
        if (filas < 2) {
            return;
        }
        int fila = filas - 1;
        for (int columna = 0; columna < columnas; columna++) {
            Image tile = columna == 0
                ? imagenes.get("wall_edge_bottom_left")
                : columna == columnas - 1
                    ? imagenes.get("wall_edge_bottom_right")
                    : imagenes.get("wall_mid");
            dibujarTile(graficos, tile, columna * tamanoTile, fila * tamanoTile);
        }
    }

    private int tipoSuelo(int fila, int columna) {
        int patron = Math.floorMod(fila * 17 + columna * 31, 53);
        return patron < 7 ? patron + 1 : 0;
    }

    private void dibujarTile(GraphicsContext graficos, Image tile, double x, double y) {
        graficos.drawImage(tile, x, y, tamanoTile, tamanoTile);
    }

    private void dibujarTile(
        GraphicsContext graficos,
        Image tile,
        double x,
        double y,
        double ancho,
        double alto
    ) {
        graficos.drawImage(tile, x, y, ancho, alto);
    }

}
