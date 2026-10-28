package modelo;

import java.util.Objects;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;

public final class Enemigo extends Entidad implements ModeloEnemigo {
    public static final double INTERVALO_ATAQUE_DEFAULT = 1.0;

    private double vida;
    private final double vidaMaxima;
    private final double danio;
    private final int experienciaRecompensa;
    private final double intervaloAtaque;
    private double tiempoHastaAtaque;

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
            INTERVALO_ATAQUE_DEFAULT,
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
        this(
            nombre,
            hitbox,
            posicion,
            velocidadMaxima,
            vidaMaxima,
            danio,
            experienciaRecompensa,
            INTERVALO_ATAQUE_DEFAULT,
            notificador
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
        double intervaloAtaque,
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
        if (!Double.isFinite(intervaloAtaque) || intervaloAtaque <= 0) {
            throw new IllegalArgumentException("El intervalo de ataque debe ser positivo y finito.");
        }
        this.vida = vidaMaxima;
        this.vidaMaxima = vidaMaxima;
        this.danio = danio;
        this.experienciaRecompensa = experienciaRecompensa;
        this.intervaloAtaque = intervaloAtaque;
        this.tiempoHastaAtaque = 0;
    }

    @Override
    public double getVida() { return vida; }

    @Override
    public double getVidaMaxima() { return vidaMaxima; }

    @Override
    public double getDanio() { return danio; }

    @Override
    public int getExperienciaRecompensa() { return experienciaRecompensa; }

    @Override
    public Entidad getEntidad() { return this; }

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

    @Override
    public void actualizarEnfriamientoAtaque(double dt) {
        if (!Double.isFinite(dt) || dt < 0) {
            throw new IllegalArgumentException("El delta de tiempo debe ser finito y no negativo.");
        }
        tiempoHastaAtaque = Math.max(0, tiempoHastaAtaque - dt);
    }

    @Override
    public boolean atacar(ObjetivoEnemigo objetivo) {
        Objects.requireNonNull(objetivo, "El objetivo no puede ser nulo.");
        if (tiempoHastaAtaque > 0 || vida <= 0 || objetivo.getVida() <= 0) {
            return false;
        }
        objetivo.recibirDanio(danio);
        tiempoHastaAtaque = intervaloAtaque;
        return true;
    }

    @Override
    public void detener() {
        getVelocidad().setX(0);
        getVelocidad().setY(0);
    }

    @Override
    public void perseguir(VecDouble2D destino, double dt) {
        Objects.requireNonNull(destino, "El destino no puede ser nulo.");
        VecDouble2D posicionActual = getPosicion();
        double diferenciaX = destino.getX() - posicionActual.getX();
        double diferenciaY = destino.getY() - posicionActual.getY();
        double distancia = Math.hypot(diferenciaX, diferenciaY);
        if (distancia == 0) {
            detener();
            return;
        }
        double velocidadMax = getVelocidadMax();
        getVelocidad().setX(diferenciaX / distancia * velocidadMax);
        getVelocidad().setY(diferenciaY / distancia * velocidadMax);
        mover(dt);
    }
}
