package controlador;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import javafx.scene.Node;
import modelo.ModeloProyectil;
import motor.colisiones.Colisionable;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import motor.util.observer.Observador;
import vista.VistaProyectil;

public final class ProyectilControlador implements Controlador {
    private final ModeloProyectil modelo;
    private final VistaProyectil vista;
    private final BiConsumer<Colisionable, Double> accionImpacto;
    private final Observador<Colisionable> observadorColision;
    private boolean impactado;

    public ProyectilControlador(
        ModeloProyectil modelo,
        VistaProyectil vista,
        Consumer<Colisionable> accionImpacto
    ) {
        this(
            modelo,
            vista,
            (colisionado, danio) -> Objects.requireNonNull(
                accionImpacto,
                "La acción de impacto no puede ser nula"
            ).accept(colisionado)
        );
    }

    public ProyectilControlador(
        ModeloProyectil modelo,
        VistaProyectil vista,
        BiConsumer<Colisionable, Double> accionImpacto
    ) {
        this.modelo = Objects.requireNonNull(modelo, "El modelo del proyectil no puede ser nulo");
        this.vista = Objects.requireNonNull(vista, "La vista del proyectil no puede ser nula");
        this.accionImpacto = Objects.requireNonNull(accionImpacto, "La acción de impacto no puede ser nula");
        this.observadorColision = this::alColisionar;
        this.modelo.getEntidad().getNotificadorColision().suscribirObservador(observadorColision);
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

    public double getDanio() {
        return modelo.getDanio();
    }

    private void alColisionar(Colisionable otro) {
        if (!estaActivo()) {
            return;
        }
        impactado = true;
        accionImpacto.accept(otro, modelo.getDanio());
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
