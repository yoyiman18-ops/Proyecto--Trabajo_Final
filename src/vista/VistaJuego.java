package vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public final class VistaJuego extends BorderPane {

    private final Pane areaJuego = new Pane();
    private final Label vida = crearValor("Sin jugador");
    private final HBox corazones = new HBox(4);
    private final List<ImageView> iconosVida = new ArrayList<>();
    private final Image corazonLleno = cargarCorazon("led-heart-red.png");
    private final Image corazonVacio = cargarCorazon("led-heart.png");
    private final Label progreso = crearValor("-- | --/--");
    private final Label tiempo = crearValor("--:--");
    private final Label enemigos = crearValor("--");

    public VistaJuego() {
        Label titulo = new Label("WAVES 2D");
        titulo.getStyleClass().add("game-title");

        FlowPane indicadores = new FlowPane(10, 10,
            crearTarjeta("VIDA", new HBox(8, corazones, vida), 280, 320),
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
        areaJuego.setFocusTraversable(true);

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
        int maximaValida = Math.max(0, maxima);
        int vidaValida = Math.max(0, Math.min(actual, maximaValida));

        while (iconosVida.size() < maximaValida) {
            ImageView corazon = new ImageView();
            corazon.setFitWidth(22);
            corazon.setFitHeight(22);
            corazon.setPreserveRatio(true);
            iconosVida.add(corazon);
            corazones.getChildren().add(corazon);
        }
        while (iconosVida.size() > maximaValida) {
            corazones.getChildren().remove(iconosVida.remove(iconosVida.size() - 1));
        }
        for (int indice = 0; indice < iconosVida.size(); indice++) {
            iconosVida.get(indice).setImage(indice < vidaValida ? corazonLleno : corazonVacio);
        }
        vida.setText(vidaValida + "/" + maximaValida);
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

    private static VBox crearTarjeta(String titulo, Node valor) {
        return crearTarjeta(titulo, valor, 125, 150);
    }

    private static VBox crearTarjeta(String titulo, Node valor,
                                     double anchoMinimo, double anchoPreferido) {
        Label etiqueta = new Label(titulo);
        etiqueta.getStyleClass().add("hud-label");
        VBox tarjeta = new VBox(4, etiqueta, valor);
        tarjeta.getStyleClass().add("hud-card");
        tarjeta.setMinWidth(anchoMinimo);
        tarjeta.setPrefWidth(anchoPreferido);
        tarjeta.setMaxWidth(Double.MAX_VALUE);
        return tarjeta;
    }

    private static Image cargarCorazon(String nombre) {
        var recurso = VistaJuego.class.getResource("/recursos/imagenes/" + nombre);
        if (recurso == null) {
            throw new IllegalStateException("No se encontró el recurso de vida: " + nombre);
        }
        return new Image(recurso.toExternalForm());
    }
}
