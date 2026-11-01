package motor.modelo;

import java.util.List;
import java.util.Objects;

import motor.colisiones.Colisionable;
import motor.colisiones.hitboxes.Hitbox;
import motor.util.Eje;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorFuerte;

public abstract class Entidad 
implements ModeloEntidad {
    private final int idGlobal;
    private boolean activa;
    private final String nombre;
    private final Hitbox hitbox;
    private VecDouble2D posicion;
    private VecDouble2D velocidad;
    private double aceleracion;
    private double friccion;
    private double velocidadMax;

    private final List<Interaccion> interaccionesColision;
    private final List<EstrategiaExpiracion> estrategiasExpiracion;

    private final Notificador<VecDouble2D> notificadorPosicion;
    private final Notificador<ModeloEntidad> notificadorInstanciacion;
    private final Notificador<Integer> notificadorExpiracion;
    private final Notificador<String> notificadorAnimacion;

    private final String animacionDefault;

    public Entidad(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        String animacionDefault
    ) {
        this(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            velocidad,
            aceleracion,
            friccion,
            velocidadMax,
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            estrategiasExpiracion,
            interaccionesColision,
            animacionDefault
        );
    }

    public Entidad(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax,
        Notificador<VecDouble2D> notificadorPosicion,
        Notificador<ModeloEntidad> notificadorInstanciacion,
        Notificador<Integer> notificadorExpiracion,
        Notificador<String> notificadorAnimacion,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        String animacionDefault
    ) {
        this.activa = true;
        this.idGlobal = idGlobal;
        this.nombre = Objects.requireNonNull(nombre, "Nombre no puede ser nulo");
        this.hitbox = Objects.requireNonNull(hitbox, "Hitbox no puede ser nula");
        this.posicion = Objects.requireNonNull(posicion, "Posicion no puede ser nula");
        this.velocidad = Objects.requireNonNull(velocidad, "Velocidad no puede ser nula");
        this.aceleracion = aceleracion;
        this.friccion = friccion;
        this.velocidadMax = velocidadMax;
        this.notificadorPosicion = Objects.requireNonNull(notificadorPosicion, "El notificadorPosicion no puede ser nulo");
        this.notificadorInstanciacion = Objects.requireNonNull(notificadorInstanciacion, "El notificadorInstanciacion no puede ser nulo");
        this.notificadorExpiracion = Objects.requireNonNull(notificadorExpiracion, "El notificadorExpiracion no puede ser nulo");
        this.notificadorAnimacion = Objects.requireNonNull(notificadorAnimacion, "El notificadorExpiracion no puede ser nulo");
        if (this.hitbox != null && this.posicion != null) {
            this.notificadorPosicion.suscribirObservador(hitbox);
            this.hitbox.cambio(this.posicion);
        }
        this.interaccionesColision = Objects.requireNonNull(interaccionesColision);
        this.estrategiasExpiracion = Objects.requireNonNull(estrategiasExpiracion);
        this.animacionDefault = animacionDefault;
    }

    public void acelerar(Direccion direccion, double dt) {
        double cambioVelocidad = aceleracion * dt;
        switch (direccion) {
            case Direccion.ARRIBA -> velocidad.setY(velocidad.getY() - cambioVelocidad);
            case Direccion.ABAJO -> velocidad.setY(velocidad.getY() + cambioVelocidad);
            case Direccion.IZQUIERDA -> velocidad.setX(velocidad.getX() - cambioVelocidad);
            case Direccion.DERECHA -> velocidad.setX(velocidad.getX() + cambioVelocidad);
        }
        limitarVelocidad();
    }

    public void frenar(Eje eje, double dt) {
        double cambioVelocidad = friccion * dt;
        switch (eje) {
            case Eje.X -> {
                if (velocidad.getX() == 0) { return; }
                if (velocidad.getX() > 0) {
                    velocidad.setX(Math.max(velocidad.getX() - cambioVelocidad, 0));
                } else {
                    velocidad.setX(Math.min(velocidad.getX() + cambioVelocidad, 0));
                }
            }
            case Eje.Y -> {
                if (velocidad.getY() > 0) {
                    velocidad.setY(Math.max(velocidad.getY() - cambioVelocidad, 0));
                } else {
                    velocidad.setY(Math.min(velocidad.getY() + cambioVelocidad, 0));
                }
            }
        }
    }

    @Override public void tick(double dt) { if (activo()) { mover(dt); } }

    public String getNombre() { return this.nombre; }
    public VecDouble2D getVelocidad() { return this.velocidad; }
    @Override public VecDouble2D getPosicion() { return this.posicion; }
    @Override public void setPosicion(VecDouble2D posicion) { this.posicion = posicion.clone(); }
    public double getAceleracion() { return this.aceleracion; }
    public double getFriccion() { return this.friccion; }
    public double getVelocidadMax() { return this.velocidadMax; }
    @Override public String getAnimacionDefault() { return this.animacionDefault; }


    @Override public Notificador<VecDouble2D> getNotificadorPosicion() { return this.notificadorPosicion; }
    @Override public Notificador<ModeloEntidad> getNotificadorInstanciacion() { return this.notificadorInstanciacion; }
    @Override public Notificador<Integer> getNotificadorExpiracion() { return this.notificadorExpiracion; }
    @Override public Notificador<String> getNotificadorAnimacion() { return this.notificadorAnimacion; }

    public void setVelocidadMax(double velocidadMax) {
        if (!Double.isFinite(velocidadMax) || velocidadMax < 0) {
            throw new IllegalArgumentException("La velocidad maxima debe ser finita y no negativa.");
        }
        this.velocidadMax = velocidadMax;
    }

    @Override public void setActivo(boolean activa) { this.activa = activa; } 
    @Override public boolean activo() { return this.activa; }
    @Override public Hitbox getHitbox() { return this.hitbox; }
    @Override public int getIdGlobal() { return this.idGlobal; }
    @Override public boolean colisionesActivas() { return this.hitbox.estaActiva(); }

    @Override public void colisionar(Colisionable o) {
        for (Interaccion i : interaccionesColision) { o.interactuar(i); }
    }

    protected boolean añadirInteraccionColision(Interaccion i) {
        if (!interaccionesColision.contains(i)) { interaccionesColision.add(i); return true; }
        return false;
    }

    protected void mover(double dt) {
        if (velocidad.getX() != 0 || velocidad.getY() != 0) {
            VecDouble2D desplazamiento = velocidad.clone();
            desplazamiento.producto(dt);
            posicion.sumar(desplazamiento);
            notificadorPosicion.notificar(posicion);
        }
    }

    protected void expirar() {
        if (!activo()) { return; }
        setActivo(false);
        int estado = 0; // >= 1 para forzar terminación
        if (estrategiasExpiracion != null) { 
            for (EstrategiaExpiracion e : estrategiasExpiracion) { if ((estado = e.ejecutar(this)) >= 1) {continue; };}
        }
        getNotificadorExpiracion().notificar(estado);
    }

    private void limitarVelocidad() {
        double magnitud = Math.hypot(velocidad.getX(), velocidad.getY());
        if (magnitud > velocidadMax) {
            double factor = velocidadMax / magnitud;
            velocidad.setX(velocidad.getX() * factor);
            velocidad.setY(velocidad.getY() * factor);
        }
    }

}
