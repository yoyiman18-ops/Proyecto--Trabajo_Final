package motor.modelo;

import java.awt.geom.Rectangle2D;
import java.util.Objects;

import motor.colisiones.Colisionable;
import motor.colisiones.hitboxes.Hitbox;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.util.Eje;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;

public class Entidad implements Colisionable {
    private final String nombre;
    private final Hitbox hitbox;
    private VecDouble2D posicion;
    private VecDouble2D velocidad;
    private double aceleracion;
    private double friccion;
    private double velocidadMax;
    private final Notificador<Entidad> notificador;
    private final Notificador<Colisionable> notificadorColision;

    public Entidad(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax) {
        this(
            nombre,
            hitbox,
            posicion,
            velocidad,
            aceleracion,
            friccion,
            velocidadMax,
            new NotificadorDebil<>()
        );
    }

    public Entidad(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax,
        Notificador<Entidad> notificador) {
        this.nombre = nombre;
        this.hitbox = hitbox;
        this.posicion = posicion;
        this.velocidad = velocidad;
        this.aceleracion = aceleracion;
        this.friccion = friccion;
        this.velocidadMax = velocidadMax;
        this.notificador = Objects.requireNonNull(notificador, "El notificador no puede ser nulo");
        this.notificadorColision = new NotificadorDebil<>();
        if (this.hitbox != null && this.posicion != null) {
            this.hitbox.actualizarTransformacion(this.posicion);
        }
    }

    public void mover(double dt) {
        if (velocidad.getX() != 0 || velocidad.getY() != 0) {
            VecDouble2D desplazamiento = velocidad.clone();
            desplazamiento.producto(dt);
            posicion.sumar(desplazamiento);
            hitbox.actualizarTransformacion(posicion);
            notificador.notificar(this);
        }
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

    private void limitarVelocidad() {
        double magnitud = Math.hypot(velocidad.getX(), velocidad.getY());
        if (magnitud > velocidadMax) {
            double factor = velocidadMax / magnitud;
            velocidad.setX(velocidad.getX() * factor);
            velocidad.setY(velocidad.getY() * factor);
        }
    }

    public VecDouble2D getVelocidad() { return this.velocidad; }
    public VecDouble2D getPosicion() { return this.posicion; }
    public double getAceleracion() { return this.aceleracion; }
    public double getFriccion() { return this.friccion; }
    public double getVelocidadMax() { return this.velocidadMax; }
    public Notificador<Entidad> getNotificador() { return this.notificador; }
    public Notificador<Colisionable> getNotificadorColision() { return this.notificadorColision; }

    public void setVelocidadMax(double velocidadMax) {
        if (!Double.isFinite(velocidadMax) || velocidadMax < 0) {
            throw new IllegalArgumentException("La velocidad maxima debe ser finita y no negativa.");
        }
        this.velocidadMax = velocidadMax;
    }

    @Override public Hitbox getHitbox() { return this.hitbox; }
    @Override public boolean colisionesActivas() { return this.hitbox.estaActiva(); }

    @Override
    public void colisionar(Colisionable otra) {
        if (otra != null
            && this.hitbox != null
            && otra.getHitbox() != null
            && this.hitbox.getTipo() == TipoHitbox.SOLIDA
            && otra.getHitbox().getTipo() == TipoHitbox.INAMOVIBLE) {
            resolverColisionInamovible(otra.getHitbox());
        }
        this.notificadorColision.notificar(otra);
    }

    private void resolverColisionInamovible(Hitbox obstaculo) {
        Rectangle2D propio = this.hitbox.getBounds();
        Rectangle2D ajeno = obstaculo.getBounds();
        Rectangle2D interseccion = propio.createIntersection(ajeno);
        if (interseccion.getWidth() <= 0 || interseccion.getHeight() <= 0) {
            return;
        }
        if (interseccion.getWidth() < interseccion.getHeight()) {
            double dx = propio.getCenterX() < ajeno.getCenterX()
                ? -interseccion.getWidth()
                : interseccion.getWidth();
            posicion.setX(posicion.getX() + dx);
            velocidad.setX(0);
        } else {
            double dy = propio.getCenterY() < ajeno.getCenterY()
                ? -interseccion.getHeight()
                : interseccion.getHeight();
            posicion.setY(posicion.getY() + dy);
            velocidad.setY(0);
        }
        this.hitbox.actualizarTransformacion(posicion);
        this.notificador.notificar(this);
    }
}
