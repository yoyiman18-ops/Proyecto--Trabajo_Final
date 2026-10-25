package modelo;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;

public final class Enemigo extends Entidad implements ModeloEnemigo {
    private double vida;
    private final double vidaMaxima;
    private final double danio;
    private final int experienciaRecompensa;

    public Enemigo(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        double velocidadMaxima,
        double vidaMaxima,
        double danio,
        int experienciaRecompensa
    ) {
        this(
            nombre,
            hitbox,
            posicion,
            velocidadMaxima,
            vidaMaxima,
            danio,
            experienciaRecompensa,
            new NotificadorDebil<>()
        );
    }

    public Enemigo(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        double velocidadMaxima,
        double vidaMaxima,
        double danio,
        int experienciaRecompensa,
        Notificador<Entidad> notificador
    ) {
        super(nombre, hitbox, posicion, new VecDouble2D(), 0, 0, velocidadMaxima, notificador);
        if (!Double.isFinite(vidaMaxima) || vidaMaxima <= 0) {
            throw new IllegalArgumentException("La vida máxima debe ser positiva y finita.");
        }
        if (!Double.isFinite(danio) || danio < 0) {
            throw new IllegalArgumentException("El daño debe ser finito y no negativo.");
        }
        if (experienciaRecompensa < 0) {
            throw new IllegalArgumentException("La experiencia de recompensa no puede ser negativa.");
        }
        this.vida = vidaMaxima;
        this.vidaMaxima = vidaMaxima;
        this.danio = danio;
        this.experienciaRecompensa = experienciaRecompensa;
    }

    @Override
    public double getVida() {
        return vida;
    }

    @Override
    public double getVidaMaxima() {
        return vidaMaxima;
    }

    @Override
    public double getDanio() {
        return danio;
    }

    @Override
    public int getExperienciaRecompensa() {
        return experienciaRecompensa;
    }

    @Override
    public Entidad getEntidad() {
        return this;
    }

    @Override
    public boolean recibirDanio(double cantidad) {
        if (!Double.isFinite(cantidad) || cantidad < 0) {
            throw new IllegalArgumentException("El daño recibido debe ser finito y no negativo.");
        }
        if (vida == 0 || cantidad == 0) {
            return false;
        }
        vida = Math.max(0, vida - cantidad);
        return vida == 0;
    }
}