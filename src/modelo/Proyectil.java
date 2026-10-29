package modelo;

import motor.colisiones.Colisionable;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;

public final class Proyectil
extends Entidad
implements ModeloProyectil,Dañable {

    private final double daño;
    private double tiempoVida;

    public Proyectil(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D direccion,
        double daño,
        double velocidad,
        double tiempoVida
    ) {
        this(
            nombre,
            hitbox,
            posicion,
            daño,
            velocidad,
            tiempoVida,
            new NotificadorDebil<>()
        );
    }

    public Proyectil(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        double daño,
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
        if (!Double.isFinite(daño) || daño < 0) { throw new IllegalArgumentException("El daño debe ser finito y no negativo.");}
        if (!Double.isFinite(tiempoVida) || tiempoVida <= 0) { throw new IllegalArgumentException("El tiempo de vida debe ser positivo y finito."); }
        this.daño = daño;
        this.tiempoVida = tiempoVida;
    }

    @Override public double getDaño() { return daño; }
    @Override public void tick(double dt) {
        if (!super.activa()) { return; }
        tiempoVida = Math.max(0, tiempoVida - dt); 
        super.tick(dt);
        if (estaExpirado()) { super.setActivo(false); }
    }

    @Override public Entidad getEntidad() { return this; }

    

    @Override public boolean dañar(Dañable o) { return o.recibirDaño(this.getDaño()); }
    private boolean estaExpirado() { return tiempoVida == 0; }

}
