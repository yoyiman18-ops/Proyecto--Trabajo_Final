package modelo.proyectil;

import java.util.List;

import modelo.EstrategiaExpiracion;
import motor.colisiones.hitboxes.HitboxFactory;
import motor.modelo.EntidadFactory;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

public abstract class ProyectilFactory extends EntidadFactory {
    private double tiempoVida;
    private int penetracion;
    private double daño;

    public ProyectilFactory(
        int idGlobal,
        String nombre,
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
            idGlobal,
            nombre,
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
        int idGlobal,
        String nombre,
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
            idGlobal,
            nombre,
            hitboxFactory,
            posicion,
            velocidad,
            aceleracion,
            friccion,
            velocidadMax,
            estrategiasExpiracion,
            interaccionesColision
        );
        setTiempoVida(tiempoVida);
        setPenetracion(penetracion);
        setDaño(daño);
    }

    public void setTiempoVida(double tiempoVida) {
        if (!Double.isFinite(tiempoVida) || tiempoVida <= 0) {
            throw new IllegalArgumentException("El tiempo de vida debe ser positivo y finito.");
        }
        this.tiempoVida = tiempoVida;
    }

    public void setPenetracion(int penetracion) {
        this.penetracion = penetracion;
    }

    public void setDaño(double daño) {
        if (!Double.isFinite(daño)) { throw new IllegalArgumentException("Daño debe ser finito"); }
        this.daño = Math.max(0.0, daño);
    }

    public double getDaño() { return this.daño; }
    public double getTiempoVida() { return this.tiempoVida; }
    public int getPenetracion() { return this.penetracion; }


    @Override
    public abstract Proyectil get();

}
