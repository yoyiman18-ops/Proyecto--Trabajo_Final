package motor.util;

import javafx.animation.AnimationTimer;
import motor.util.Observer.NotificadorDebil;

public class RelojDelta extends NotificadorDebil<DoubleSupplier> {
    private final AnimationTimer temporizador;
    private long ultimoTiempo;   
    private final DoubleSupplier a;

    public RelojDelta() {
        this.ultimoTiempo = 0;
        this.temporizador = new AnimationTimer() {
        @Override
        public void handle(long ahora) {
            if (ultimoTiempo == 0) { ultimoTiempo = ahora; return; }
            double deltaTiempoSegundos = (ahora - ultimoTiempo) / 1_000_000_000.0;
            notificar(DoubleSupplier.deltaTiempoSegundos);
            ultimoTiempo = ahora;
            }
        };
    }

    public void iniciar() { this.temporizador.start(); }
    public void detener() { this.temporizador.stop(); ultimoTiempo = 0; }
}
