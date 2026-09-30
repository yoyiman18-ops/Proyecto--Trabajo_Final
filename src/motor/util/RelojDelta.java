package motor.util;

import javafx.animation.AnimationTimer;
import motor.util.observer.Notificador;
import motor.util.observer.Observador;

public class RelojDelta {
    private final AnimationTimer temporizador;
    private final Notificador<Double> notificador;
    private long ultimoTiempo;   

    public RelojDelta(Notificador<Double> notificador) {
        this.notificador = notificador;
        this.ultimoTiempo = 0;
        this.temporizador = new AnimationTimer() {
        @Override
        public void handle(long ahora) {
            if (ultimoTiempo == 0) { ultimoTiempo = ahora; return; }
            double deltaTiempoSegundos = (ahora - ultimoTiempo) / 1_000_000_000.0;
            RelojDelta.this.notificador.notificar(deltaTiempoSegundos);
            ultimoTiempo = ahora;
            }
        };
    }

    public void suscribirObservador(Observador<Double> o) { this.notificador.suscribirObservador(o); }
    public void desuscribirObservador(Observador<Double> o) { this.notificador.desuscribirObservador(o); }
    public void iniciar() { this.temporizador.start(); }
    public void detener() { this.temporizador.stop(); ultimoTiempo = 0; }
}
