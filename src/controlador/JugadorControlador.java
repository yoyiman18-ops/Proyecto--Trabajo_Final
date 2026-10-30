package controlador;

import java.util.Objects;
import java.util.function.DoubleSupplier;

import javafx.scene.Node;
import modelo.ModeloJugador;
import motor.entrada.Accion;
import motor.entrada.EstadoAcciones;
import motor.modelo.Direccion;
import motor.modelo.Entidad;
import motor.util.Eje;
import motor.util.VecDouble2D;
import motor.util.observer.Observador;
import vista.VistaImagen;

public class JugadorControlador {

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
    private final VistaImagen vista;
    private final DoubleSupplier anchoArea;
    private final DoubleSupplier altoArea;
    private final LimitesArea limites;
    private final Observador<ModeloJugador> observadorVida;
    private final Runnable accionAtaque;
    private EstadoAcciones estadoAcciones;
    private final Observador<EstadoAcciones> observadorAcciones;
    private boolean posicionInicializada;

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
        this.anchoArea = Objects.requireNonNull(anchoArea, "anchoArea no puede ser nulo");
        this.altoArea = Objects.requireNonNull(altoArea, "altoArea no puede ser nulo");
        this.limites = Objects.requireNonNull(limites, "limites no puede ser nulo");
        this.observadorVida = Objects.requireNonNull(observadorVida, "observadorVida no puede ser nulo");
        this.accionAtaque = Objects.requireNonNull(accionAtaque, "accionAtaque no puede ser nula");
        this.observadorAcciones = estado -> this.estadoAcciones = estado;

        modelo.getNotificadorVida().suscribirObservador(this.observadorVida);
        this.observadorVida.cambio(modelo);
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
        mantenerDentroDelArea();

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

    private void mantenerDentroDelArea() {
        double anchoAreaActual = anchoArea.getAsDouble();
        double altoAreaActual = altoArea.getAsDouble();
        if (!Double.isFinite(anchoAreaActual) || !Double.isFinite(altoAreaActual)
            || anchoAreaActual <= 0 || altoAreaActual <= 0) {
            return;
        }

        VecDouble2D posicion = modelo.getPosicion();
        double limiteIzquierdo = limites.margenHorizontal();
        double limiteSuperior = limites.margenSuperior();
        double limiteDerecho = Math.max(
            limiteIzquierdo,
            anchoAreaActual - limites.margenHorizontal() - vista.getAncho());
        double limiteInferior = Math.max(
            limiteSuperior,
            altoAreaActual - limites.margenInferior() - vista.getAlto());
        boolean centrar = !posicionInicializada;
        if (centrar) {
            posicion.setX((limiteIzquierdo + limiteDerecho) / 2);
            posicion.setY((limiteSuperior + limiteInferior) / 2);
            posicionInicializada = true;
        }

        double xAnterior = posicion.getX();
        double yAnterior = posicion.getY();
        double x = Math.max(limiteIzquierdo, Math.min(xAnterior, limiteDerecho));
        double y = Math.max(limiteSuperior, Math.min(yAnterior, limiteInferior));
        boolean fueraEnX = x != xAnterior;
        boolean fueraEnY = y != yAnterior;

        if (centrar || fueraEnX || fueraEnY) {
            posicion.setX(x);
            posicion.setY(y);
            if (fueraEnX) {
                modelo.getVelocidad().setX(0);
            }
            if (fueraEnY) {
                modelo.getVelocidad().setY(0);
            }
            modelo.getHitbox().actualizarTransformacion(posicion);
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
