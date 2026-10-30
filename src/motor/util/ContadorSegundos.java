package motor.util;
import motor.modelo.Ticking;
import motor.util.observer.NotificadorDebil;

public class ContadorSegundos 
extends NotificadorDebil<Double> 
implements Ticking {

    private double intervaloSegundos;
    private double tiempoActual;

    public ContadorSegundos() {
        this(1.0);
    }

    public ContadorSegundos(double intervaloSegundos) {
        if (intervaloSegundos <= 0) { throw new IllegalArgumentException("El intervalo no puede ser igual o menor a 0s"); }
        this.intervaloSegundos = intervaloSegundos;
        this.tiempoActual = 0.0;
    }

    public void reset() { this.tiempoActual = 0; }
    public void setIntervaloSegundos(double intervaloSegundos) {
        if (intervaloSegundos <= 0) { throw new IllegalArgumentException("El intervalo no puede ser igual o menor a 0s"); }
        this.intervaloSegundos = intervaloSegundos;
    }

    @Override
    public void tick(double dt) {
        tiempoActual += dt;
        double cociente = tiempoActual / intervaloSegundos;
        if (cociente >= 1) {
            tiempoActual -= cociente * intervaloSegundos;
            notificar(cociente);
            }
        }
}

