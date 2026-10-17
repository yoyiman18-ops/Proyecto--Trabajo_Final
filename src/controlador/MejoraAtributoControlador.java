package controlador;

import javafx.scene.Node;
import modelo.ItemMejoraAtributo;
import modelo.ModeloJugador;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import vista.VistaItem;

public final class MejoraAtributoControlador implements Controlador {
    private final ModeloJugador jugador;
    private final ItemMejoraAtributo mejora;
    private final Entidad modelo;
    private final VistaItem vista;
    private final double distanciaRecoleccion;
    private boolean recogida;

    public MejoraAtributoControlador(
        ModeloJugador jugador,
        ItemMejoraAtributo mejora,
        Entidad modelo,
        VistaItem vista
    ) {
        this(jugador, mejora, modelo, vista, 38);
    }

    public MejoraAtributoControlador(
        ModeloJugador jugador,
        ItemMejoraAtributo mejora,
        Entidad modelo,
        VistaItem vista,
        double distanciaRecoleccion
    ) {
        if (jugador == null || mejora == null || modelo == null || vista == null
            || !Double.isFinite(distanciaRecoleccion) || distanciaRecoleccion <= 0) {
            throw new IllegalArgumentException(
                "El jugador, la mejora, el modelo, la vista y una distancia positiva son obligatorios."
            );
        }
        this.jugador = jugador;
        this.mejora = mejora;
        this.modelo = modelo;
        this.vista = vista;
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
            mejora.aplicar(jugador);
            recogida = true;
        }
    }

    private void actualizarVista() {
        vista.actualizarPosicion(modelo.getPosicion().getX(), modelo.getPosicion().getY());
    }
}

