package controlador;

import java.util.Objects;
import java.util.function.Consumer;

import javafx.scene.Node;
import modelo.ModeloProyectil;
import motor.colisiones.Colisionable;
import motor.controlador.ControladorEntidad;
import motor.modelo.Entidad;
import motor.util.Ticking;
import motor.util.observer.Observador;
import vista.VistaImagen;
import vista.VistaImagenFX;

public final class ProyectilControlador implements Ticking {
    private final ModeloProyectil modelo;
    private final VistaImagen vista;
    private final Consumer<Colisionable> accionImpacto;
    private final Observador<Colisionable> observadorColision;
    private boolean impactado;

    public ProyectilControlador(
        ModeloProyectil modelo,
        VistaImagen vista,
        Consumer<Colisionable> accionImpacto
    ) {
        this.modelo = Objects.requireNonNull(modelo, "El modelo del proyectil no puede ser nulo");
        this.vista = Objects.requireNonNull(vista, "La vista del proyectil no puede ser nula");
        this.accionImpacto = Objects.requireNonNull(accionImpacto, "La acción de impacto no puede ser nula");
        this.observadorColision = this::alColisionar;
        this.modelo.getNotificadorColision().suscribirObservador(observadorColision);
    }

    private void alColisionar(Colisionable otro) {
        if (!estaActivo()) {
            return;
        }
        impactado = true;
        accionImpacto.accept(otro);
    }

    public boolean estaActivo() {
        return !impactado && !modelo.estaExpirado();
    }

    @Override
    public void tick(double dt) {
        if (!estaActivo()) { return; }
        modelo.actualizarTiempoVida(dt);
        
        }
    }

}
