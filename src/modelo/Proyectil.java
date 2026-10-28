package modelo;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;

public final class Proyectil extends Entidad implements ModeloProyectil {

    private final double danio;
    private double tiempoVida;

    public Proyectil(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D direccion,
        double danio,
        double velocidad,
        double tiempoVida
    ) {
        this(
            nombre,
            hitbox,
            posicion,
            direccion,
            danio,
            velocidad,
            tiempoVida,
            new NotificadorDebil<>()
        );
    }

    public Proyectil(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D direccion,
        double danio,
        double velocidad,
        double tiempoVida,
        Notificador<Entidad> notificador
    ) {
        super(
            nombre,
            hitbox,
            posicion,
            new VecDouble2D(),
            0,
            0,
            velocidad,
            notificador
        );

        if (direccion == null) {
            throw new IllegalArgumentException(
                "La dirección no puede ser nula."
            );
        }

        if (!Double.isFinite(danio) || danio < 0) {
            throw new IllegalArgumentException(
                "El daño debe ser finito y no negativo."
            );
        }

        if (!Double.isFinite(velocidad) || velocidad <= 0) {
            throw new IllegalArgumentException(
                "La velocidad debe ser positiva y finita."
            );
        }

        if (!Double.isFinite(tiempoVida) || tiempoVida <= 0) {
            throw new IllegalArgumentException(
                "El tiempo de vida debe ser positivo y finito."
            );
        }

        VecDouble2D velocidadCalculada = direccion.clone().normalizado();
        velocidadCalculada.producto(velocidad);
        getVelocidad().setX(velocidadCalculada.getX());
        getVelocidad().setY(velocidadCalculada.getY());

        this.danio = danio;
        this.tiempoVida = tiempoVida;
    }

    @Override
    public double getDanio() {
        return danio;
    }

    @Override
    public void actualizarTiempoVida(double deltaTiempo) {
        if (!Double.isFinite(deltaTiempo) || deltaTiempo < 0) {
            throw new IllegalArgumentException(
                "El delta de tiempo debe ser finito y no negativo."
            );
        }

        tiempoVida = Math.max(0, tiempoVida - deltaTiempo);
    }

    @Override
    public boolean estaExpirado() {
        return tiempoVida == 0;
    }
}
