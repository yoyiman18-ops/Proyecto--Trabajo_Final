package vista;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Vista principal de la pantalla de juego y su interfaz gráfica (HUD, menús de pausa, mejoras y fin de partida).
 * Cumple con MVC puro: solo contiene elementos y lógica de presentación,
 * sin acoplamiento a las clases del modelo de dominio.
 */
public class VistaJuego extends StackPane {
    public interface Eventos {
        void solicitarPausa();
        void solicitarReanudacion();
        void solicitarVolverAlMenuDesdePausa();
        void seleccionarMejora(int indice);
        void solicitarReintento();
        void solicitarVolverAlMenuDesdeFinPartida();
    }

    private final int vidaPorCorazon = 10;
    private final int cantidadGemasExperiencia = 10;

    private final Pane areaJuego = new Pane();
    private final Label vida = crearValor("100/100");
    private final FlowPane corazones = new FlowPane(3, 3);
    private final List<ImageView> iconosVida = new ArrayList<>();
    private final Image corazonLleno;
    private final Image corazonVacio;
    private final Image iconoExperiencia;
    private final Image gemaExperienciaVacia;
    private final Image gemaExperienciaLlena;
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

    private Eventos eventos;

    public VistaJuego(Map<String, Image> imagenesHud, String hojaEstilosUrl) {
        Objects.requireNonNull(imagenesHud, "El mapa de imágenes de HUD no puede ser nulo");
        Objects.requireNonNull(hojaEstilosUrl, "La URL de la hoja de estilos no puede ser nula");
        this.corazonLleno = imagenesHud.get("corazonLleno");
        this.corazonVacio = imagenesHud.get("corazonVacio");
        this.iconoExperiencia = imagenesHud.get("iconoExperiencia");
        this.gemaExperienciaVacia = imagenesHud.get("gemaExperienciaVacia");
        this.gemaExperienciaLlena = imagenesHud.get("gemaExperienciaLlena");

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
        botonPausar.setOnAction(event -> eventos().solicitarPausa());
        botonPausarMejora.getStyleClass().add("pause-button");
        botonPausarMejora.setOnAction(event -> eventos().solicitarPausa());

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
        botonReanudar.setOnAction(event -> eventos().solicitarReanudacion());
        botonSalirPausa.setOnAction(event -> eventos().solicitarVolverAlMenuDesdePausa());

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
        botonReintentar.setOnAction(event -> eventos().solicitarReintento());
        botonVolverMenu.setOnAction(event -> eventos().solicitarVolverAlMenuDesdeFinPartida());

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

        getStylesheets().add(hojaEstilosUrl);
    }

    private Eventos eventos() {
        return Objects.requireNonNull(eventos, "Los eventos de VistaJuego no han sido configurados");
    }

    public Pane getAreaJuego() {
        return areaJuego;
    }

    public void configurarEventos(Eventos eventos) {
        this.eventos = Objects.requireNonNull(eventos, "Los eventos de VistaJuego no pueden ser nulos");
    }

    public void setPausaHabilitada(boolean habilitada) {
        botonPausar.setDisable(!habilitada);
        botonPausarMejora.setDisable(!habilitada);
    }

    public void mostrarFinPartida(String mensaje) {
        textoFinPartida.setText(Objects.requireNonNull(mensaje, "El mensaje de fin de partida no puede ser nulo"));
        panelFinPartida.setManaged(true);
        panelFinPartida.setVisible(true);
    }

    public void mostrarEleccionMejora(
        int nivel,
        List<String> descripcionesMejoras
    ) {
        Objects.requireNonNull(descripcionesMejoras, "Las descripciones de mejoras no pueden ser nulas");
        Label titulo = new Label("¡NIVEL " + nivel + "!");
        titulo.getStyleClass().add("game-over-title");
        VBox opciones = new VBox(14);
        opciones.setAlignment(Pos.CENTER);
        opciones.setMaxWidth(340);
        opciones.getChildren().add(titulo);

        for (int indice = 0; indice < descripcionesMejoras.size(); indice++) {
            final int seleccion = indice;
            Button opcion = new Button(descripcionesMejoras.get(indice));
            opcion.getStyleClass().add("game-over-button");
            opcion.setMaxWidth(Double.MAX_VALUE);
            opcion.setOnAction(event -> eventos().seleccionarMejora(seleccion));
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

    public void cerrarPausa() {
        panelPausa.setVisible(false);
        panelPausa.setManaged(false);
    }

    public void mostrarPausa() {
        panelPausa.setManaged(true);
        panelPausa.setVisible(true);
    }

    public void cerrarEleccionMejora() {
        panelEleccionMejora.setVisible(false);
        panelEleccionMejora.setManaged(false);
    }

    public void actualizarVida(double actual, double maxima) {
        int maximaValida = Math.max(0, (int) Math.round(maxima));
        int vidaValida = Math.max(0, Math.min((int) Math.round(actual), maximaValida));
        int corazonesMaximos = maximaValida == 0
            ? 0
            : 1 + (maximaValida - 1) / vidaPorCorazon;
        int corazonesLlenos = vidaValida == 0
            ? 0
            : 1 + (vidaValida - 1) / vidaPorCorazon;

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
        int gemasLlenas = (int) Math.round(progreso * cantidadGemasExperiencia);
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

    private Label crearValor(String valorInicial) {
        Label valor = new Label(valorInicial);
        valor.getStyleClass().add("hud-value");
        return valor;
    }

    private VBox crearIndicador(String titulo, Node valor,
                                double anchoMinimo, double anchoPreferido) {
        Label etiqueta = new Label(titulo);
        etiqueta.getStyleClass().add("hud-label");
        VBox tarjeta = new VBox(4, etiqueta, valor);
        tarjeta.setMinWidth(anchoMinimo);
        tarjeta.setPrefWidth(anchoPreferido);
        tarjeta.setMaxWidth(Double.MAX_VALUE);
        return tarjeta;
    }

    private FlowPane crearGrupoHud(double anchoSalto, Node... indicadores) {
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

        for (int indice = 0; indice < cantidadGemasExperiencia; indice++) {
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
}
