package motor.modelo;

import java.util.Objects;
import motor.util.VecDouble2D;

/**
 * Estrategia de movimiento lineal que acelera continuamente en una dirección
 * fija hasta alcanzar la velocidad máxima del sujeto móvil.
 */
public class EstrategiaMovimientoLineal implements EstrategiaMovimiento {

    private VecDouble2D direccion;

    public EstrategiaMovimientoLineal(VecDouble2D direccion) {
        this.direccion = Objects.requireNonNull(direccion, "La dirección no puede ser nula").clone().normalizado();
    }

    public EstrategiaMovimientoLineal(Direccion direccion) {
        Objects.requireNonNull(direccion, "La dirección no puede ser nula");
        this.direccion = switch (direccion) {
            case ARRIBA -> VecDouble2D.arriba();
            case ABAJO -> VecDouble2D.abajo();
            case IZQUIERDA -> VecDouble2D.izquierda();
            case DERECHA -> VecDouble2D.derecha();
        };
    }

    @Override
    public void ejecutar(Móvil sujeto, double dt) {
        if (sujeto == null) { return; }
        VecDouble2D vel = sujeto.getVelocidad();
        double rapidezActual = Math.hypot(vel.getX(), vel.getY());
        if (rapidezActual < sujeto.getVelocidadMax()) {
            sujeto.acelerar(this.direccion, dt);
        }
    }

    public VecDouble2D getDireccion() {
        return this.direccion.clone();
    }

    public void setDireccion(VecDouble2D direccion) {
        this.direccion = Objects.requireNonNull(direccion, "La dirección no puede ser nula").clone().normalizado();
    }

    public void setDireccion(Direccion direccion) {
        Objects.requireNonNull(direccion, "La dirección no puede ser nula");
        this.direccion = switch (direccion) {
            case ARRIBA -> VecDouble2D.arriba();
            case ABAJO -> VecDouble2D.abajo();
            case IZQUIERDA -> VecDouble2D.izquierda();
            case DERECHA -> VecDouble2D.derecha();
        };
    }
}
