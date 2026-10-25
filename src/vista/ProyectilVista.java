package vista;

import java.util.Objects;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class ProyectilVista extends ImageView implements VistaProyectil {
    private final Animacion animacion;
    private final int filaAnimacion;
    private final double tamanoModelo;

    public ProyectilVista(Animacion animacion) {
        this(animacion, 28, 28, 16);
    }

    public ProyectilVista(
        Animacion animacion,
        double ancho,
        double alto,
        double tamanoModelo
    ) {
        this.animacion = Objects.requireNonNull(animacion, "La animación del proyectil no puede ser nula");
        this.filaAnimacion = 0;
        if (!Double.isFinite(ancho) || ancho <= 0
            || !Double.isFinite(alto) || alto <= 0
            || !Double.isFinite(tamanoModelo) || tamanoModelo <= 0) {
            throw new IllegalArgumentException("Las dimensiones del proyectil deben ser positivas y finitas.");
        }
        this.tamanoModelo = tamanoModelo;
        setFitWidth(ancho);
        setFitHeight(alto);
        setPreserveRatio(true);
        if (animacion.esSecuenciaImagenes()) {
            setImage(animacion.actualizarSecuencia(false, 0));
        } else {
            throw new IllegalArgumentException(
                "Para una hoja de sprites se debe indicar la imagen base en el constructor."
            );
        }
    }

    public ProyectilVista(
        Image hojaSprites,
        Animacion animacion,
        double ancho,
        double alto,
        double tamanoModelo
    ) {
        this(hojaSprites, animacion, 0, ancho, alto, tamanoModelo);
    }

    public ProyectilVista(
        Image hojaSprites,
        Animacion animacion,
        int filaAnimacion,
        double ancho,
        double alto,
        double tamanoModelo
    ) {
        super(Objects.requireNonNull(hojaSprites, "La hoja de sprites no puede ser nula"));
        this.animacion = Objects.requireNonNull(animacion, "La animación del proyectil no puede ser nula");
        if (filaAnimacion < 0) {
            throw new IllegalArgumentException("La fila de animación no puede ser negativa.");
        }
        if (!Double.isFinite(ancho) || ancho <= 0
            || !Double.isFinite(alto) || alto <= 0
            || !Double.isFinite(tamanoModelo) || tamanoModelo <= 0) {
            throw new IllegalArgumentException("Las dimensiones del proyectil deben ser positivas y finitas.");
        }
        this.filaAnimacion = filaAnimacion;
        this.tamanoModelo = tamanoModelo;
        setFitWidth(ancho);
        setFitHeight(alto);
        setPreserveRatio(true);
        if (animacion.esSecuenciaImagenes()) {
            setImage(animacion.actualizarSecuencia(false, 0));
        } else {
            setViewport(animacion.actualizar(filaAnimacion, true, 0));
        }
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
        if (animacion.esSecuenciaImagenes()) {
            setImage(animacion.actualizarSecuencia(true, dt));
        } else {
            setViewport(animacion.actualizar(filaAnimacion, true, dt));
        }
    }
}
