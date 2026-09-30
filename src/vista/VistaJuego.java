package vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

import java.util.ArrayList;
import java.util.List;

public class VistaJuego extends StackPane {
    private static final int VIDA_POR_CORAZON = 10;

    private final Pane areaJuego = new Pane();
    private final Label vida = crearValor("100/100");
    private final FlowPane corazones = new FlowPane(3, 3);
    private final List<ImageView> iconosVida = new ArrayList<>();
    private final Image corazonLleno = cargarImagen("led-heart-red.png");
    private final Image corazonVacio = cargarImagen("led-heart.png");
    private final Image iconoExperiencia = cargarImagen("hud_xp.png");
    private final Image placaExperiencia = cargarImagen("hud_placa_madera.png");
    private final Label nivelExperiencia = new Label("NIVEL --");
    private final Label valorExperiencia = crearValor("-- / -- XP");
    private final ProgressBar barraExperiencia = new ProgressBar(0);
    private final Label tiempo = crearValor("--:--");
    private final Label oleada = crearValor("--");
    private final Label enemigos = crearValor("--");
    private final Label danio = crearValor("--");
    private final Label puntos = crearValor("--/--");

    public VistaJuego() {
        corazones.setPrefWrapLength(125);
        FlowPane indicadores = new FlowPane(8, 8,
            crearIndicador("VIDA", new VBox(5,
                new HBox(6, corazones, vida),
                crearPanelExperiencia()), 205, 215),
                crearIndicador("TIEMPO", tiempo, 95, 110),
                crearIndicador("OLEADA", oleada, 85, 95),
                crearIndicador("ENEMIGOS", enemigos, 100, 110),
                crearIndicador("DAÑO", danio, 85, 95),
                crearIndicador("PUNTOS", puntos, 90, 100));
        indicadores.setAlignment(Pos.CENTER_LEFT);
        indicadores.setPadding(new Insets(8, 0, 0, 0));
        indicadores.setMaxWidth(Double.MAX_VALUE);

        VBox interfaz = new VBox(indicadores);
        interfaz.getStyleClass().add("game-hud");
        interfaz.setPadding(new Insets(14));
        interfaz.setMaxWidth(Double.MAX_VALUE);

        areaJuego.getStyleClass().add("game-area");
        areaJuego.setMinSize(0, 0);
        areaJuego.setFocusTraversable(true);

        getChildren().addAll(areaJuego, interfaz);
        StackPane.setAlignment(interfaz, Pos.TOP_LEFT);
        getStyleClass().add("game-root");

        var hojaEstilos = VistaJuego.class.getResource("vista-juego.css");
        if (hojaEstilos == null) {
            throw new IllegalStateException("No se encontró vista-juego.css");
        }
        getStylesheets().add(hojaEstilos.toExternalForm());
        actualizarVida(100, 100);
    }

    public Pane getAreaJuego() {
        return areaJuego;
    }

    public void actualizarVida(int actual, int maxima) {
        int maximaValida = Math.max(0, maxima);
        int vidaValida = Math.max(0, Math.min(actual, maximaValida));
        int corazonesMaximos = maximaValida == 0
            ? 0
            : 1 + (maximaValida - 1) / VIDA_POR_CORAZON;
        int corazonesLlenos = vidaValida == 0
            ? 0
            : 1 + (vidaValida - 1) / VIDA_POR_CORAZON;

        while (iconosVida.size() < corazonesMaximos) {
            ImageView corazon = new ImageView();
            corazon.setFitWidth(22);
            corazon.setFitHeight(22);
            corazon.setPreserveRatio(true);
            iconosVida.add(corazon);
            corazones.getChildren().add(corazon);
        }
        while (iconosVida.size() > corazonesMaximos) {
            corazones.getChildren().remove(iconosVida.remove(iconosVida.size() - 1));
        }
        for (int indice = 0; indice < iconosVida.size(); indice++) {
            iconosVida.get(indice).setImage(indice < corazonesLlenos ? corazonLleno : corazonVacio);
        }
        vida.setText(vidaValida + "/" + maximaValida);
    }

    public void actualizarProgreso(int nivel, int experiencia, int experienciaSiguiente) {
        int objetivo = Math.max(0, experienciaSiguiente);
        int actual = Math.max(0, Math.min(experiencia, objetivo));
        nivelExperiencia.setText("NIVEL " + nivel);
        valorExperiencia.setText(actual + " / " + objetivo + " XP");
        barraExperiencia.setProgress(objetivo == 0 ? 0 : (double) actual / objetivo);
    }

    public void actualizarTiempo(long segundos) {
        tiempo.setText(String.format("%02d:%02d", segundos / 60, segundos % 60));
    }

    public void actualizarEnemigos(int cantidad) {
        enemigos.setText(Integer.toString(cantidad));
    }

    public void actualizarOleada(int numero) {
        oleada.setText(Integer.toString(numero));
    }

    public void actualizarDanio(int valor) {
        danio.setText(Integer.toString(valor));
    }

    public void actualizarPuntos(int actual, int siguiente) {
        puntos.setText(actual + "/" + siguiente);
    }

    private static Label crearValor(String valorInicial) {
        Label valor = new Label(valorInicial);
        valor.getStyleClass().add("hud-value");
        return valor;
    }

    private static VBox crearIndicador(String titulo, Node valor,
                                       double anchoMinimo, double anchoPreferido) {
        Label etiqueta = new Label(titulo);
        etiqueta.getStyleClass().add("hud-label");
        VBox tarjeta = new VBox(4, etiqueta, valor);
        tarjeta.setMinWidth(anchoMinimo);
        tarjeta.setPrefWidth(anchoPreferido);
        tarjeta.setMaxWidth(Double.MAX_VALUE);
        return tarjeta;
    }

    private StackPane crearPanelExperiencia() {
        ImageView placa = new ImageView(placaExperiencia);
        placa.setViewport(new Rectangle2D(0, 30, 285, 60));
        placa.setFitWidth(180);
        placa.setPreserveRatio(true);

        ImageView icono = new ImageView(iconoExperiencia);
        icono.setFitWidth(22);
        icono.setFitHeight(22);
        icono.setPreserveRatio(true);

        nivelExperiencia.getStyleClass().add("xp-level");
        valorExperiencia.getStyleClass().add("xp-value");
        barraExperiencia.getStyleClass().add("xp-progress");
        barraExperiencia.setMaxWidth(Double.MAX_VALUE);

        HBox etiquetas = new HBox(nivelExperiencia, valorExperiencia);
        etiquetas.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(nivelExperiencia, Priority.ALWAYS);

        VBox informacion = new VBox(3, etiquetas, barraExperiencia);
        HBox contenido = new HBox(8, icono, informacion);
        contenido.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(informacion, Priority.ALWAYS);

        StackPane panel = new StackPane(placa, contenido);
        panel.setMinSize(180, 56);
        panel.setPrefSize(180, 56);
        StackPane.setMargin(contenido, new Insets(7, 10, 7, 10));
        return panel;
    }

    private static Image cargarImagen(String nombre) {
        var recurso = VistaJuego.class.getResource("/recursos/imagenes/" + nombre);
        if (recurso == null) {
            throw new IllegalStateException("No se encontró el recurso de HUD: " + nombre);
        }
        return new Image(recurso.toExternalForm());
    }
}
