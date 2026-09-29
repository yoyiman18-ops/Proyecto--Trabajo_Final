package vista;

import motor.modelo.Entidad;
import motor.util.VecDouble2D;
import motor.util.observer.Observador;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;

public class SpriteVista extends ImageView implements Observador<Entidad> {

    private final Image imagen;
    private int cuadroActual;

    public SpriteVista(Image imagen, Rectangle2D recorte, double ancho, double alto) {
        this.imagen = imagen;
        this.setViewport(recorte);
        this.setFitWidth(ancho);
        this.setFitHeight(alto);
        this.setPreserveRatio(true);
    }

    @Override 
    public void cambio(Entidad entidad) {
        VecDouble2D posicion = entidad.getPosicion();
        setLayoutX(posicion.getX());
        setLayoutY(posicion.getY());
    }

}
