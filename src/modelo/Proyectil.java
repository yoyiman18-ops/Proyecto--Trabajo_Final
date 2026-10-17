
package modelo;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.util.VecDouble2D;

public final class Proyectil extends Entidad {

    private final double danio;
    private double tiempoVida;
    private boolean activo;

    public Proyectil(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D direccion,
        double velocidad,
        double danio,
        double tiempoVida
    ) {
        super(
            nombre,
            hitbox,
            posicion,
            crearVelocidad(direccion, velocidad),
            0,
            0,
            velocidad
        );

        if (!Double.isFinite(danio) || danio < 0) {
            throw new IllegalArgumentException(
                "El daño debe ser finito y no negativo."
            );
        }

        if (!Double.isFinite(tiempoVida) || tiempoVida <= 0) {
            throw new IllegalArgumentException(
                "El tiempo de vida debe ser positivo y finito."
            );
        }

        this.danio = danio;
        this.tiempoVida = tiempoVida;
        this.activo = true;
    }

    private static VecDouble2D crearVelocidad(
        VecDouble2D direccion,
        double velocidad
    ) {
        if (direccion == null) {
            throw new IllegalArgumentException(
                "La dirección no puede ser nula."
            );
        }

        if (!Double.isFinite(velocidad) || velocidad <= 0) {
            throw new IllegalArgumentException(
                "La velocidad debe ser positiva y finita."
            );
        }

        VecDouble2D direccionNormalizada = direccion.clone();

        if (direccionNormalizada.getX() == 0
                && direccionNormalizada.getY() == 0) {
            throw new IllegalArgumentException(
                "La dirección del proyectil no puede ser cero."
            );
        }

        direccionNormalizada.normalizar();
        direccionNormalizada.producto(velocidad);

        return direccionNormalizada;
    }

    public double getDanio() {
        return danio;
    }

    public double getTiempoVida() {
        return tiempoVida;
    }

    public boolean estaActivo() {
        return activo;
    }

    public void actualizar(double dt) {
        if (!Double.isFinite(dt) || dt < 0) {
            throw new IllegalArgumentException(
                "El tiempo de actualización debe ser finito y no negativo."
            );
        }

        if (!activo) {
            return;
        }

        tiempoVida -= dt;

        if (tiempoVida <= 0) {
            tiempoVida = 0;
            desactivar();
            return;
        }

        mover(dt);
    }

    public void desactivar() {
        if (!activo) {
            return;
        }

        activo = false;
        getHitbox().desactivar();
    }
}
