package vista;

import modelo.SpriteModelo;
import modelo.Vec2;
import motor.cache.CacheImagenes;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.geometry.Rectangle2D;

public class SpriteVista extends Pane {

    private CacheImagenes cache;
    private ImageView imageView;
    private String recurso;
    private Rectangle2D recorte;

    public SpriteVista(CacheImagenes cache) {
        this.cache = cache;
        imageView = new ImageView();
        getChildren().add(imageView);
    }

    public SpriteVista(CacheImagenes cache, String recurso, Rectangle2D recorte,
                       double ancho, double alto) {
        this(cache);
        this.recurso = recurso;
        this.recorte = recorte;
        imageView.setViewport(recorte);
        imageView.setFitWidth(ancho);
        imageView.setFitHeight(alto);
        imageView.setPreserveRatio(true);
    }

    public void actualizar(SpriteModelo modelo) {
        Vec2 posicion = modelo.getPosicion();
        String nombreRecurso = recurso == null ? modelo.getNombre() : recurso;
        imageView.setImage(cache.getRecurso(nombreRecurso));
        imageView.setLayoutX(posicion.getX());
        imageView.setLayoutY(posicion.getY());
    }

}
