package vista;

import java.util.Objects;

import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public final class ProyectilVista extends ImageView implements VistaProyectil {
    public static final double ANCHO_DEFAULT = 28;
    public static final double ALTO_DEFAULT = 28;
    public static final double TAMANO_MODELO_DEFAULT = 16;
    public static final int FILA_ANIMACION_DEFAULT = 0;

    private final Animacion animacion;
    private final int filaAnimacion;
    private final double tamanoModelo;

    public ProyectilVista(Animacion animacion) {
        this(animacion, FILA_ANIMACION_DEFAULT, ANCHO_DEFAULT, ALTO_DEFAULT, TAMANO_MODELO_DEFAULT);
    }

    public ProyectilVista(
        Animacion animacion,
        double ancho,
        double alto,
        double tamanoModelo
    ) {
        this(animacion, FILA_ANIMACION_DEFAULT, ancho, alto, tamanoModelo);
    }

    public ProyectilVista(
        Image hojaSprites,
        Animacion animacion,
        double ancho,
        double alto,
        double tamanoModelo
    ) {
        this(hojaSprites, animacion, FILA_ANIMACION_DEFAULT, ancho, alto, tamanoModelo);
    }

    public ProyectilVista(
        Animacion animacion,
        int filaAnimacion,
        double ancho,
        double alto,
        double tamanoModelo
    ) {
        this(Objects.requireNonNull(animacion, "La animación del proyectil no puede ser nula").getHoja(),
             animacion, filaAnimacion, ancho, alto, tamanoModelo);
    }

    public ProyectilVista(
        Image hojaSprites,
        Animacion animacion,
        int filaAnimacion,
        double ancho,
        double alto,
        double tamanoModelo
    ) {
        super(hojaSprites);
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
        this.animacion.aplicar(this, filaAnimacion, false, 0);
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
        animacion.aplicar(this, filaAnimacion, true, dt);
    }
}
