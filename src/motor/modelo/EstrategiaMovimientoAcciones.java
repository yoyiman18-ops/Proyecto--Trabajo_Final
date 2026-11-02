package motor.modelo;

import motor.entrada.Accion;
import motor.entrada.EstadoAcciones;
import motor.entrada.ReceptorEstadoAcciones;
import motor.util.Eje;
import motor.util.VecDouble2D;
import motor.util.observer.Observador;


public class EstrategiaMovimientoAcciones
implements EstrategiaMovimiento, Observador<EstadoAcciones>, ReceptorEstadoAcciones {

    private EstadoAcciones estadoAcciones;

    public EstrategiaMovimientoAcciones() {
        this(null);
    }

    public EstrategiaMovimientoAcciones(EstadoAcciones estadoInicial) {
        this.estadoAcciones = estadoInicial;
    }

    @Override
    public void cambio(EstadoAcciones nuevoEstado) {
        this.estadoAcciones = nuevoEstado;
    }

    @Override
    public Observador<EstadoAcciones> getObservadorAcciones() {
        return this;
    }

    @Override
    public void ejecutar(Móvil sujeto, double dt) {
        if (sujeto == null) { return; }

        VecDouble2D direccion = new VecDouble2D(0, 0);

        if (estadoAcciones != null) {
            if (estadoAcciones.activa(Accion.MOVER_ARRIBA))    { direccion.sumar(VecDouble2D.arriba()); }
            if (estadoAcciones.activa(Accion.MOVER_ABAJO))     { direccion.sumar(VecDouble2D.abajo()); }
            if (estadoAcciones.activa(Accion.MOVER_IZQUIERDA)) { direccion.sumar(VecDouble2D.izquierda()); }
            if (estadoAcciones.activa(Accion.MOVER_DERECHA))   { direccion.sumar(VecDouble2D.derecha()); }
        }

        if (direccion.getX() != 0 || direccion.getY() != 0) {
            sujeto.acelerar(direccion, dt);
        }

        // Si no hay componente en X (o se cancelan), frena en ese eje
        if (direccion.getX() == 0) {
            sujeto.frenar(Eje.X, dt);
        }

        // Si no hay componente en Y (o se cancelan), frena en ese eje
        if (direccion.getY() == 0) {
            sujeto.frenar(Eje.Y, dt);
        }
    }

    public EstadoAcciones getEstadoAcciones() {
        return this.estadoAcciones;
    }
}
