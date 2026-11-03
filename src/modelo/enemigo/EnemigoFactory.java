package modelo.enemigo;

import java.util.ArrayList;
import java.util.List;

import motor.colisiones.hitboxes.HitboxFactory;
import motor.modelo.EntidadFactory;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.EstrategiaMovimientoPersecucion;
import motor.modelo.Interaccion;
import motor.modelo.Posicionable;
import motor.util.VecDouble2D;

public abstract class EnemigoFactory extends EntidadFactory {
    private final double vidaMaxima;
    private final double daño;
    private final int experienciaRecompensa;
    private Posicionable objetivo;
    private final double intervaloAtaque;

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
        EstrategiaMovimiento estrategiaMovimiento,
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
            estrategiaMovimiento,
            estrategiasExpiracion != null ? estrategiasExpiracion : new ArrayList<>(),
            interaccionesColision != null ? interaccionesColision : new ArrayList<>()
        );
        if (!Double.isFinite(vidaMaxima) || vidaMaxima <= 0) {
            throw new IllegalArgumentException("La vida máxima debe ser positiva y finita.");
        }
        if (!Double.isFinite(daño) || daño < 0) {
            throw new IllegalArgumentException("El daño no puede ser negativo y debe ser finito.");
        }
        if (experienciaRecompensa < 0) {
            throw new IllegalArgumentException("La experiencia de recompensa no puede ser negativa.");
        }
        if (!Double.isFinite(intervaloAtaque) || intervaloAtaque <= 0) {
            throw new IllegalArgumentException("El intervalo de ataque debe ser positivo y finito.");
        }
        this.vidaMaxima = vidaMaxima;
        this.daño = daño;
        this.experienciaRecompensa = experienciaRecompensa;
        this.objetivo = objetivo;
        this.intervaloAtaque = intervaloAtaque;
    }

    public void setObjetivo(Posicionable objetivo) {
        this.objetivo = objetivo;
        if (getEstrategiaMovimiento() instanceof EstrategiaMovimientoPersecucion persecucion) {
            persecucion.setObjetivo(objetivo);
        }
    }

    public double getVidaMaxima() {
        return vidaMaxima;
    }

    public double getDanio() {
        return daño;
    }

    public int getExperienciaRecompensa() {
        return experienciaRecompensa;
    }

    public Posicionable getObjetivo() {
        return objetivo;
    }

    public double getIntervaloAtaque() {
        return intervaloAtaque;
    }

    @Override
    public abstract Enemigo get();
}
