package vista;

import java.util.Objects;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class EnemigoVista extends ImageView implements VistaEnemigo {
    public static final double ANCHO_DEFAULT = 48;
    public static final double ALTO_DEFAULT = 52;
    public static final double TAMANO_MODELO_DEFAULT = 32;
    public static final double DURACION_REACCION_GOLPE_DEFAULT = 0.22;
    public static final double OPACIDAD_BASE = 0.55;
    public static final double FACTOR_OPACIDAD_VIDA = 0.45;

    private final Animacion animacionIdle;
    private final Image frameGolpe;
    private final double tamanoModelo;
    private final double duracionReaccionGolpe;
    private double tiempoReaccionGolpe;

    public EnemigoVista(Animacion animacionIdle, Image frameGolpe) {
        this(animacionIdle, frameGolpe, ANCHO_DEFAULT, ALTO_DEFAULT, TAMANO_MODELO_DEFAULT, DURACION_REACCION_GOLPE_DEFAULT);
    }

    public EnemigoVista(
        Animacion animacionIdle,
        Image frameGolpe,
        double ancho,
        double alto,
        double tamanoModelo,
        double duracionReaccionGolpe
    ) {
        this.animacionIdle = Objects.requireNonNull(animacionIdle, "La animación idle no puede ser nula");
        this.frameGolpe = Objects.requireNonNull(frameGolpe, "El frame de golpe no puede ser nulo");
        if (!Double.isFinite(ancho) || ancho <= 0
            || !Double.isFinite(alto) || alto <= 0
            || !Double.isFinite(tamanoModelo) || tamanoModelo <= 0
            || !Double.isFinite(duracionReaccionGolpe) || duracionReaccionGolpe <= 0) {
            throw new IllegalArgumentException("Las dimensiones y la duración deben ser positivas y finitas.");
        }
        this.tamanoModelo = tamanoModelo;
        this.duracionReaccionGolpe = duracionReaccionGolpe;
        setFitWidth(ancho);
        setFitHeight(alto);
        setPreserveRatio(true);
        animacionIdle.aplicar(this, false, 0);
    }

    @Override
    public Node getNodo() {
        return this;
    }

    @Override
    public void actualizarPosicion(double x, double y) {
        setLayoutX(x - (getFitWidth() - tamanoModelo) / 2);
        setLayoutY(y - (getFitHeight() - tamanoModelo) / 2);
    }

    @Override
    public void actualizarAnimacion(double dt) {
        if (!Double.isFinite(dt) || dt < 0) {
            throw new IllegalArgumentException("El delta tiempo debe ser finito y no negativo.");
        }
        if (tiempoReaccionGolpe > 0) {
            tiempoReaccionGolpe = Math.max(0, tiempoReaccionGolpe - dt);
            if (tiempoReaccionGolpe == 0) {
                animacionIdle.aplicar(this, false, 0);
            }
            return;
        }
        animacionIdle.aplicar(this, true, dt);
    }

    @Override
    public void mostrarReaccionDanio(double proporcionVida) {
        if (!Double.isFinite(proporcionVida)) {
            throw new IllegalArgumentException("La proporción de vida debe ser finita.");
        }
        double proporcionValida = Math.max(0, Math.min(1, proporcionVida));
        setOpacity(OPACIDAD_BASE + FACTOR_OPACIDAD_VIDA * proporcionValida);
        tiempoReaccionGolpe = duracionReaccionGolpe;
        setImage(frameGolpe);
        setViewport(null);
    }
}
