package modelo.proyectil;

import java.util.List;

import motor.colisiones.hitboxes.HitboxFactory;
import motor.modelo.EntidadFactory;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

public abstract class ProyectilFactory extends EntidadFactory {
    private double tiempoVida;
    private int penetracion;
    private double daño;

    public ProyectilFactory(
        HitboxFactory hitboxFactory,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double daño,
        double tiempoVida,
        int penetracion,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        this(
            hitboxFactory,
            posicion,
            velocidad,
            0.0,
            0.0,
            1000.0,
            estrategiasExpiracion,
            interaccionesColision,
            daño,
            tiempoVida,
            penetracion
        );
    }

    public ProyectilFactory(
        HitboxFactory hitboxFactory,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        double daño,
        double tiempoVida,
        int penetracion
    ) {
        super(
            hitboxFactory,
            posicion,
            velocidad,
            aceleracion,
            friccion,
            velocidadMax,
            estrategiasExpiracion,
            interaccionesColision
        );
        setTiempoVidaAdicional(tiempoVida);
        setPenetracionAdicional(penetracion);
        setDañoAdicional(daño);
    }

    public void setTiempoVidaAdicional(double tiempoVida) {
        if (!Double.isFinite(tiempoVida)) {
            throw new IllegalArgumentException("El tiempo de vida debe ser finito");
        }
        this.tiempoVida = Math.max(0,tiempoVida);
    }

    public void setPenetracionAdicional(int penetracion) {
        this.penetracion = Math.max(-1,penetracion);
    }

    public void setDañoAdicional(double daño) {
        if (!Double.isFinite(daño)) { throw new IllegalArgumentException("Daño debe ser finito"); }
        this.daño = Math.max(0.0, daño);
    }

    public double getDañoAdicional() { return this.daño; }
    public double getTiempoVidaAdicional() { return this.tiempoVida; }
    public int getPenetracionAdicional() { return this.penetracion; }


    @Override
    public abstract Proyectil get();

}
