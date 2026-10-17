package vista;

import java.util.Map;
import java.util.Objects;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public final class FondoMazmorra extends Canvas implements VistaFondo {
    private final int cantidadMarcosFuente = 3;
    private final double tamanoTile;
    private final double margenHorizontalJugable;
    private final double margenSuperiorJugable;
    private final double margenInferiorJugable;
    private final double duracionMarcoFuente;
    private final Map<String, Image> imagenes;
    private int marcoFuente;
    private double tiempoAcumuladoFuente;

    public FondoMazmorra(
        Pane areaJuego,
        double tamanoTile,
        long duracionMarcoFuenteNanosegundos,
        Map<String, Image> imagenes
    ) {
        this(areaJuego, tamanoTile, duracionMarcoFuenteNanosegundos / 1_000_000_000.0, imagenes);
    }

    public FondoMazmorra(
        Pane areaJuego,
        double tamanoTile,
        double duracionMarcoFuenteSegundos,
        Map<String, Image> imagenes
    ) {
        Objects.requireNonNull(areaJuego, "El área de juego no puede ser nula");
        Objects.requireNonNull(imagenes, "El mapa de imágenes no puede ser nulo");
        if (!Double.isFinite(tamanoTile) || tamanoTile <= 0) {
            throw new IllegalArgumentException("El tamaño de tile debe ser positivo y finito.");
        }
        if (!Double.isFinite(duracionMarcoFuenteSegundos) || duracionMarcoFuenteSegundos <= 0) {
            throw new IllegalArgumentException("La duración del marco de la fuente debe ser positiva y finita.");
        }
        this.tamanoTile = tamanoTile;
        this.margenHorizontalJugable = tamanoTile;
        this.margenSuperiorJugable = tamanoTile * 3;
        this.margenInferiorJugable = tamanoTile;
        this.duracionMarcoFuente = duracionMarcoFuenteSegundos;
        this.imagenes = Map.copyOf(imagenes);
        setMouseTransparent(true);
        setFocusTraversable(false);
        widthProperty().bind(areaJuego.widthProperty());
        heightProperty().bind(areaJuego.heightProperty());
        widthProperty().addListener((observable, anterior, nuevo) -> dibujar());
        heightProperty().addListener((observable, anterior, nuevo) -> dibujar());
        dibujar();
    }

    @Override
    public Node getNodo() {
        return this;
    }

    @Override
    public void actualizarAnimacion(double dt) {
        if (!Double.isFinite(dt) || dt < 0) {
            throw new IllegalArgumentException("El delta tiempo debe ser finito y no negativo.");
        }
        tiempoAcumuladoFuente += dt;
        if (tiempoAcumuladoFuente >= duracionMarcoFuente) {
            int avances = (int) (tiempoAcumuladoFuente / duracionMarcoFuente);
            tiempoAcumuladoFuente %= duracionMarcoFuente;
            setMarcoFuente((marcoFuente + avances) % cantidadMarcosFuente);
        }
    }

    public void setMarcoFuente(int marcoFuente) {
        int nuevoMarco = Math.floorMod(marcoFuente, cantidadMarcosFuente);
        if (this.marcoFuente != nuevoMarco) {
            this.marcoFuente = nuevoMarco;
            dibujar();
        }
    }

    @Override
    public double getMargenHorizontalJugable() {
        return margenHorizontalJugable;
    }

    @Override
    public double getMargenSuperiorJugable() {
        return margenSuperiorJugable;
    }

    @Override
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
