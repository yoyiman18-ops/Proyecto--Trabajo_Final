package modelo;

import java.util.List;

import modelo.enemigo.Enemigo;
import modelo.enemigo.SpawnerEnemigos;
import motor.util.observer.Notificador;

public interface ModeloPartida {
    Jugador getJugador();
    void setJugador(Jugador jugador);
    List<Enemigo> getEnemigosActivos();
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
    double calcularVelocidadEnemigoOleadaActual();
    int calcularEnemigosAGenerar();
    void registrarEnemigoGenerado();
    boolean registrarEnemigoGenerado(Enemigo enemigo);
    void registrarEnemigoDerrotado();
    boolean registrarEnemigoDerrotado(Enemigo enemigo);
    boolean actualizarOleadas(double dt);
}
