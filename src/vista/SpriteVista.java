package vista;

import modelo.SpriteModelo;
import modelo.Vec2;
import motor.recursos.Extension;
import motor.recursos.GestorRecursos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.geometry.Rectangle2D;

public class SpriteVista extends Pane {

    private static final int CANTIDAD_CUADROS = 4;
    private static final long INTERVALO_CUADRO_NS = 125_000_000L;

    private final ImageView imageView;
    private String imagen;
    private String carpetaAnimacion;
    private String direccionAnimacion = "abajo";
    private String recursoActual;
    private int cuadroActual;
    private long ultimoCambioCuadro;

    public SpriteVista() {
        imageView = new ImageView();
        getChildren().add(imageView);
    }

    public SpriteVista(String imagen, Rectangle2D recorte, double ancho, double alto) {
        this.imagen = imagen;
        this.imageView = new ImageView();
        imageView.setViewport(recorte);
        imageView.setFitWidth(ancho);
        imageView.setFitHeight(alto);
        imageView.setPreserveRatio(true);
        mostrarImagen(imagen, Extension.Imagen.JPG);
        getChildren().add(imageView);
    }

    public SpriteVista(String carpetaAnimacion, double ancho, double alto) {
        if (carpetaAnimacion == null || carpetaAnimacion.isBlank()) {
            throw new IllegalArgumentException("La carpeta de animación no puede estar vacía");
        }
        this.imageView = new ImageView();
        this.carpetaAnimacion = carpetaAnimacion;
        imageView.setFitWidth(ancho);
        imageView.setFitHeight(alto);
        imageView.setPreserveRatio(true);
        setPrefSize(ancho, alto);
        mostrarCuadroAnimacion();
        getChildren().add(imageView);
    }

    public void actualizar(SpriteModelo modelo) {
        if (carpetaAnimacion != null) {
            actualizarAnimacion(modelo, 0, 0, false);
            return;
        }
        Vec2 posicion = modelo.getPosicion();
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

        long ahora = System.nanoTime();
        if (ahora - ultimoCambioCuadro >= INTERVALO_CUADRO_NS) {
            cuadroActual = (cuadroActual + 1) % CANTIDAD_CUADROS;
            ultimoCambioCuadro = ahora;
            mostrarCuadroAnimacion();
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

    private void mostrarCuadroAnimacion() {
        mostrarImagen(carpetaAnimacion + "/zorro_" + direccionAnimacion
                + "_" + cuadroActual, Extension.Imagen.PNG);
    }

    private void mostrarImagen(String nombre, Extension.Imagen extension) {
        if (!nombre.equals(recursoActual)) {
            imageView.setImage(GestorRecursos.getInstancia().getImagen(nombre, extension));
            recursoActual = nombre;
        }
    }

}
