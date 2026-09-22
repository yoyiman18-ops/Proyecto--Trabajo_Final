package vista;

import modelo.SpriteModelo;
import modelo.Vec2;
import motor.cache.CacheImagenes;
import motor.cache.Extension;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.geometry.Rectangle2D;

public class SpriteVista extends Pane {

    private ImageView imageView;
    private String imagen;
    private Rectangle2D recorte;

    public SpriteVista() {
        imageView = new ImageView();
        getChildren().add(imageView);
    }

    public SpriteVista(String imagen, Rectangle2D recorte,
                       double ancho, double alto) {
        this.imagen = imagen;
        this.recorte = recorte;
        imageView.setViewport(recorte);
        imageView.setFitWidth(ancho);
        imageView.setFitHeight(alto);
        imageView.setPreserveRatio(true);
    }

    public void actualizar(SpriteModelo modelo) {
        Vec2 posicion = modelo.getPosicion();
        String nombreImagen = imagen == null ? modelo.getNombre() : imagen;
        imageView.setImage(
            CacheImagenes.getInstancia().getRecurso(nombreImagen, Extension.Imagen.PNG));
        imageView.setLayoutX(posicion.getX());
        imageView.setLayoutY(posicion.getY());
    }

}
