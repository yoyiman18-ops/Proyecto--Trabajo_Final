package motor.modelo;

import motor.colisiones.Colisionable;
import motor.colisiones.hitboxes.Hitbox;
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

    public Entidad(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax) {
        this.nombre = nombre;
        this.hitbox = hitbox;
        this.posicion = posicion;
        this.velocidad = velocidad;
        this.aceleracion = aceleracion;
        this.friccion = friccion;
        this.velocidadMax = velocidadMax;
        this.notificador = new NotificadorDebil<>();
    }

    public void mover(double dt) {
        if (velocidad.getX() != 0 || velocidad.getY() != 0) {
            velocidad.producto(dt);
            posicion.sumar(velocidad);
            hitbox.actualizarTransformacion(posicion);
            notificador.notificar(this);
        }
    }

    public void acelerar(Direccion direccion) {
        switch (direccion) {
            case Direccion.ARRIBA -> velocidad.setY(Math.max(velocidad.getY() - aceleracion, -velocidadMax));
            case Direccion.ABAJO -> { velocidad.setY(Math.min(velocidad.getY() + aceleracion, velocidadMax));}
            case Direccion.IZQUIERDA -> velocidad.setX(Math.max(velocidad.getX() - aceleracion, -velocidadMax));
            case Direccion.DERECHA -> velocidad.setX(Math.min(velocidad.getX() + aceleracion, velocidadMax));
        }
    }

    public void frenar(Eje eje) {
        switch (eje) {
            case Eje.X -> {
                if (velocidad.getX() == 0) { return; }
                if (velocidad.getX() > 0) {
                    velocidad.setX(Math.max(velocidad.getX() - friccion, 0));
                } else {
                    velocidad.setX(Math.min(velocidad.getX() + friccion, 0));
                }
            }
            case Eje.Y -> {
                if (velocidad.getY() > 0) {
                    velocidad.setY(Math.max(velocidad.getY() - friccion, 0));
                } else {
                    velocidad.setY(Math.min(velocidad.getY() + friccion, 0));
                }
            }
        }
    }

    public VecDouble2D getVelocidad() { return this.velocidad; }
    public VecDouble2D getPosicion() { return this.posicion; }
    public double getAceleracion() { return this.aceleracion; }
    public double getFriccion() { return this.friccion; }
    public Notificador<Entidad> getNotificador() { return this.notificador; }

    @Override public Hitbox getHitbox() { return this.hitbox; }
    @Override public boolean colisionesActivas() { return this.hitbox.estaActiva(); }
    @Override public void colisionar(Colisionable otra) { System.out.println(this.nombre +" ha colisionado."); }


}
