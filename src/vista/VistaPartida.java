package vista;

import java.util.List;
import java.util.function.IntConsumer;

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

    // Callbacks de interacción del usuario (estilo funcional / MVC pasivo)
    void setOnPausa(Runnable accion);
    void setOnReanudar(Runnable accion);
    void setOnVolverAlMenuDesdePausa(Runnable accion);
    void setOnSeleccionarMejora(IntConsumer accion);
    void setOnReintentar(Runnable accion);
    void setOnVolverAlMenuDesdeFinPartida(Runnable accion);

    void setPausaHabilitada(boolean habilitada);
    void mostrarFinPartida(String mensaje);
    void mostrarEleccionMejora(int nivel, List<OpcionMejoraVisual> opciones);
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
