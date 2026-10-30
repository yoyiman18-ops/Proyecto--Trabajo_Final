package vista;

import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import motor.util.VecDouble2D;
import motor.util.observer.Observador;

public class VistaImagen 
extends ImageView {
    
    private final Observador<Rectangle2D> observadorRectangulo;
    private final Observador<VecDouble2D> observadorPosicion;

    public VistaImagen(Image imagen) { this(imagen, true); }

    public VistaImagen(Image imagen, boolean visible) {
        this.setImage(imagen);
        this.setVisbilidad(visible);
        this.observadorRectangulo = this::setViewport;
        this.observadorPosicion = this::setPosicion;
    }

    public Observador<Rectangle2D> getObservadorRectangulo() { return this.observadorRectangulo; }
    public Observador<VecDouble2D> getObservadorPosicion() { return this.observadorPosicion; }
    public void setVisbilidad(boolean visible) { this.setVisible(visible); }
    public void setPosicion(VecDouble2D posicion) { setX(posicion.getX()); setY(posicion.getY()); }

}
