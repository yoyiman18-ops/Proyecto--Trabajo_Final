package vista;

import modelo.SpriteModelo;
import modelo.Vec2;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import main.CacheRecursos;

public class SpriteVista extends Pane {

    private CacheRecursos cacheRecursos;
    private ImageView imageView;

    public SpriteVista(CacheRecursos cacheRecursos) {
        this.cacheRecursos = cacheRecursos;
        imageView = new ImageView();
        getChildren().add(imageView);
    }

    private String getPathRecurso(SpriteModelo modelo) {
        System.out.println("/recursos/" + modelo.getId() + ".jpg");
        return getClass().getResource("/recursos/" + modelo.getId() + ".jpg").toExternalForm();
    }

    public void actualizar(SpriteModelo modelo) {
        Vec2 posicion = modelo.getPosicion();
        imageView.setImage(cacheRecursos.getImagen(getPathRecurso(modelo)));
        imageView.setLayoutX(posicion.getX());
        imageView.setLayoutY(posicion.getY());
    }

}
