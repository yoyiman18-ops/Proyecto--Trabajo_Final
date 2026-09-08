package vista;

import modelo.SpriteModelo;
import modelo.Vec2;
import motor.cache.CacheImagenes;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class SpriteVista extends Pane {

    private CacheImagenes cache;
    private ImageView imageView;

    public SpriteVista(CacheImagenes cache) {
        this.cache = cache;
        imageView = new ImageView();
        getChildren().add(imageView);
    }

    public void actualizar(SpriteModelo modelo) {
        Vec2 posicion = modelo.getPosicion();
        imageView.setImage(cache.getRecurso(modelo.getNombre()));
        imageView.setLayoutX(posicion.getX());
        imageView.setLayoutY(posicion.getY());
    }

}
