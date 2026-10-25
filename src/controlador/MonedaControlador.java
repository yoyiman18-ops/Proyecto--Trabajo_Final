package controlador;

import java.util.Objects;
import java.util.function.IntConsumer;

import javafx.scene.Node;
import modelo.ModeloJugador;
import modelo.ModeloMoneda;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import vista.VistaItem;

public final class MonedaControlador implements Controlador {
    private final ModeloJugador jugador;
    private final ModeloMoneda modelo;
    private final VistaItem vista;
    private final double distanciaRecoleccion;
    private final IntConsumer onRecoger;

    public MonedaControlador(
        ModeloJugador jugador,
        ModeloMoneda modelo,
        VistaItem vista,
        IntConsumer onRecoger
    ) {
        this(jugador, modelo, vista, 38, onRecoger);
    }

    public MonedaControlador(
        ModeloJugador jugador,
        ModeloMoneda modelo,
        VistaItem vista,
        double distanciaRecoleccion,
        IntConsumer onRecoger
    ) {
        this.jugador = Objects.requireNonNull(jugador, "El jugador no puede ser nulo");
        this.modelo = Objects.requireNonNull(modelo, "El modelo de la moneda no puede ser nulo");
        this.vista = Objects.requireNonNull(vista, "La vista de la moneda no puede ser nula");
        this.onRecoger = Objects.requireNonNull(onRecoger, "La acción al recoger no puede ser nula");
        if (!Double.isFinite(distanciaRecoleccion) || distanciaRecoleccion <= 0) {
            throw new IllegalArgumentException("La distancia de recolección debe ser positiva y finita.");
        }
        this.distanciaRecoleccion = distanciaRecoleccion;
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
        return modelo.estaRecogida();
    }

    @Override
    public void tick(Double dt) {
        if (modelo.estaRecogida() || jugador.getVida() <= 0) {
            return;
        }

        double diferenciaX = jugador.getPosicion().getX() + 25 - modelo.getPosicion().getX();
        double diferenciaY = jugador.getPosicion().getY() + 25 - modelo.getPosicion().getY();
        if (Math.hypot(diferenciaX, diferenciaY) <= distanciaRecoleccion && modelo.recoger()) {
            onRecoger.accept(modelo.getValor());
        }
    }

    private void actualizarVista() {
        vista.actualizarPosicion(modelo.getPosicion().getX(), modelo.getPosicion().getY());
    }
}
