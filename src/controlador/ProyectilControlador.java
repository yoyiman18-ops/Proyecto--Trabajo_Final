package controlador;

import java.util.Objects;

import javafx.scene.Node;
import modelo.ModeloProyectil;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import vista.VistaItem;
import vista.VistaProyectil;

public final class ProyectilControlador implements Controlador {
    private final ModeloProyectil modelo;
    private final VistaProyectil vista;
    private boolean impactado;

    public ProyectilControlador(ModeloProyectil modelo, VistaProyectil vista) {
        this.modelo = Objects.requireNonNull(modelo, "El modelo del proyectil no puede ser nulo");
        this.vista = Objects.requireNonNull(vista, "La vista del proyectil no puede ser nula");
        actualizarVista();
    }

    public ProyectilControlador(ModeloProyectil modelo, VistaItem vista) {
        this(
            modelo,
            adaptarVistaItem(Objects.requireNonNull(vista, "La vista del proyectil no puede ser nula"))
        );
    }

    private static VistaProyectil adaptarVistaItem(VistaItem vista) {
        if (vista instanceof VistaProyectil vistaProyectil) {
            return vistaProyectil;
        }
        return new VistaProyectil() {
            @Override
            public Node getNodo() {
                return vista.getNodo();
            }

            @Override
            public void actualizarPosicion(double x, double y) {
                vista.actualizarPosicion(x, y);
            }

            @Override
            public void actualizarAnimacion(double dt) {
            }
        };
    }

    @Override
    public Entidad getModelo() {
        return modelo.getEntidad();
    }

    @Override
    public Node getVista() {
        return vista.getNodo();
    }

    public double getDanio() {
        return modelo.getDanio();
    }

    public void registrarImpacto() {
        this.impactado = true;
    }

    public boolean estaActivo() {
        return !impactado && !modelo.estaExpirado();
    }

    @Override
    public void tick(Double dt) {
        if (!estaActivo()) {
            return;
        }
        modelo.actualizarTiempoVida(dt);
        if (estaActivo()) {
            modelo.mover(dt);
            vista.actualizarAnimacion(dt);
            actualizarVista();
        }
    }

    private void actualizarVista() {
        vista.actualizarPosicion(modelo.getPosicion().getX(), modelo.getPosicion().getY());
    }
}

