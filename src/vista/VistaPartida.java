package vista;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

public interface VistaPartida {
    Parent getRaiz();
    Pane getAreaJuego();
    double getAnchoAreaJuego();
    double getAltoAreaJuego();
    void agregarNodoJuego(Node nodo);
    void removerNodoJuego(Node nodo);
    void solicitarFoco();
    void configurarEventos(VistaJuego.Eventos eventos);
    void setPausaHabilitada(boolean habilitada);
    void mostrarFinPartida(String mensaje);
    void mostrarEleccionMejora(int nivel, List<String> descripcionesMejoras);
    void cerrarEleccionMejora();
    void mostrarPausa();
    void cerrarPausa();
    void actualizarVida(double actual, double maxima);
    void actualizarProgreso(int nivel, int experiencia, int experienciaSiguiente);
    void actualizarTiempo(long segundos);
    void actualizarEnemigos(int cantidad);
    void actualizarOleada(int numero);
    void actualizarDanio(int valor);
    void actualizarPuntos(int actual, int siguiente);
}
