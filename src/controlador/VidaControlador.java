package controlador;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import modelo.EntidadViva;
import motor.util.VecDouble2D;

/** Representa una recuperación de vida que el jugador puede recoger. */
public class VidaControlador {

    private static final double RADIO_RECOGIDA = 42;

    private final Circle vista = new Circle(6, Color.CRIMSON);
    private final VecDouble2D posicion;
    private final int cantidad;
    private boolean recogida;

    public VidaControlador(VecDouble2D posicion, int cantidad) {
        if (posicion == null || cantidad <= 0) {
            throw new IllegalArgumentException("La recuperación debe tener posición y cantidad positiva");
        }
        this.posicion = posicion.clone();
        this.cantidad = cantidad;
        vista.setStroke(Color.WHITE);
        vista.setLayoutX(posicion.getX());
        vista.setLayoutY(posicion.getY());
    }

    public boolean actualizar(EntidadViva jugador) {
        if (recogida || jugador.getVida() >= jugador.getVidaMax()) {
            return false;
        }

        VecDouble2D jugadorPosicion = jugador.getPosicion();
        double dx = jugadorPosicion.getX() - posicion.getX();
        double dy = jugadorPosicion.getY() - posicion.getY();
        if (Math.sqrt(dx * dx + dy * dy) <= RADIO_RECOGIDA) {
            jugador.curar(cantidad);
            recogida = true;
            vista.setVisible(false);
            return true;
        }
        return false;
    }

    public boolean fueRecogida() {
        return recogida;
    }

    public Node getVista() {
        return vista;
    }
}
