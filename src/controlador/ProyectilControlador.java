package controlador;

import java.util.Objects;

import javafx.scene.Node;
import modelo.ModeloProyectil;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import vista.VistaItem;

public final class ProyectilControlador implements Controlador {
    private final ModeloProyectil modelo;
    private final VistaItem vista;

    public ProyectilControlador(ModeloProyectil modelo, VistaItem vista) {
        this.modelo = Objects.requireNonNull(modelo, "El modelo del proyectil no puede ser nulo");
        this.vista = Objects.requireNonNull(vista, "La vista del proyectil no puede ser nula");
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

    public boolean estaActivo() {
        return !modelo.estaExpirado();
    }

    @Override
    public void tick(Double dt) {
        if (modelo.estaExpirado()) {
            return;
        }
        modelo.actualizarTiempoVida(dt);
        if (!modelo.estaExpirado()) {
            modelo.mover(dt);
            actualizarVista();
        }
    }

    private void actualizarVista() {
        vista.actualizarPosicion(modelo.getPosicion().getX(), modelo.getPosicion().getY());
    }
}
