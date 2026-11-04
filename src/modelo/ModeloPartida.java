package modelo;

import modelo.enemigo.Enemigo;
import modelo.enemigo.SpawnerEnemigos;
import motor.util.observer.Notificador;

public interface ModeloPartida {
    Jugador getJugador();
    Notificador<EstadoOleadas> getNotificadorOleadas();
    Notificador<Integer> getNotificadorSonido();
    Notificador<Enemigo> getNotificadorEnemigoGenerado();
    SpawnerEnemigos getSpawnerEnemigos();
    boolean isEnPausa();
    void setEnPausa(boolean enPausa);
    boolean isEleccionMejoraActiva();
    void iniciarEleccionMejora();
    void confirmarEleccionMejora();
    boolean isPartidaFinalizada();
    void finalizarPartida();
    boolean actualizarOleadas(double dt);
}
