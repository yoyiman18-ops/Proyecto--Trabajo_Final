package motor.util;

import javafx.animation.AnimationTimer;

public class RelojDelta extends Notificador<Double> {
    private final AnimationTimer temporizador;
    private long ultimoTiempo = 0;   

    private RelojDelta() {
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
}
