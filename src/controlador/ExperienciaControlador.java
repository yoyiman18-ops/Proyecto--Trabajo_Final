package controlador;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import modelo.EntidadMovil;
import modelo.Vec2;

/** Representa la experiencia que deja un enemigo y permite recogerla. */
public class ExperienciaControlador {

    private static final double RADIO_RECOGIDA = 42;

    private final Circle vista = new Circle(6, Color.GOLD);
    private final Vec2 posicion;
    private final int cantidad;
    private boolean recogida;

    public ExperienciaControlador(Vec2 posicion, int cantidad) {
        if (posicion == null || cantidad <= 0) {
            throw new IllegalArgumentException("La experiencia debe tener posición y cantidad positiva");
        }
        this.posicion = posicion.clone();
        this.cantidad = cantidad;
        actualizarVista();
    }

    public boolean actualizar(EntidadMovil jugador) {
        if (recogida) {
            return false;
        }
        Vec2 jugadorPosicion = jugador.getPosicion();
        double dx = jugadorPosicion.getX() - posicion.getX();
        double dy = jugadorPosicion.getY() - posicion.getY();
        if (Math.sqrt(dx * dx + dy * dy) <= RADIO_RECOGIDA) {
            recogida = true;
            vista.setVisible(false);
            return true;
        }
        return false;
    }

    private void actualizarVista() {
        vista.setLayoutX(posicion.getX());
        vista.setLayoutY(posicion.getY());
    }

    public int getCantidad() {
        return cantidad;
    }

    public boolean fueRecogida() {
        return recogida;
    }

    public Node getVista() {
        return vista;
    }
}
