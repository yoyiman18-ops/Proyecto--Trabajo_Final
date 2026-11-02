package modelo;

import motor.util.observer.Notificador;

public interface ModeloPartida {
    Notificador<EstadoOleadas> getNotificadorOleadas();
    Notificador<Integer> getNotificadorSonido();
    boolean isEnPausa();
    void setEnPausa(boolean enPausa);
    boolean isEleccionMejoraActiva();
    boolean isPartidaFinalizada();
    void finalizarPartida();
    double calcularVelocidadEnemigoOleadaActual();
    int calcularEnemigosAGenerar();
    void registrarEnemigoGenerado();
    void registrarEnemigoDerrotado();
    boolean actualizarOleadas(double dt);
}
