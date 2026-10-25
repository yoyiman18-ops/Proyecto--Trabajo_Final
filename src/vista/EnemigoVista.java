package vista;

import java.util.Objects;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class EnemigoVista extends ImageView implements VistaEnemigo {
    private final double duracionReaccionGolpe;
    private final Animacion animacionIdle;
    private final Image frameGolpe;
    private double tiempoReaccionGolpe;

    public EnemigoVista(Animacion animacionIdle, Image frameGolpe) {
        this(animacionIdle, frameGolpe, 48, 52, 32, 0.22);
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
        this.duracionReaccionGolpe = duracionReaccionGolpe;
        setFitWidth(ancho);
        setFitHeight(alto);
        setPreserveRatio(true);
        this.tamanoModelo = tamanoModelo;
        setImage(animacionIdle.actualizarSecuencia(false, 0));
    }

    private final double tamanoModelo;

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
                setImage(animacionIdle.actualizarSecuencia(false, 0));
            }
            return;
        }
        setImage(animacionIdle.actualizarSecuencia(true, dt));
    }

    @Override
    public void mostrarReaccionDanio(double proporcionVida) {
        if (!Double.isFinite(proporcionVida)) {
            throw new IllegalArgumentException("La proporción de vida debe ser finita.");
        }
        double proporcionValida = Math.max(0, Math.min(1, proporcionVida));
        setOpacity(0.55 + 0.45 * proporcionValida);
        tiempoReaccionGolpe = duracionReaccionGolpe;
        setImage(frameGolpe);
    }
}
