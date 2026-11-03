package motor.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import motor.colisiones.hitboxes.HitboxFactory;
import motor.util.VecDouble2D;

public abstract class EntidadFactory {
    private final HitboxFactory hitboxFactory;
    private VecDouble2D posicion;
    private VecDouble2D velocidad;
    private double aceleracion;
    private double friccion;
    private double velocidadMax;
    private EstrategiaMovimiento estrategiaMovimiento;
    private List<EstrategiaExpiracion> estrategiasExpiracion;
    private List<Interaccion> interaccionesColision;

    public EntidadFactory(
        HitboxFactory hitboxFactory,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        this.hitboxFactory = Objects.requireNonNull(hitboxFactory, "HitboxFactory no puede ser nula.");
        this.posicion = posicion != null ? posicion : new VecDouble2D();
        this.velocidad = velocidad != null ? velocidad.clone() : new VecDouble2D();
        this.aceleracion = aceleracion;
        this.friccion = friccion;
        this.velocidadMax = velocidadMax;
        this.estrategiaMovimiento = estrategiaMovimiento;
        this.estrategiasExpiracion = estrategiasExpiracion != null ? new ArrayList<>(estrategiasExpiracion) : new ArrayList<>();
        this.interaccionesColision = interaccionesColision != null ? new ArrayList<>(interaccionesColision) : new ArrayList<>();
    }

    public void setPosicion(VecDouble2D posicion) {
        this.posicion = Objects.requireNonNull(posicion, "La posición no puede ser nula");
    }

    public void setVelocidad(VecDouble2D velocidad) {
        this.velocidad = Objects.requireNonNull(velocidad, "La velocidad no puede ser nula").clone();
    }

    public void setAceleracion(double aceleracion) {
        if (!Double.isFinite(aceleracion) || aceleracion < 0) {
            throw new IllegalArgumentException("La aceleración debe ser finita y no negativa.");
        }
        this.aceleracion = aceleracion;
    }

    public void setFriccion(double friccion) {
        if (!Double.isFinite(friccion) || friccion < 0) {
            throw new IllegalArgumentException("La fricción debe ser finita y no negativa.");
        }
        this.friccion = friccion;
    }

    public void setVelocidadMax(double velocidadMax) {
        if (!Double.isFinite(velocidadMax) || velocidadMax < 0) {
            throw new IllegalArgumentException("La velocidad máxima debe ser finita y no negativa.");
        }
        this.velocidadMax = velocidadMax;
    }

    public void setEstrategiaMovimiento(EstrategiaMovimiento estrategiaMovimiento) {
        this.estrategiaMovimiento = estrategiaMovimiento;
    }

    public void setEstrategiasExpiracion(List<EstrategiaExpiracion> estrategiasExpiracion) {
        this.estrategiasExpiracion = estrategiasExpiracion != null ? new ArrayList<>(estrategiasExpiracion) : new ArrayList<>();
    }

    public void añadirEstrategiaExpiracion(EstrategiaExpiracion estrategia) {
        if (estrategia != null) {
            this.estrategiasExpiracion.add(estrategia);
        }
    }

    public void setInteraccionesColision(List<Interaccion> interaccionesColision) {
        this.interaccionesColision = interaccionesColision != null ? new ArrayList<>(interaccionesColision) : new ArrayList<>();
    }

    public void añadirInteraccionColision(Interaccion interaccion) {
        if (interaccion != null && !this.interaccionesColision.contains(interaccion)) {
            this.interaccionesColision.add(interaccion);
        }
    }

    public HitboxFactory getHitboxFactory() { return this.hitboxFactory; }
    public VecDouble2D getPosicion() { return this.posicion != null ? this.posicion.clone() : new VecDouble2D(); }
    public VecDouble2D getVelocidad() { return this.velocidad != null ? this.velocidad.clone() : new VecDouble2D(); }
    public double getAceleracion() { return this.aceleracion; }
    public double getFriccion() { return this.friccion; }
    public double getVelocidadMax() { return this.velocidadMax; }
    public EstrategiaMovimiento getEstrategiaMovimiento() { return this.estrategiaMovimiento; }
    public List<EstrategiaExpiracion> getEstrategiasExpiracion() { return new ArrayList<>(this.estrategiasExpiracion); }
    public List<Interaccion> getInteraccionesColision() { return new ArrayList<>(this.interaccionesColision); }

    public abstract ModeloEntidad get();
}
