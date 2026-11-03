package modelo.enemigo;

import java.util.ArrayList;
import java.util.List;

import motor.colisiones.hitboxes.HitboxFactory;
import motor.modelo.EntidadFactory;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.Interaccion;
import motor.modelo.Posicionable;
import motor.util.VecDouble2D;

public abstract class EnemigoFactory extends EntidadFactory {
    private double vidaMaxima;
    private double daño;
    private int experienciaRecompensa;
    private Posicionable objetivo;
    private double intervaloAtaque;

    public EnemigoFactory(
        HitboxFactory hitboxFactory,
        VecDouble2D posicion,
        double aceleracion,
        double friccion,
        double velocidadMax,
        double vidaMaxima,
        double daño,
        int experienciaRecompensa,
        Posicionable objetivo,
        double intervaloAtaque,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(
            hitboxFactory,
            posicion,
            new VecDouble2D(0.0, 0.0),
            aceleracion,
            friccion,
            velocidadMax,
            estrategiasExpiracion != null ? estrategiasExpiracion : new ArrayList<>(),
            interaccionesColision != null ? interaccionesColision : new ArrayList<>()
        );
        setVidaMaxima(vidaMaxima);
        setDanio(daño);
        setExperienciaRecompensa(experienciaRecompensa);
        this.objetivo = objetivo;
        setIntervaloAtaque(intervaloAtaque);
    }

    public void setVidaMaxima(double vidaMaxima) {
        if (!Double.isFinite(vidaMaxima) || vidaMaxima <= 0) {
            throw new IllegalArgumentException("La vida máxima debe ser positiva y finita.");
        }
        this.vidaMaxima = vidaMaxima;
    }

    public double getVidaMaxima() {
        return vidaMaxima;
    }

    public void setDanio(double daño) {
        if (!Double.isFinite(daño) || daño < 0) {
            throw new IllegalArgumentException("El daño no puede ser negativo y debe ser finito.");
        }
        this.daño = daño;
    }

    public double getDanio() {
        return daño;
    }

    public void setExperienciaRecompensa(int experienciaRecompensa) {
        if (experienciaRecompensa < 0) {
            throw new IllegalArgumentException("La experiencia de recompensa no puede ser negativa.");
        }
        this.experienciaRecompensa = experienciaRecompensa;
    }

    public int getExperienciaRecompensa() {
        return experienciaRecompensa;
    }

    public void setObjetivo(Posicionable objetivo) {
        this.objetivo = objetivo;
    }

    public Posicionable getObjetivo() {
        return objetivo;
    }

    public void setIntervaloAtaque(double intervaloAtaque) {
        if (!Double.isFinite(intervaloAtaque) || intervaloAtaque <= 0) {
            throw new IllegalArgumentException("El intervalo de ataque debe ser positivo y finito.");
        }
        this.intervaloAtaque = intervaloAtaque;
    }

    public double getIntervaloAtaque() {
        return intervaloAtaque;
    }

    @Override
    public abstract Enemigo get();
}
