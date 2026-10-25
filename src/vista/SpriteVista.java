package vista;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import motor.modelo.Direccion;

public class SpriteVista extends ImageView implements VistaJugador {

    public static final Function<Direccion, Integer> MAPEO_DIRECCION_ESTANDAR = dir -> {
        if (dir == null) return 0;
        return switch (dir) {
            case ABAJO -> 0;
            case ARRIBA -> 1;
            case DERECHA -> 2;
            case IZQUIERDA -> 3;
        };
    };

    private final Animacion animacion;
    private final Function<Direccion, Integer> mapeoDireccion;

    public SpriteVista(Image imagen, Rectangle2D recorte, double ancho, double alto) {
        super(imagen);
        this.animacion = null;
        this.mapeoDireccion = MAPEO_DIRECCION_ESTANDAR;
        setViewport(recorte);
        configurarTamano(ancho, alto);
    }

    public SpriteVista(Image imagen, int columnas, int filas, double ancho, double alto) {
        this(imagen, columnas, filas, ancho, alto, MAPEO_DIRECCION_ESTANDAR);
    }

    public SpriteVista(
        Image imagen,
        int columnas,
        int filas,
        double ancho,
        double alto,
        Function<Direccion, Integer> mapeoDireccion
    ) {
        super(imagen);
        this.animacion = new Animacion(imagen, columnas, filas);
        this.mapeoDireccion = Objects.requireNonNull(mapeoDireccion, "El mapeo de dirección no puede ser nulo");
        setViewport(animacion.getViewport());
        configurarTamano(ancho, alto);
    }

    public SpriteVista(List<Image> frames, double ancho, double alto) {
        this(frames, Animacion.DURACION_FRAME_DEFAULT, ancho, alto);
    }

    public SpriteVista(List<Image> frames, double duracionFrame, double ancho, double alto) {
        super();
        this.animacion = new Animacion(frames, duracionFrame);
        this.mapeoDireccion = MAPEO_DIRECCION_ESTANDAR;
        setImage(animacion.getImagenActual());
        configurarTamano(ancho, alto);
    }

    private void configurarTamano(double ancho, double alto) {
        if (!Double.isFinite(ancho) || ancho <= 0 || !Double.isFinite(alto) || alto <= 0) {
            throw new IllegalArgumentException("Las dimensiones del sprite deben ser positivas y finitas.");
        }
        setFitWidth(ancho);
        setFitHeight(alto);
        setPreserveRatio(true);
    }

    public void actualizarAnimacion(int fila, boolean activa, double dt) {
        if (animacion != null) {
            animacion.aplicar(this, fila, activa, dt);
        }
    }

    public void actualizarAnimacion(boolean activa, double dt) {
        if (animacion != null) {
            animacion.aplicar(this, activa, dt);
        }
    }

    @Override
    public Node getNodo() {
        return this;
    }

    @Override
    public double getAncho() {
        return getLayoutBounds().getWidth();
    }

    @Override
    public double getAlto() {
        return getLayoutBounds().getHeight();
    }

    @Override
    public void actualizarPosicion(double x, double y) {
        setLayoutX(x);
        setLayoutY(y);
    }

    @Override
    public void actualizarAnimacion(Direccion direccion, boolean activa, double dt) {
        if (animacion == null) {
            return;
        }
        int fila = mapeoDireccion.apply(direccion);
        animacion.aplicar(this, fila, activa, dt);
    }
}
