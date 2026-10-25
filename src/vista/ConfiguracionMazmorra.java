package vista;

/**
 * Configuración paramétrica del diseño y dimensiones de la mazmorra.
 * Centraliza las reglas de diseño del escenario (tamaño de tiles, altura de paredes
 * y límites jugables) para desacoplarlas del renderizador visual.
 */
public record ConfiguracionMazmorra(
    double tamanoTile,
    int altoParedSuperiorTiles,
    int altoParedInferiorTiles,
    int anchoParedLateralTiles,
    double duracionMarcoFuenteSegundos
) {
    public static final double DURACION_FUENTE_DEFAULT = 0.18;
    public static final int ALTO_PARED_SUPERIOR_DEFAULT = 3;
    public static final int ALTO_PARED_INFERIOR_DEFAULT = 1;
    public static final int ANCHO_PARED_LATERAL_DEFAULT = 1;

    public ConfiguracionMazmorra {
        if (!Double.isFinite(tamanoTile) || tamanoTile <= 0) {
            throw new IllegalArgumentException("El tamaño de tile debe ser positivo y finito.");
        }
        if (!Double.isFinite(duracionMarcoFuenteSegundos) || duracionMarcoFuenteSegundos <= 0) {
            throw new IllegalArgumentException("La duración del marco de fuente debe ser positiva y finita.");
        }
        if (altoParedSuperiorTiles < 0 || altoParedInferiorTiles < 0 || anchoParedLateralTiles < 0) {
            throw new IllegalArgumentException("La cantidad de tiles de pared no puede ser negativa.");
        }
    }

    public ConfiguracionMazmorra(double tamanoTile) {
        this(
            tamanoTile,
            ALTO_PARED_SUPERIOR_DEFAULT,
            ALTO_PARED_INFERIOR_DEFAULT,
            ANCHO_PARED_LATERAL_DEFAULT,
            DURACION_FUENTE_DEFAULT
        );
    }

    public double margenHorizontalJugable() {
        return tamanoTile * anchoParedLateralTiles;
    }

    public double margenSuperiorJugable() {
        return tamanoTile * altoParedSuperiorTiles;
    }

    public double margenInferiorJugable() {
        return tamanoTile * altoParedInferiorTiles;
    }
}
