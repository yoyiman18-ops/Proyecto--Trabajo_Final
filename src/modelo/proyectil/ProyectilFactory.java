package modelo.proyectil;

import java.util.List;

import motor.colisiones.hitboxes.HitboxFactory;
import motor.modelo.EntidadFactory;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

public abstract class ProyectilFactory extends EntidadFactory {
    private final double tiempoVidaAdicional;
    private final int penetracionAdicional;
    private double dañoAdicional;

    public ProyectilFactory(
        HitboxFactory hitboxFactory,
        VecDouble2D posicion,
        double aceleracion,
        double friccion,
        double velocidadMax,
        EstrategiaMovimiento estrategiaMovimiento,
        double tiempoVidaAdicional,
        int penetracionAdicional,
        double dañoAdicional,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(
            hitboxFactory,
            posicion,
            new VecDouble2D(0, 0),
            aceleracion,
            friccion,
            velocidadMax,
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision
        );
        if (!Double.isFinite(tiempoVidaAdicional)) {
            throw new IllegalArgumentException("El tiempo de vida debe ser finito");
        }
        this.tiempoVidaAdicional = tiempoVidaAdicional;
        this.penetracionAdicional = Math.max(-1, penetracionAdicional);
        setDañoAdicional(dañoAdicional);
    }

    public void setDañoAdicional(double daño) {
        if (!Double.isFinite(daño)) {
            throw new IllegalArgumentException("Daño debe ser finito");
        }
        this.dañoAdicional = Math.max(0.0, daño);
    }

    public double getDañoAdicional() { return this.dañoAdicional; }
    public double getTiempoVidaAdicional() { return this.tiempoVidaAdicional; }
    public int getPenetracionAdicional() { return this.penetracionAdicional; }

    @Override
    public abstract Proyectil get();
}
