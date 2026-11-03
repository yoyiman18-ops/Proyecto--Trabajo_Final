package modelo.experiencia;

import java.util.List;

import motor.colisiones.hitboxes.HitboxFactory;
import motor.modelo.EntidadFactory;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

public abstract class ExperienciaFactory extends EntidadFactory {
    private final double cantidadExp;

    public ExperienciaFactory(
        HitboxFactory hitboxFactory,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        double cantidadExp
    ) {
        super(
            hitboxFactory,
            posicion,
            velocidad != null ? velocidad.clone() : new VecDouble2D(),
            aceleracion,
            friccion,
            velocidadMax,
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision
        );
        if (!Double.isFinite(cantidadExp) || cantidadExp <= 0) {
            throw new IllegalArgumentException("La cantidad de experiencia debe ser finita y positiva.");
        }
        this.cantidadExp = cantidadExp;
    }

    public double getCantidadExp() {
        return this.cantidadExp;
    }

    @Override
    public abstract Experiencia get();
}
