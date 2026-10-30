package modelo;

import java.util.Objects;

import motor.colisiones.Colisionable;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Dañable;
import motor.modelo.Entidad;
import motor.modelo.Posicionable;
import motor.modelo.Solido;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorFuerte;

public final class Enemigo
extends Entidad
implements ModeloEnemigo
{
    private double vida;
    private final double vidaMaxima;
    private final double daño;
    private final int experienciaRecompensa;
    private final Posicionable objetivo;
    private final double intervaloAtaque;
    private double tiempoHastaAtaque;
    private boolean contactoConObjetivo;
    private final Notificador<Double> notificadorDaño;
    private final Notificador<Integer> notificadorExpiracion;

    public Enemigo(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        double velocidadMaxima,
        double vidaMaxima,
        double daño,
        int experienciaRecompensa,
        Posicionable objetivo
    ) {
        this(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            velocidadMaxima,
            vidaMaxima,
            daño,
            experienciaRecompensa,
            objetivo,
            1.0
        );
    }

    public Enemigo(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        double velocidadMaxima,
        double vidaMaxima,
        double daño,
        int experienciaRecompensa,
        Posicionable objetivo,
        double intervaloAtaque
    ) {
        super(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            new VecDouble2D(),
            0.0,
            0.0,
            velocidadMaxima
        );
        if (!Double.isFinite(vidaMaxima) || vidaMaxima <= 0) {
            throw new IllegalArgumentException("La vida máxima debe ser positiva y finita.");
        }
        if (!Double.isFinite(daño) || daño < 0) {
            throw new IllegalArgumentException("El daño debe ser finito y no negativo.");
        }
        if (experienciaRecompensa < 0) {
            throw new IllegalArgumentException("La experiencia de recompensa no puede ser negativa.");
        }
        if (!Double.isFinite(intervaloAtaque) || intervaloAtaque <= 0) {
            throw new IllegalArgumentException("El intervalo de ataque debe ser positivo y finito.");
        }
        this.objetivo = Objects.requireNonNull(objetivo, "El objetivo no puede ser nulo");
        this.vida = vidaMaxima;
        this.vidaMaxima = vidaMaxima;
        this.daño = daño;
        this.experienciaRecompensa = experienciaRecompensa;
        this.intervaloAtaque = intervaloAtaque;
        this.tiempoHastaAtaque = 0;
        this.contactoConObjetivo = false;
        this.notificadorDaño = new NotificadorFuerte<>();
        this.notificadorExpiracion = new NotificadorFuerte<>();
    }

    @Override
    public void tick(double dt) {
        if (!activo()) {
            return;
        }
        tiempoHastaAtaque = Math.max(0, tiempoHastaAtaque - dt);

        if (contactoConObjetivo) {
            getVelocidad().setX(0);
            getVelocidad().setY(0);
            contactoConObjetivo = false;
            return;
        }

        VecDouble2D posicion = getPosicion();
        VecDouble2D posicionObjetivo = objetivo.getPosicion();
        double diferenciaX = posicionObjetivo.getX() + 25 - posicion.getX();
        double diferenciaY = posicionObjetivo.getY() + 25 - posicion.getY();
        double distancia = Math.hypot(diferenciaX, diferenciaY);

        double velocidadMax = getVelocidadMax();
        if (distancia == 0) {
            getVelocidad().setX(0);
            getVelocidad().setY(0);
            return;
        }
        getVelocidad().setX(diferenciaX / distancia * velocidadMax);
        getVelocidad().setY(diferenciaY / distancia * velocidadMax);
        super.tick(dt);
    }

    @Override public double getVida() { return vida; }
    @Override public double getVidaMaxima() { return vidaMaxima; }
    @Override public double getDaño() { return daño; }
    @Override public int getExperienciaRecompensa() { return experienciaRecompensa; }

    @Override
    public boolean dañar(Dañable o) {
        return o.recibirDaño(this.getDaño());
    }

    @Override
    public boolean recibirDaño(double cantidad) {
        if (!activo()) { return false; }
        if (!Double.isFinite(cantidad) || cantidad < 0) {
            throw new IllegalArgumentException("El daño recibido debe ser finito y no negativo.");
        }
        if (vida == 0 || cantidad == 0) {
            return false;
        }
        vida = Math.max(0, vida - cantidad);
        notificadorDaño.notificar(cantidad);
        if (vida == 0) {
            expirar();
        }
        return vida == 0;
    }

    @Override
    public void colisionar(Colisionable o) {
        o.colisionarCon(getDañable());
    }

    @Override
    public void colisionarCon(Dañable o) {
        if (!activo()) { return; }
        contactoConObjetivo = true;
        if (tiempoHastaAtaque <= 0) {
            dañar(o);
            tiempoHastaAtaque = intervaloAtaque;
        }
    }

    @Override
    public void colisionarCon(Solido o) {}

    @Override public Dañable getDañable() { return this; }
    @Override public Notificador<Double> getNotificadorDaño() { return this.notificadorDaño; }
    @Override public Notificador<Integer> getNotificadorExpiracion() { return this.notificadorExpiracion; }

    private void expirar() {
        setActivo(false);
        this.notificadorExpiracion.notificar(1);
    }
}
