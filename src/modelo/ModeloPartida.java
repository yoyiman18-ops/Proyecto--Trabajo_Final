package modelo;

import java.util.List;

import motor.EstadoOleadas;
import motor.util.observer.Notificador;

public interface ModeloPartida {
    Notificador<EstadoOleadas> getNotificadorOleadas();
    boolean isEnPausa();
    void setEnPausa(boolean enPausa);
    boolean isEleccionMejoraActiva();
    boolean isPartidaFinalizada();
    void finalizarPartida();
    List<ItemMejoraAtributo> iniciarEleccionMejora();
    ItemMejoraAtributo consumirOpcionMejora(int indice);
    double calcularVelocidadEnemigoOleadaActual();
    int calcularEnemigosAGenerar();
    void registrarEnemigoGenerado();
    void registrarEnemigoDerrotado();
    boolean actualizarOleadas(double dt);
}
