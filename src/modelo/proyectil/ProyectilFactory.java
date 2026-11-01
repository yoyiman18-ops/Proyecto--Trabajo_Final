package modelo.proyectil;

import java.util.ArrayList;
import java.util.List;

import modelo.EstrategiaExpiracion;
import motor.colisiones.hitboxes.HitboxFactory;
import motor.modelo.EntidadFactory;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

public abstract class ProyectilFactory extends EntidadFactory {
    private double daño;
    private double tiempoVida;
    private int penetracion;

    public ProyectilFactory(HitboxFactory hitboxFactory) {
        super(hitboxFactory);
        this.daño = 1.0;
        this.tiempoVida = 1.0;
        this.penetracion = 1;
    }

    public ProyectilFactory(
        int idGlobal,
        String nombre,
        HitboxFactory hitboxFactory,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double daño,
        double tiempoVida,
        int penetracion,
        List<EstrategiaExpiracion> estrategiasExpiracion
    ) {
        this(
            idGlobal,
            nombre,
            hitboxFactory,
            posicion,
            velocidad,
            0.0,
            0.0,
            0.0,
            estrategiasExpiracion,
            new ArrayList<>(),
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
        setDaño(daño);
        setTiempoVida(tiempoVida);
        setPenetracion(penetracion);
    }

    // ==========================================
    // Setters Protegidos
    // ==========================================

    protected void setDaño(double daño) {
        if (!Double.isFinite(daño) || daño < 0) {
            throw new IllegalArgumentException("El daño debe ser finito y no negativo.");
        }
        this.daño = daño;
    }

    protected void setTiempoVida(double tiempoVida) {
        if (!Double.isFinite(tiempoVida) || tiempoVida <= 0) {
            throw new IllegalArgumentException("El tiempo de vida debe ser positivo y finito.");
        }
        this.tiempoVida = tiempoVida;
    }

    protected void setPenetracion(int penetracion) {
        this.penetracion = penetracion;
    }

    // ==========================================
    // Getters Protegidos
    // ==========================================

    protected double getDaño() { return this.daño; }
    protected double getTiempoVida() { return this.tiempoVida; }
    protected int getPenetracion() { return this.penetracion; }

    // ==========================================
    // Métodos de Fabricación
    // ==========================================

    @Override
    public abstract Proyectil getEntidad();

    /**
     * Instancia un proyectil en la posición indicada.
     */
    public Proyectil crearProyectil(VecDouble2D posicion) {
        setPosicion(posicion);
        return getEntidad();
    }

    /**
     * Instancia un proyectil con la posición y velocidad especificadas.
     */
    public Proyectil crearProyectil(VecDouble2D posicion, VecDouble2D velocidad) {
        setPosicion(posicion);
        setVelocidad(velocidad);
        return getEntidad();
    }
}
