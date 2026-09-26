package vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public final class VistaJuego extends BorderPane {

    private final Pane areaJuego = new Pane();
    private final Label vida = crearValor("--/--");
    private final Label progreso = crearValor("-- | --/--");
    private final Label tiempo = crearValor("--:--");
    private final Label enemigos = crearValor("--");

    public VistaJuego() {
        Label titulo = new Label("WAVES 2D");
        titulo.getStyleClass().add("game-title");

        FlowPane indicadores = new FlowPane(10, 10,
                crearTarjeta("VIDA", vida),
                crearTarjeta("NIVEL / XP", progreso),
                crearTarjeta("TIEMPO", tiempo),
                crearTarjeta("ENEMIGOS", enemigos));
        indicadores.setAlignment(Pos.CENTER_LEFT);
        indicadores.setPadding(new Insets(12, 0, 0, 0));
        indicadores.setMaxWidth(Double.MAX_VALUE);

        VBox interfaz = new VBox(titulo, indicadores);
        interfaz.getStyleClass().add("game-hud");
        interfaz.setPadding(new Insets(18, 20, 18, 20));
        interfaz.setMaxWidth(Double.MAX_VALUE);

        areaJuego.getStyleClass().add("game-area");
        areaJuego.setMinSize(0, 0);

        setTop(interfaz);
        setCenter(areaJuego);
        getStyleClass().add("game-root");

        var hojaEstilos = VistaJuego.class.getResource("vista-juego.css");
        if (hojaEstilos == null) {
            throw new IllegalStateException("No se encontró vista-juego.css");
        }
        getStylesheets().add(hojaEstilos.toExternalForm());
    }

    public Pane getAreaJuego() {
        return areaJuego;
    }

    public void actualizarVida(int actual, int maxima) {
        vida.setText(actual + "/" + maxima);
    }

    public void actualizarProgreso(int nivel, int experiencia, int experienciaSiguiente) {
        progreso.setText(nivel + " | " + experiencia + "/" + experienciaSiguiente);
    }

    public void actualizarTiempo(long segundos) {
        tiempo.setText(String.format("%02d:%02d", segundos / 60, segundos % 60));
    }

    public void actualizarEnemigos(int cantidad) {
        enemigos.setText(Integer.toString(cantidad));
    }

    private static Label crearValor(String valorInicial) {
        Label valor = new Label(valorInicial);
        valor.getStyleClass().add("hud-value");
        return valor;
    }

    private static VBox crearTarjeta(String titulo, Label valor) {
        Label etiqueta = new Label(titulo);
        etiqueta.getStyleClass().add("hud-label");
        VBox tarjeta = new VBox(4, etiqueta, valor);
        tarjeta.getStyleClass().add("hud-card");
        tarjeta.setMinWidth(125);
        tarjeta.setPrefWidth(150);
        tarjeta.setMaxWidth(Double.MAX_VALUE);
        return tarjeta;
    }
}
