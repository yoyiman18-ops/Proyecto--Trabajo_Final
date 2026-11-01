package modelo.proyectil;

import java.util.List;
import java.util.Objects;

import motor.colisiones.hitboxes.HitboxFactory;
import motor.modelo.EntidadFactory;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

public abstract class ProyectilFactory extends EntidadFactory {
    private VecDouble2D direccion;
    private double velocidad;
    private double tiempoVida;
    private int penetracion;
    private double daño;
    private String animacionExpirar;

    public ProyectilFactory(
        HitboxFactory hitboxFactory,
        VecDouble2D posicion,
        VecDouble2D direccion,
        double velocidad,
        double daño,
        double tiempoVida,
        int penetracion,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        this(
            hitboxFactory,
            posicion,
            direccion,
            velocidad,
            daño,
            tiempoVida,
            penetracion,
            estrategiasExpiracion,
            interaccionesColision,
            "expirar"
        );
    }

    public ProyectilFactory(
        HitboxFactory hitboxFactory,
        VecDouble2D posicion,
        VecDouble2D direccion,
        double velocidad,
        double daño,
        double tiempoVida,
        int penetracion,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        String animacionExpirar
    ) {
        this(
            hitboxFactory,
            posicion,
            direccion,
            velocidad,
            0.0,
            0.0,
            1000.0,
            estrategiasExpiracion,
            interaccionesColision,
            daño,
            tiempoVida,
            penetracion,
            animacionExpirar
        );
    }

    public ProyectilFactory(
        HitboxFactory hitboxFactory,
        VecDouble2D posicion,
        VecDouble2D direccion,
        double velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        double daño,
        double tiempoVida,
        int penetracion
    ) {
        this(
            hitboxFactory,
            posicion,
            direccion,
            velocidad,
            aceleracion,
            friccion,
            velocidadMax,
            estrategiasExpiracion,
            interaccionesColision,
            daño,
            tiempoVida,
            penetracion,
            "expirar"
        );
    }

    public ProyectilFactory(
        HitboxFactory hitboxFactory,
        VecDouble2D posicion,
        VecDouble2D direccion,
        double velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        double daño,
        double tiempoVida,
        int penetracion,
        String animacionExpirar
    ) {
        super(
            hitboxFactory,
            posicion,
            direccion != null ? direccion.clone().normalizar().producto(velocidad) : new VecDouble2D(),
            aceleracion,
            friccion,
            velocidadMax,
            estrategiasExpiracion,
            interaccionesColision
        );
        this.direccion = direccion != null ? direccion.clone() : new VecDouble2D();
        this.velocidad = velocidad;
        setTiempoVidaAdicional(tiempoVida);
        setPenetracionAdicional(penetracion);
        setDañoAdicional(daño);
        setAnimacionExpirar(animacionExpirar);
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

    public void setAnimacionExpirar(String animacionExpirar) {
        this.animacionExpirar = animacionExpirar;
    }

    public void setDireccion(VecDouble2D direccion) {
        this.direccion = Objects.requireNonNull(direccion, "La dirección no puede ser nula").clone();
        super.setVelocidad(this.direccion.clone().normalizar().producto(this.velocidad));
    }

    public void setVelocidad(double velocidad) {
        this.velocidad = velocidad;
        if (this.direccion != null) {
            super.setVelocidad(this.direccion.clone().normalizar().producto(this.velocidad));
        }
    }

    public VecDouble2D getDireccion() { return this.direccion != null ? this.direccion.clone() : new VecDouble2D(); }
    public double getVelocidadDouble() { return this.velocidad; }

    public double getDañoAdicional() { return this.daño; }
    public double getTiempoVidaAdicional() { return this.tiempoVida; }
    public int getPenetracionAdicional() { return this.penetracion; }
    public String getAnimacionExpirar() { return this.animacionExpirar; }

    @Override
    public abstract Proyectil get();

}
