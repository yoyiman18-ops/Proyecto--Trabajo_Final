package modelo.proyectil;

import java.util.List;

import motor.colisiones.hitboxes.HitboxFactory;
import motor.modelo.EntidadFactory;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

public abstract class ProyectilFactory extends EntidadFactory {
    private EstrategiaMovimiento estrategiaMovimiento;
    private double tiempoVidaAdicional;
    private int penetracionAdicional;
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
            estrategiasExpiracion,
            interaccionesColision
        );
        this.estrategiaMovimiento = estrategiaMovimiento;
        setTiempoVidaAdicional(tiempoVidaAdicional);
        setPenetracionAdicional(penetracionAdicional);
        setDañoAdicional(dañoAdicional);
    }

    public ProyectilFactory(
        HitboxFactory hitboxFactory,
        VecDouble2D posicion,
        EstrategiaMovimiento estrategiaMovimiento,
        double tiempoVidaAdicional,
        int penetracionAdicional,
        double dañoAdicional,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        this(
            hitboxFactory,
            posicion,
            1000.0,
            0.0,
            1000.0,
            estrategiaMovimiento,
            tiempoVidaAdicional,
            penetracionAdicional,
            dañoAdicional,
            estrategiasExpiracion,
            interaccionesColision
        );
    }

    public void setTiempoVidaAdicional(double tiempoVida) {
        if (!Double.isFinite(tiempoVida)) {
            throw new IllegalArgumentException("El tiempo de vida debe ser finito");
        }
        this.tiempoVidaAdicional = tiempoVida;
    }

    public void setPenetracionAdicional(int penetracion) {
        this.penetracionAdicional = Math.max(-1, penetracion);
    }

    public void setDañoAdicional(double daño) {
        if (!Double.isFinite(daño)) {
            throw new IllegalArgumentException("Daño debe ser finito");
        }
        this.dañoAdicional = Math.max(0.0, daño);
    }

    public void setEstrategiaMovimiento(EstrategiaMovimiento estrategiaMovimiento) {
        this.estrategiaMovimiento = estrategiaMovimiento;
    }

    public EstrategiaMovimiento getEstrategiaMovimiento() {
        return this.estrategiaMovimiento;
    }

    public double getDañoAdicional() { return this.dañoAdicional; }
    public double getTiempoVidaAdicional() { return this.tiempoVidaAdicional; }
    public int getPenetracionAdicional() { return this.penetracionAdicional; }

    @Override
    public abstract Proyectil get();
}
