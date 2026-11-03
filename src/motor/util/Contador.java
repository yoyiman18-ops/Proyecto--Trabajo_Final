package motor.util;

import motor.modelo.Ticking;
import motor.util.observer.NotificadorDebil;

public class Contador extends NotificadorDebil<Double> implements Ticking {

    private double duracion;
    private double tiempoRestante;

    public Contador(double duracion) {
        this(duracion, 0.0);
    }

    public Contador(double duracion, double tiempoInicialRestante) {
        if (!Double.isFinite(duracion) || duracion < 0) {
            throw new IllegalArgumentException("La duración del contador debe ser finita y no negativa.");
        }
        this.duracion = duracion;
        this.tiempoRestante = Math.max(0.0, tiempoInicialRestante);
    }

    @Override
    public void tick(double dt) {
        if (dt <= 0 || estaListo()) {
            return;
        }
        tiempoRestante = Math.max(0.0, tiempoRestante - dt);
        if (estaListo()) {
            notificar(tiempoRestante);
        }
    }

    public boolean estaListo() {
        return tiempoRestante <= 0.0;
    }

    public void reiniciar() {
        this.tiempoRestante = this.duracion;
    }

    public void reset() {
        this.tiempoRestante = 0.0;
    }

    public void setDuracion(double duracion) {
        if (!Double.isFinite(duracion) || duracion < 0) {
            throw new IllegalArgumentException("La duración debe ser finita y no negativa.");
        }
        this.duracion = duracion;
    }

    public double getTiempoRestante() {
        return tiempoRestante;
    }
}
