package vista;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import modelo.ItemMejoraAtributo;

public class VistaJuego extends StackPane {
    private static final int VIDA_POR_CORAZON = 10;
    private static final int CANTIDAD_GEMAS_EXPERIENCIA = 10;

    private final Pane areaJuego = new Pane();
    private final Label vida = crearValor("100/100");
    private final FlowPane corazones = new FlowPane(3, 3);
    private final List<ImageView> iconosVida = new ArrayList<>();
    private final Image corazonLleno = cargarImagen("led-heart-red.png");
    private final Image corazonVacio = cargarImagen("led-heart.png");
    private final Image iconoExperiencia = cargarImagen("hud_xp.png");
    private final Image gemaExperienciaVacia = cargarImagen("hud_gema_gris.png");
    private final Image gemaExperienciaLlena = cargarImagen("hud_gema_verde.png");
    private final Label nivelExperiencia = new Label("NIVEL --");
    private final Label valorExperiencia = crearValor("-- / -- XP");
    private final HBox gemasExperiencia = new HBox(2);
    private final List<ImageView> iconosExperiencia = new ArrayList<>();
    private final Label tiempo = crearValor("--:--");
    private final Label oleada = crearValor("--");
    private final Label enemigos = crearValor("--");
    private final Label danio = crearValor("--");
    private final Label puntos = crearValor("--/--");
    private final VBox indicadorPuntos = crearIndicador("PUNTOS", puntos, 90, 100);
    private final Button botonPausar = new Button("PAUSA");
    private final Button botonPausarMejora = new Button("PAUSA");
    private final Label textoPausa = new Label("JUEGO EN PAUSA");
    private final Button botonReanudar = new Button("CONTINUAR");
    private final Button botonSalirPausa = new Button("VOLVER AL MENÚ");
    private final StackPane panelPausa = new StackPane();
    private final Label textoFinPartida = new Label("FIN DE LA PARTIDA");
    private final Button botonReintentar = new Button("REINTENTAR");
    private final Button botonVolverMenu = new Button("VOLVER AL MENÚ");
    private final StackPane panelFinPartida = new StackPane();
    private final StackPane panelEleccionMejora = new StackPane();
    private Runnable alPausar = () -> {};
    private Runnable alReanudar = () -> {};
    private Runnable alVolverAlMenuDesdePausa = () -> {};

