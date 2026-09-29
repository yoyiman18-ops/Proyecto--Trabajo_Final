package vista;

import motor.modelo.Entidad;
import motor.util.VecDouble2D;
import motor.util.observer.Observador;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class SpriteVista extends ImageView implements Observador<Entidad> {
    private final Animacion animacion;

    public SpriteVista(Image imagen, Rectangle2D recorte, double ancho, double alto) {
        super(imagen);
        this.animacion = null;
        setViewport(recorte);
        configurarTamaño(ancho, alto);
    }

    public SpriteVista(Image imagen, int columnas, int filas, double ancho, double alto) {
        super(imagen);
        this.animacion = new Animacion(imagen, columnas, filas);
        setViewport(animacion.getViewport());
        configurarTamaño(ancho, alto);
    }

    private void configurarTamaño(double ancho, double alto) {
        setFitWidth(ancho);
        setFitHeight(alto);
        setPreserveRatio(true);
    }

    public void actualizarAnimacion(int fila, boolean activa, double dt) {
        if (animacion != null) {
            setViewport(animacion.actualizar(fila, activa, dt));
        }
    }

    @Override 
    public void cambio(Entidad entidad) {
        VecDouble2D posicion = entidad.getPosicion();
        setLayoutX(posicion.getX());
        setLayoutY(posicion.getY());
    }

}
