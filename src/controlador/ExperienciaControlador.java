package controlador;

import javafx.scene.Node;
import modelo.ModeloExperiencia;
import modelo.ModeloJugador;
import motor.colisiones.Colisionable;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import motor.util.observer.Observador;
import vista.VistaItem;

public final class ExperienciaControlador implements Controlador {
    private final ModeloJugador jugador;
    private final ModeloExperiencia modelo;
    private final VistaItem vista;
    private final Observador<Colisionable> observadorColision;

    public ExperienciaControlador(ModeloJugador jugador, ModeloExperiencia modelo, VistaItem vista) {
        if (jugador == null || modelo == null || vista == null) {
            throw new IllegalArgumentException(
                "El jugador, el modelo de experiencia y la vista son obligatorios."
            );
        }
        this.jugador = jugador;
        this.modelo = modelo;
        this.vista = vista;
        this.observadorColision = this::alColisionar;
        this.modelo.getEntidad().getNotificadorColision().suscribirObservador(this.observadorColision);
        actualizarVista();
    }

    @Override
    public Entidad getModelo() {
        return modelo.getEntidad();
    }

    @Override
    public Node getVista() {
        return vista.getNodo();
    }

    public boolean fueRecogida() {
        return modelo.fueRecogida();
    }

    @Override
    public void tick(Double dt) {
        // La recolección se resuelve mediante eventos del sistema de colisiones del motor.
    }

    private void alColisionar(Colisionable otro) {
        if (otro == jugador.getEntidad()) {
            modelo.recoger(jugador);
        }
    }

    private void actualizarVista() {
        vista.actualizarPosicion(modelo.getPosicion().getX(), modelo.getPosicion().getY());
    }
}
