package controlador;

import javafx.scene.Node;
import modelo.ModeloJugador;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import vista.VistaItem;

public final class ExperienciaControlador implements Controlador {
    private final ModeloJugador jugador;
    private final Entidad modelo;
    private final VistaItem vista;
    private final int cantidad;
    private final double distanciaRecoleccion;
    private boolean recogida;

    public ExperienciaControlador(ModeloJugador jugador, Entidad modelo, VistaItem vista, int cantidad) {
        this(jugador, modelo, vista, cantidad, 38);
    }

    public ExperienciaControlador(
        ModeloJugador jugador,
        Entidad modelo,
        VistaItem vista,
        int cantidad,
        double distanciaRecoleccion
    ) {
        if (jugador == null || modelo == null || vista == null || cantidad <= 0
            || !Double.isFinite(distanciaRecoleccion) || distanciaRecoleccion <= 0) {
            throw new IllegalArgumentException(
                "El jugador, modelo, vista, una cantidad positiva de XP y una distancia positiva son obligatorios."
            );
        }
        this.jugador = jugador;
        this.modelo = modelo;
        this.vista = vista;
        this.cantidad = cantidad;
        this.distanciaRecoleccion = distanciaRecoleccion;
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
        if (recogida || jugador.getVida() <= 0) {
            return;
        }

        double diferenciaX = jugador.getPosicion().getX() + 25 - modelo.getPosicion().getX();
        double diferenciaY = jugador.getPosicion().getY() + 25 - modelo.getPosicion().getY();
        if (Math.hypot(diferenciaX, diferenciaY) <= distanciaRecoleccion) {
            jugador.ganarExperiencia(cantidad);
            recogida = true;
        }
    }

    private void actualizarVista() {
        vista.actualizarPosicion(modelo.getPosicion().getX(), modelo.getPosicion().getY());
    }
}

