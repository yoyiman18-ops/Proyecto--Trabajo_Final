package vista;

import java.util.Map;
import java.util.Objects;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

/**
 * Renderizador de fondo de la mazmorra basado en JavaFX Canvas.
 * Dibuja de forma pasiva los tiles de suelo, paredes perimetrales y decoraciones
 * según la configuración paramétrica del escenario.
 */
public final class FondoMazmorra extends Canvas implements VistaFondo {
    private static final int CANTIDAD_MARCOS_FUENTE = 3;
    private static final double PROPORCION_COLUMNA_IZQUIERDA = 0.25;
    private static final double PROPORCION_COLUMNA_DERECHA = 0.75;

    // Parámetros de dispersión pseudoaleatoria para variación de suelo
    private static final int HASH_MODULO = 53;
    private static final int HASH_COEF_FILA = 17;
    private static final int HASH_COEF_COLUMNA = 31;
    private static final int UMBRAL_VARIACION_SUELO = 7;

    private final ConfiguracionMazmorra configuracion;
    private final Map<String, Image> imagenes;
    private int marcoFuente;
    private double tiempoAcumuladoFuente;

    public FondoMazmorra(
        Pane areaJuego,
        ConfiguracionMazmorra configuracion,
        Map<String, Image> imagenes
    ) {
        Objects.requireNonNull(areaJuego, "El área de juego no puede ser nula");
        this.configuracion = Objects.requireNonNull(configuracion, "La configuración de mazmorra no puede ser nula");
        Objects.requireNonNull(imagenes, "El mapa de imágenes no puede ser nulo");
        this.imagenes = Map.copyOf(imagenes);

        setMouseTransparent(true);
        setFocusTraversable(false);
        widthProperty().bind(areaJuego.widthProperty());
        heightProperty().bind(areaJuego.heightProperty());
        widthProperty().addListener((observable, anterior, nuevo) -> dibujar());
        heightProperty().addListener((observable, anterior, nuevo) -> dibujar());
        dibujar();
    }

    public FondoMazmorra(
        Pane areaJuego,
        double tamanoTile,
        double duracionMarcoFuenteSegundos,
        Map<String, Image> imagenes
    ) {
        this(
            areaJuego,
            new ConfiguracionMazmorra(
                tamanoTile,
                ConfiguracionMazmorra.ALTO_PARED_SUPERIOR_DEFAULT,
                ConfiguracionMazmorra.ALTO_PARED_INFERIOR_DEFAULT,
                ConfiguracionMazmorra.ANCHO_PARED_LATERAL_DEFAULT,
                duracionMarcoFuenteSegundos
            ),
            imagenes
        );
    }

    public FondoMazmorra(
        Pane areaJuego,
        double tamanoTile,
        long duracionMarcoFuenteNanosegundos,
        Map<String, Image> imagenes
    ) {
        this(areaJuego, tamanoTile, duracionMarcoFuenteNanosegundos / 1_000_000_000.0, imagenes);
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
        double duracion = configuracion.duracionMarcoFuenteSegundos();
        if (tiempoAcumuladoFuente >= duracion) {
            int avances = (int) (tiempoAcumuladoFuente / duracion);
            tiempoAcumuladoFuente %= duracion;
            setMarcoFuente((marcoFuente + avances) % CANTIDAD_MARCOS_FUENTE);
        }
    }

    public void setMarcoFuente(int marcoFuente) {
        int nuevoMarco = Math.floorMod(marcoFuente, CANTIDAD_MARCOS_FUENTE);
        if (this.marcoFuente != nuevoMarco) {
            this.marcoFuente = nuevoMarco;
            dibujar();
        }
    }

    @Override
    public double getMargenHorizontalJugable() {
        return configuracion.margenHorizontalJugable();
    }

    @Override
    public double getMargenSuperiorJugable() {
        return configuracion.margenSuperiorJugable();
    }

