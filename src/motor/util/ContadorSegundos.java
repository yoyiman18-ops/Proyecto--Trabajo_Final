package motor.util;

import motor.modelo.Ticking;
import motor.util.observer.NotificadorDebil;

public class ContadorSegundos 
extends NotificadorDebil<Double> 
implements Ticking {

    private double intervaloSegundos;
    private double tiempoActual;
    private boolean bucle;

    public ContadorSegundos() {
        this(1.0, 0.0, true);
    }

    public ContadorSegundos(double intervaloSegundos) {
        this(intervaloSegundos, 0.0, false);
    }

    public ContadorSegundos(double intervaloSegundos, boolean listoInicialmente) {
        this(intervaloSegundos, listoInicialmente ? intervaloSegundos : 0.0, false);
    }

    public ContadorSegundos(double intervaloSegundos, double tiempoActualInicial) {
        this(intervaloSegundos, tiempoActualInicial, false);
    }

    public ContadorSegundos(double intervaloSegundos, double tiempoActualInicial, boolean bucle) {
        if (!Double.isFinite(intervaloSegundos) || intervaloSegundos <= 0) {
            throw new IllegalArgumentException("El intervalo no puede ser igual o menor a 0s");
        }
        this.intervaloSegundos = intervaloSegundos;
        this.tiempoActual = Math.max(0.0, tiempoActualInicial);
        this.bucle = bucle;
    }

    public boolean estaListo() {
        return tiempoActual >= intervaloSegundos;
    }

    public void reiniciar() {
        this.tiempoActual = 0.0;
    }

    public void reset() {
        this.tiempoActual = 0.0;
    }

    public void forzarListo() {
        this.tiempoActual = this.intervaloSegundos;
    }

    public void setIntervaloSegundos(double intervaloSegundos) {
        if (!Double.isFinite(intervaloSegundos) || intervaloSegundos <= 0) {
            throw new IllegalArgumentException("El intervalo no puede ser igual o menor a 0s");
        }
        this.intervaloSegundos = intervaloSegundos;
        this.tiempoActual = Math.min(this.tiempoActual, this.intervaloSegundos);
    }

    public void setDuracion(double duracion) {
        setIntervaloSegundos(duracion);
    }

    public double getIntervaloSegundos() {
        return intervaloSegundos;
    }

    public double getTiempoRestante() {
        return Math.max(0.0, intervaloSegundos - tiempoActual);
    }

    public double getTiempoActual() {
        return tiempoActual;
    }

    public boolean esBucle() {
        return bucle;
    }

    public void setBucle(boolean bucle) {
        this.bucle = bucle;
    }

    @Override
    public void tick(double dt) {
        if (dt <= 0) {
            return;
        }
        if (bucle) {
            tiempoActual += dt;
            double cociente = tiempoActual / intervaloSegundos;
            if (cociente >= 1) {
                tiempoActual -= (int) cociente * intervaloSegundos;
                notificar(cociente);
            }
        } else {
            if (estaListo()) {
                return;
            }
            tiempoActual = Math.min(intervaloSegundos, tiempoActual + dt);
            if (estaListo()) {
                notificar(1.0);
            }
        }
    }
}
