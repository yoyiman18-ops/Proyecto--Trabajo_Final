package controlador;

import javafx.scene.Node;
import modelo.ModeloJugador;
import motor.colisiones.Colisionable;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import motor.util.observer.Observador;
import vista.VistaItem;

public final class ExperienciaControlador implements ControladorFX {
    private final ModeloJugador jugador;
    private final Entidad modelo;
    private final VistaItem vista;
    private final int cantidad;
    private final Observador<Colisionable> observadorColision;
    private boolean recogida;

    public ExperienciaControlador(ModeloJugador jugador, Entidad modelo, VistaItem vista, int cantidad) {
        if (jugador == null || modelo == null || vista == null || cantidad <= 0) {
            throw new IllegalArgumentException(
                "El jugador, modelo, vista y una cantidad positiva de XP son obligatorios."
            );
        }
        this.jugador = jugador;
        this.modelo = modelo;
        this.vista = vista;
        this.cantidad = cantidad;
        this.observadorColision = this::alColisionar;
        this.modelo.getNotificadorColision().suscribirObservador(this.observadorColision);
        actualizarVista();
    }

    @Override
    public Entidad getModelo() {
        return modelo;
    }

    @Override
    public Node getVista() {
        return vista.getNodo();
    }

    public boolean fueRecogida() {
        return recogida;
    }

    @Override
    public void tick(Double dt) {
        // La recolección se resuelve mediante eventos del sistema de colisiones del motor.
    }

    private void alColisionar(Colisionable otro) {
        if (recogida || jugador.getVida() <= 0) {
            return;
        }
        if (otro == jugador.getEntidad()) {
            jugador.ganarExperiencia(cantidad);
            recogida = true;
        }
    }

    private void actualizarVista() {
        vista.actualizarPosicion(modelo.getPosicion().getX(), modelo.getPosicion().getY());
    }
}
