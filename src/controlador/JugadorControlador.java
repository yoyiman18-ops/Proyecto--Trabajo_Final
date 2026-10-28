package controlador;

import java.util.Objects;
import java.util.function.DoubleSupplier;

import javafx.scene.Node;
import modelo.ModeloJugador;
import motor.entrada.Accion;
import motor.entrada.EstadoAcciones;
import motor.modelo.Direccion;
import motor.modelo.Entidad;
import motor.mvc.ControladorUsuario;
import motor.util.Eje;
import motor.util.observer.Observador;
import vista.VistaJugador;

public class JugadorControlador implements ControladorUsuario {

    public record LimitesArea(
        double margenHorizontal,
        double margenSuperior,
        double margenInferior
    ) {
        public LimitesArea {
            if (!Double.isFinite(margenHorizontal) || margenHorizontal < 0
                || !Double.isFinite(margenSuperior) || margenSuperior < 0
                || !Double.isFinite(margenInferior) || margenInferior < 0) {
                throw new IllegalArgumentException("Los márgenes deben ser finitos y no negativos.");
            }
        }
    }

    private final ModeloJugador modelo;
    private final VistaJugador vista;
    private final Observador<ModeloJugador> observadorVida;
    private final Observador<Entidad> observadorPosicion;
    private final Runnable accionAtaque;
    private EstadoAcciones estadoAcciones;
    private final Observador<EstadoAcciones> observadorAcciones;

    public JugadorControlador(
        ModeloJugador modelo,
        VistaJugador vista,
        DoubleSupplier anchoArea,
        DoubleSupplier altoArea,
        LimitesArea limites,
        Observador<ModeloJugador> observadorVida,
        Runnable accionAtaque
    ) {
        this.modelo = Objects.requireNonNull(modelo, "modelo no puede ser nulo");
        this.vista = Objects.requireNonNull(vista, "vista no puede ser nula");
        Objects.requireNonNull(anchoArea, "anchoArea no puede ser nulo");
        Objects.requireNonNull(altoArea, "altoArea no puede ser nulo");
        Objects.requireNonNull(limites, "limites no puede ser nulo");
        this.observadorVida = Objects.requireNonNull(observadorVida, "observadorVida no puede ser nulo");
        this.accionAtaque = Objects.requireNonNull(accionAtaque, "accionAtaque no puede ser nula");
        this.observadorAcciones = estado -> this.estadoAcciones = estado;
        this.observadorPosicion = entidad -> vista.actualizarPosicion(
            entidad.getPosicion().getX(),
            entidad.getPosicion().getY()
        );

        modelo.getEntidad().getNotificador().suscribirObservador(this.observadorPosicion);
        modelo.getNotificadorVida().suscribirObservador(this.observadorVida);
        this.observadorVida.cambio(modelo);
        vista.actualizarPosicion(modelo.getPosicion().getX(), modelo.getPosicion().getY());
    }

    @Override
    public void tick(Double dt) {
        Objects.requireNonNull(dt, "dt no puede ser nulo");
        if (!Double.isFinite(dt) || dt < 0) {
            throw new IllegalArgumentException("El delta tiempo debe ser finito y no negativo.");
        }

        if (estadoAcciones != null && estadoAcciones.activa(Accion.MOVER_ARRIBA)) {
            modelo.acelerar(Direccion.ARRIBA, dt);
        } else if (estadoAcciones != null && estadoAcciones.activa(Accion.MOVER_ABAJO)) {
            modelo.acelerar(Direccion.ABAJO, dt);
        } else {
            modelo.frenar(Eje.Y, dt);
        }

        if (estadoAcciones != null && estadoAcciones.activa(Accion.MOVER_DERECHA)) {
            modelo.acelerar(Direccion.DERECHA, dt);
        } else if (estadoAcciones != null && estadoAcciones.activa(Accion.MOVER_IZQUIERDA)) {
            modelo.acelerar(Direccion.IZQUIERDA, dt);
        } else {
            modelo.frenar(Eje.X, dt);
        }
        modelo.mover(dt);

        double vx = modelo.getVelocidad().getX();
        double vy = modelo.getVelocidad().getY();
        boolean enMovimiento = vx != 0 || vy != 0;
        Direccion direccionActual = enMovimiento ? direccionMovimiento(vx, vy) : modelo.getDireccion();
        vista.actualizarPosicion(modelo.getPosicion().getX(), modelo.getPosicion().getY());
        vista.actualizarAnimacion(direccionActual, enMovimiento, dt);

        if (estadoAcciones != null && estadoAcciones.activa(Accion.ATACAR)) {
            accionAtaque.run();
        }
    }

    private Direccion direccionMovimiento(double vx, double vy) {
        if (Math.abs(vx) > Math.abs(vy)) {
            return vx > 0 ? Direccion.DERECHA : Direccion.IZQUIERDA;
        }
        return vy > 0 ? Direccion.ABAJO : Direccion.ARRIBA;
    }

    @Override
    public Observador<EstadoAcciones> getObservadorAcciones() {
        return observadorAcciones;
    }

    @Override
    public Entidad getModelo() {
        return modelo.getEntidad();
    }

    @Override
    public Node getVista() {
        return vista.getNodo();
    }
}