    public VistaJuego() {
        corazones.setPrefWrapLength(190);
        FlowPane grupoJugador = crearGrupoHud(270,
            crearIndicador("VIDA", new VBox(5,
                new HBox(6, corazones, vida),
                crearPanelExperiencia()), 245, 255));
        FlowPane grupoPartida = crearGrupoHud(340,
            crearIndicador("TIEMPO", tiempo, 95, 110),
            crearIndicador("OLEADA", oleada, 85, 95),
            crearIndicador("ENEMIGOS", enemigos, 100, 110));
        FlowPane grupoCombate = crearGrupoHud(220,
            crearIndicador("DAÑO", danio, 85, 95),
            indicadorPuntos);
        indicadorPuntos.setVisible(false);
        indicadorPuntos.setManaged(false);

        FlowPane indicadores = new FlowPane(10, 8, grupoJugador, grupoPartida, grupoCombate);
        indicadores.setAlignment(Pos.CENTER_LEFT);
        indicadores.setPadding(new Insets(8, 0, 0, 0));
        indicadores.setMaxWidth(Double.MAX_VALUE);

        VBox interfaz = new VBox();
        interfaz.getStyleClass().add("game-hud");
        interfaz.setPadding(new Insets(14));
        interfaz.setMaxWidth(Double.MAX_VALUE);

        botonPausar.getStyleClass().add("pause-button");
        HBox barraHud = new HBox(12, indicadores, botonPausar);
        barraHud.setAlignment(Pos.TOP_LEFT);
        HBox.setHgrow(indicadores, Priority.ALWAYS);
        interfaz.getChildren().setAll(barraHud);
        botonPausar.setOnAction(event -> abrirPausa());
        botonPausarMejora.getStyleClass().add("pause-button");
        botonPausarMejora.setOnAction(event -> abrirPausa());

        panelPausa.getStyleClass().add("pause-overlay");
        panelPausa.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        textoPausa.getStyleClass().add("game-over-title");
        botonReanudar.getStyleClass().add("game-over-button");
        botonSalirPausa.getStyleClass().add("game-over-button");
        botonReanudar.setMaxWidth(Double.MAX_VALUE);
        botonSalirPausa.setMaxWidth(Double.MAX_VALUE);
        VBox opcionesPausa = new VBox(14, textoPausa, botonReanudar, botonSalirPausa);
        opcionesPausa.setAlignment(Pos.CENTER);
        opcionesPausa.setMaxWidth(280);
        panelPausa.getChildren().add(opcionesPausa);
        panelPausa.setVisible(false);
        panelPausa.setManaged(false);
        botonReanudar.setOnAction(event -> {
            cerrarPausa();
            if (!panelEleccionMejora.isVisible()) {
                alReanudar.run();
            }
        });
        botonSalirPausa.setOnAction(event -> alVolverAlMenuDesdePausa.run());

        areaJuego.getStyleClass().add("game-area");
        areaJuego.setMinSize(0, 0);
        areaJuego.setFocusTraversable(true);

        panelFinPartida.getStyleClass().add("game-over-overlay");
        panelFinPartida.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        textoFinPartida.getStyleClass().add("game-over-title");
        botonReintentar.getStyleClass().add("game-over-button");
        botonVolverMenu.getStyleClass().add("game-over-button");
        botonReintentar.setMaxWidth(Double.MAX_VALUE);
        botonVolverMenu.setMaxWidth(Double.MAX_VALUE);
        VBox opcionesFinPartida = new VBox(14,
            textoFinPartida,
            botonReintentar,
            botonVolverMenu);
        opcionesFinPartida.setAlignment(Pos.CENTER);
        opcionesFinPartida.setMaxWidth(280);
        panelFinPartida.getChildren().add(opcionesFinPartida);
        panelFinPartida.setVisible(false);
        panelFinPartida.setManaged(false);

        panelEleccionMejora.getStyleClass().add("level-up-overlay");
        panelEleccionMejora.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        panelEleccionMejora.setVisible(false);
        panelEleccionMejora.setManaged(false);

        BorderPane contenido = new BorderPane();
        contenido.setMinSize(0, 0);
        contenido.setTop(interfaz);
        contenido.setCenter(areaJuego);

        getChildren().addAll(contenido, panelEleccionMejora, panelPausa, panelFinPartida);
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

    public void configurarPausa(Runnable alPausar, Runnable alReanudar, Runnable volverAlMenu) {
        this.alPausar = alPausar;
        this.alReanudar = alReanudar;
        this.alVolverAlMenuDesdePausa = volverAlMenu;
    }

    public void mostrarFinPartida(String mensaje, Runnable reintentar, Runnable volverMenu) {
        cerrarPausa();
        panelEleccionMejora.setVisible(false);
        panelEleccionMejora.setManaged(false);
        botonPausar.setDisable(true);
        textoFinPartida.setText(mensaje);
        botonReintentar.setOnAction(event -> reintentar.run());
        botonVolverMenu.setOnAction(event -> volverMenu.run());
        panelFinPartida.setManaged(true);
        panelFinPartida.setVisible(true);
    }

    public void mostrarEleccionMejora(
        int nivel,
        List<ItemMejoraAtributo> mejoras,
        Consumer<ItemMejoraAtributo> alElegir
    ) {
        if (panelFinPartida.isVisible()) {
            return;
        }
        Label titulo = new Label("¡NIVEL " + nivel + "!");
        titulo.getStyleClass().add("game-over-title");
        VBox opciones = new VBox(14);
        opciones.setAlignment(Pos.CENTER);
        opciones.setMaxWidth(340);
        opciones.getChildren().add(titulo);

        for (ItemMejoraAtributo mejora : mejoras) {
            Button opcion = new Button(mejora.descripcion());
            opcion.getStyleClass().add("game-over-button");
            opcion.setMaxWidth(Double.MAX_VALUE);
            opcion.setOnAction(event -> {
                panelEleccionMejora.setVisible(false);
                panelEleccionMejora.setManaged(false);
                alElegir.accept(mejora);
            });
            opciones.getChildren().add(opcion);
        }

        BorderPane contenidoEleccion = new BorderPane(opciones);
        contenidoEleccion.setTop(botonPausarMejora);
        BorderPane.setAlignment(botonPausarMejora, Pos.TOP_RIGHT);
        BorderPane.setMargin(botonPausarMejora, new Insets(18));
        panelEleccionMejora.getChildren().setAll(contenidoEleccion);
        panelEleccionMejora.setManaged(true);
        panelEleccionMejora.setVisible(true);
    }

    private void cerrarPausa() {
        panelPausa.setVisible(false);
        panelPausa.setManaged(false);
    }

    private void abrirPausa() {
        if (panelPausa.isVisible() || panelFinPartida.isVisible()) {
            return;
        }
        panelPausa.setManaged(true);
        panelPausa.setVisible(true);
        alPausar.run();
    }

    public void actualizarVida(double actual, double maxima) {
        int maximaValida = Math.max(0, (int) Math.round(maxima));
        int vidaValida = Math.max(0, Math.min((int) Math.round(actual), maximaValida));
        int corazonesMaximos = maximaValida == 0
            ? 0
            : 1 + (maximaValida - 1) / VIDA_POR_CORAZON;
        int corazonesLlenos = vidaValida == 0
            ? 0
            : 1 + (vidaValida - 1) / VIDA_POR_CORAZON;

        while (iconosVida.size() < corazonesMaximos) {
            ImageView corazon = new ImageView();
            corazon.setFitWidth(16);
            corazon.setFitHeight(16);
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
        double progreso = objetivo == 0 ? 0 : (double) actual / objetivo;
        int gemasLlenas = (int) Math.round(progreso * CANTIDAD_GEMAS_EXPERIENCIA);
        for (int indice = 0; indice < iconosExperiencia.size(); indice++) {
            iconosExperiencia.get(indice).setImage(
                indice < gemasLlenas ? gemaExperienciaLlena : gemaExperienciaVacia);
        }
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
        indicadorPuntos.setManaged(true);
        indicadorPuntos.setVisible(true);
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

    private static FlowPane crearGrupoHud(double anchoSalto, Node... indicadores) {
        FlowPane grupo = new FlowPane(8, 6, indicadores);
        grupo.getStyleClass().add("hud-group");
        grupo.setAlignment(Pos.CENTER_LEFT);
        grupo.setMinWidth(0);
        grupo.setPrefWrapLength(anchoSalto);
        grupo.setMaxWidth(Double.MAX_VALUE);
        return grupo;
    }

    private HBox crearPanelExperiencia() {
        ImageView icono = new ImageView(iconoExperiencia);
        icono.setFitWidth(22);
        icono.setFitHeight(22);
        icono.setPreserveRatio(true);

        for (int indice = 0; indice < CANTIDAD_GEMAS_EXPERIENCIA; indice++) {
            ImageView gema = new ImageView(gemaExperienciaVacia);
            gema.setFitWidth(11);
            gema.setFitHeight(11);
            gema.setPreserveRatio(true);
            iconosExperiencia.add(gema);
            gemasExperiencia.getChildren().add(gema);
        }

        nivelExperiencia.getStyleClass().add("xp-level");
        valorExperiencia.getStyleClass().add("xp-value");

        HBox etiquetas = new HBox(nivelExperiencia, valorExperiencia);
        etiquetas.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(nivelExperiencia, Priority.ALWAYS);

        VBox informacion = new VBox(3, etiquetas, gemasExperiencia);
        HBox contenido = new HBox(8, icono, informacion);
        contenido.setAlignment(Pos.CENTER_LEFT);
        contenido.setMinSize(180, 56);
        contenido.setPrefSize(180, 56);
        HBox.setHgrow(informacion, Priority.ALWAYS);
        return contenido;
    }

    private static Image cargarImagen(String nombre) {
        var recurso = VistaJuego.class.getResource("/recursos/imagenes/" + nombre);
        if (recurso == null) {
            throw new IllegalStateException("No se encontró el recurso de HUD: " + nombre);
        }
        return new Image(recurso.toExternalForm());
    }
}
