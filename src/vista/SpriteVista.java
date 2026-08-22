package vista;

import modelo.SpriteModelo;
import modelo.Vec2;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class SpriteVista extends Pane {

    private ImageView imageView;

    public SpriteVista() {

        imageView = new ImageView();
        getChildren().add(imageView);
    }

    public void actualizar(SpriteModelo modelo) {
        Vec2 posicion = modelo.getPosicion();
        imageView.setImage(modelo.getImagen());
        imageView.setLayoutX(posicion.getX());
        imageView.setLayoutY(posicion.getY());
    }

}
