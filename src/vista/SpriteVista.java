package vista;


import motor.recursos.Extension;
import motor.recursos.GestorRecursos;
import motor.util.VecDouble2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.geometry.Rectangle2D;

public class SpriteVista extends ImageView {

    private final Image imagen;
    private int cuadroActual;

    public SpriteVista(Image imagen) {
    }

    public SpriteVista(Image imagen, Rectangle2D recorte, double ancho, double alto) {
        this.imagen = imagen;
        this.setViewport(recorte);
        this.setFitWidth(ancho);
        this.setFitHeight(alto);
        this.setPreserveRatio(true);
    }

    public void actualizar(SpriteModelo modelo) {
        VecDouble2D posicion = modelo.getPosicion();
        String nombreImagen = imagen == null ? modelo.getNombre() : imagen;
        mostrarImagen(nombreImagen, Extension.Imagen.JPG);
        imageView.setLayoutX(posicion.getX());
        imageView.setLayoutY(posicion.getY());
    }

    public void actualizarAnimacion(SpriteModelo modelo, double direccionX,
                                    double direccionY, boolean moviendo) {
        if (carpetaAnimacion == null) {
            actualizar(modelo);
            return;
        }

        Vec2 posicion = modelo.getPosicion();
        imageView.setLayoutX(posicion.getX());
        imageView.setLayoutY(posicion.getY());

        String nuevaDireccion = obtenerDireccion(direccionX, direccionY);
        if (nuevaDireccion != null && !nuevaDireccion.equals(direccionAnimacion)) {
            direccionAnimacion = nuevaDireccion;
            cuadroActual = 0;
            ultimoCambioCuadro = System.nanoTime();
            mostrarCuadroAnimacion();
        }

        if (!moviendo) {
            if (cuadroActual != 0) {
                cuadroActual = 0;
                mostrarCuadroAnimacion();
            }
            ultimoCambioCuadro = System.nanoTime();
            return;
        }

    }

    private String obtenerDireccion(double x, double y) {
        if (x == 0 && y == 0) {
            return null;
        }
        if (Math.abs(x) > Math.abs(y)) {
            return x > 0 ? "derecha" : "izquierda";
        }
        return y > 0 ? "abajo" : "arriba";
    }

}
