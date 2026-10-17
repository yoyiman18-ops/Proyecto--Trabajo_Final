package modelo;

import java.util.List;

import motor.EstadoOleadas;
import motor.util.observer.Notificador;

public interface ModeloPartida {
    Notificador<EstadoOleadas> getNotificadorOleadas();
    int getOleadaActual();
    int getCantidadOleadas();
    int getMaxEnemigosSimultaneos();
    int getEnemigosActivos();
    int getEnemigosRestantesOleada();
    int getEnemigosPorAparecer();
    double getProbabilidadMejoraAtributo();
    double getAumentoVidaMaxima();
    double getDistanciaMaxAtaque();
    boolean isOleadasCompletadas();
    boolean isEnPausa();
    void setEnPausa(boolean enPausa);
    boolean isEleccionMejoraActiva();
    boolean isPartidaFinalizada();
    void finalizarPartida();
    List<ItemMejoraAtributo> getOpcionesMejoraActuales();
    List<ItemMejoraAtributo> iniciarEleccionMejora();
    ItemMejoraAtributo consumirOpcionMejora(int indice);
    double calcularVelocidadEnemigoOleadaActual();
    int calcularEnemigosAGenerar();
    void registrarEnemigoGenerado();
    void registrarEnemigoDerrotado();
    boolean actualizarOleadas(double dt);
}
