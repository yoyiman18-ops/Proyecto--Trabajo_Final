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
    private final Notificador<VecDouble2D> notificadorPosicion;

    public Entidad(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion) {
        this.nombre = nombre;
        this.hitbox = hitbox;
        this.posicion = posicion;
        this.velocidad = velocidad;
        this.aceleracion = aceleracion;
        this.friccion = friccion;
        this.notificadorPosicion = new NotificadorDebil<>();
    }

    public void mover() {
        posicion.sumar(velocidad);
        hitbox.actualizarTransformacion(posicion);
        notificadorPosicion.notificar(posicion);
    }

    public void acelerar(Direccion direccion) {
        switch (direccion) {
            case Direccion.ARRIBA -> velocidad.restar(aceleracion, Eje.Y);
            case Direccion.ABAJO -> velocidad.sumar(aceleracion, Eje.Y);
            case Direccion.IZQUIERDA -> velocidad.restar(aceleracion, Eje.X);
            case Direccion.DERECHA -> velocidad.sumar(aceleracion, Eje.X);
        }
    }

    public void frenar(Eje eje) {
        switch (eje) {
            case Eje.X -> {
                if (velocidad.getX() == 0) { return; }
                if (velocidad.getX() > 0) {
                    velocidad.setX(Math.max(posicion.getX() - friccion, 0));
                } else {
                    velocidad.setX(Math.min(posicion.getX() + friccion, 0));
                }
            }
            case Eje.Y -> {
                if (velocidad.getY() > 0) {
                    velocidad.setY(Math.max(posicion.getY() - friccion, 0));
                } else {
                    velocidad.setY(Math.min(posicion.getY() + friccion, 0));
                }
            }
        }
    }

    public double getAceleracion() { return this.aceleracion; }
    public double getFriccion() { return this.friccion; }

    @Override public Hitbox getHitbox() { return this.hitbox; }
    @Override public boolean colisionesActivas() { return this.hitbox.estaActiva(); }
    @Override public void colisionar(Colisionable otra) { System.out.println(this.nombre +" ha colisionado."); }


}
