package motor.util;

import javafx.animation.AnimationTimer;
import motor.util.Observer.NotificadorDebil;

public class RelojDelta extends NotificadorDebil<Double> {
    private final AnimationTimer temporizador;
    private long ultimoTiempo;   

    public RelojDelta() {
        this.ultimoTiempo = 0;
        this.temporizador = new AnimationTimer() {
        @Override
        public void handle(long ahora) {
            if (ultimoTiempo == 0) { ultimoTiempo = ahora; return; }
            double deltaTiempoSegundos = (ahora - ultimoTiempo) / 1_000_000_000.0;
            notificar(deltaTiempoSegundos);
            ultimoTiempo = ahora;
            }
        };
    }

    public void iniciar() { this.temporizador.start(); }
    public void detener() { this.temporizador.stop(); ultimoTiempo = 0; }
}