    @Override
    public double getMargenInferiorJugable() {
        return configuracion.margenInferiorJugable();
    }

    public ConfiguracionMazmorra getConfiguracion() {
        return configuracion;
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

        double tamanoTile = configuracion.tamanoTile();
        int columnas = (int) Math.ceil(ancho / tamanoTile);
        int filas = (int) Math.ceil(alto / tamanoTile);

        // 1. Suelo
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                dibujarTile(
                    graficos,
                    imagenes.get("floor_" + (tipoSuelo(fila, columna) + 1)),
                    columna * tamanoTile,
                    fila * tamanoTile
                );
            }
        }

        // 2. Estructuras perimetrales
        dibujarParedSuperior(graficos, columnas);
        dibujarParedesLaterales(graficos, columnas, filas);
        dibujarParedInferior(graficos, columnas, filas);

        // 3. Decoraciones arquitectónicas
        dibujarDecoraciones(graficos, ancho, columnas);
    }

    private void dibujarParedSuperior(GraphicsContext graficos, int columnas) {
        double tamanoTile = configuracion.tamanoTile();
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
        double tamanoTile = configuracion.tamanoTile();
        for (int fila = configuracion.altoParedSuperiorTiles(); fila < filas - configuracion.altoParedInferiorTiles(); fila++) {
            double y = fila * tamanoTile;
            dibujarTile(graficos, imagenes.get("wall_left"), 0, y);
            dibujarTile(graficos, imagenes.get("wall_right"), (columnas - 1) * tamanoTile, y);
        }
    }

    private void dibujarDecoraciones(GraphicsContext graficos, double ancho, int columnas) {
        double tamanoTile = configuracion.tamanoTile();
        dibujarTile(graficos, imagenes.get("column_wall"), columnaEn(ancho * PROPORCION_COLUMNA_IZQUIERDA, columnas), 0,
            tamanoTile, tamanoTile * configuracion.altoParedSuperiorTiles());
        dibujarTile(graficos, imagenes.get("column"), columnaEn(ancho * PROPORCION_COLUMNA_DERECHA, columnas), 0,
            tamanoTile, tamanoTile * configuracion.altoParedSuperiorTiles());

        double fuenteX = Math.floor((columnas / 2.0) - 0.5) * tamanoTile;
        dibujarTile(graficos, imagenes.get("wall_fountain_top_2"), fuenteX, 0);
        dibujarTile(graficos, imagenes.get("wall_fountain_mid_red_anim_f" + marcoFuente), fuenteX, tamanoTile);
        dibujarTile(graficos, imagenes.get("wall_fountain_basin_red_anim_f" + marcoFuente), fuenteX, tamanoTile * 2);
    }

    private double columnaEn(double posicion, int columnas) {
        double tamanoTile = configuracion.tamanoTile();
        int columna = (int) Math.floor(posicion / tamanoTile);
        columna = Math.max(1, Math.min(columna, columnas - 2));
        return columna * tamanoTile;
    }

    private void dibujarParedInferior(GraphicsContext graficos, int columnas, int filas) {
        if (filas < 2) {
            return;
        }
        double tamanoTile = configuracion.tamanoTile();
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
        int patron = Math.floorMod(fila * HASH_COEF_FILA + columna * HASH_COEF_COLUMNA, HASH_MODULO);
        return patron < UMBRAL_VARIACION_SUELO ? patron + 1 : 0;
    }

    private void dibujarTile(GraphicsContext graficos, Image tile, double x, double y) {
        if (tile != null) {
            graficos.drawImage(tile, x, y, configuracion.tamanoTile(), configuracion.tamanoTile());
        }
    }

    private void dibujarTile(
        GraphicsContext graficos,
        Image tile,
        double x,
        double y,
        double ancho,
        double alto
    ) {
        if (tile != null) {
            graficos.drawImage(tile, x, y, ancho, alto);
        }
    }
}
