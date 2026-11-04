package vista;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntConsumer;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import motor.modelo.graficos.Sheet;

/**
 * Vista principal de la pantalla de juego.
 * Coordina el contenedor del mundo ({@code areaJuego}), el HUD ({@link VistaHud})
 * y los paneles modales (pausa, subida de nivel y fin de partida).
 */
public class VistaJuego extends StackPane implements VistaPartida {

    public static final double ANCHO_BOTON_MENU = 260;
    public static final double ALTO_BOTON_MENU = 48;
    public static final double ANCHO_CARTA_MEJORA = 195;
    public static final double ALTO_CARTA_MEJORA = 235;

    private final Pane areaJuego;
    private final VistaHud hud;

    private final Button botonPausarMejora;
    private final Label textoPausa;
    private final Button botonReanudar;
    private final Button botonSalirPausa;
    private final StackPane panelPausa;

    private final Label textoFinPartida;
    private final Button botonReintentar;
    private final Button botonVolverMenu;
    private final StackPane panelFinPartida;

    private final StackPane panelEleccionMejora;

    private IntConsumer onSeleccionarMejora;

    public VistaJuego(Map<String, Image> imagenesHud) {
        this(new VistaHud(imagenesHud, null));
    }

    public VistaJuego(
        Map<String, Image> imagenesHud,
        Sheet recortesGui
    ) {
        this(new VistaHud(imagenesHud, recortesGui));
    }

    public VistaJuego(VistaHud hud) {
        this.hud = Objects.requireNonNull(hud, "El HUD no puede ser nulo");

        this.areaJuego = new Pane();
        this.botonPausarMejora = this.hud.crearBotonPlacaRpg(
            "II  PAUSA",
            "placa_brillante",
            VistaHud.ANCHO_BOTON_PAUSA,
            VistaHud.ALTO_BOTON_PAUSA
        );
        this.botonPausarMejora.setFocusTraversable(false);

        this.textoPausa = new Label("PARTIDA EN PAUSA");
        this.botonReanudar = this.hud.crearBotonPlacaRpg("CONTINUAR", "placa_ancha", ANCHO_BOTON_MENU, ALTO_BOTON_MENU);
        this.botonSalirPausa = this.hud.crearBotonPlacaRpg("VOLVER AL MENÚ", "placa_ancha", ANCHO_BOTON_MENU, ALTO_BOTON_MENU);
        this.panelPausa = new StackPane();

        this.textoFinPartida = new Label("FIN DE LA PARTIDA");
        this.botonReintentar = this.hud.crearBotonPlacaRpg("REINTENTAR", "placa_ancha", ANCHO_BOTON_MENU, ALTO_BOTON_MENU);
        this.botonVolverMenu = this.hud.crearBotonPlacaRpg("VOLVER AL MENÚ", "placa_ancha", ANCHO_BOTON_MENU, ALTO_BOTON_MENU);
        this.panelFinPartida = new StackPane();

        this.panelEleccionMejora = new StackPane();

        StackPane.setAlignment(this.hud, Pos.TOP_CENTER);

        // Paneles modales (Pausa, Fin de partida, Subida de nivel)
        panelPausa.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox opcionesPausa = new VBox(14, textoPausa, botonReanudar, botonSalirPausa);
        opcionesPausa.setAlignment(Pos.CENTER);
        opcionesPausa.setMaxSize(380, Region.USE_PREF_SIZE);
        panelPausa.getChildren().add(opcionesPausa);
        panelPausa.setVisible(false);
        panelPausa.setManaged(false);

        areaJuego.setMinSize(0, 0);
        areaJuego.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        areaJuego.setFocusTraversable(true);

        panelFinPartida.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        VBox opcionesFinPartida = new VBox(14, textoFinPartida, botonReintentar, botonVolverMenu);
        opcionesFinPartida.setAlignment(Pos.CENTER);
        opcionesFinPartida.setMaxSize(420, Region.USE_PREF_SIZE);
        panelFinPartida.getChildren().add(opcionesFinPartida);
        panelFinPartida.setVisible(false);
        panelFinPartida.setManaged(false);

        panelEleccionMejora.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        panelEleccionMejora.setVisible(false);
        panelEleccionMejora.setManaged(false);

        getChildren().addAll(areaJuego, this.hud, panelEleccionMejora, panelPausa, panelFinPartida);
    }

    public VistaHud getHud() {
        return hud;
    }

    @Override
    public Parent getRaiz() {
        return this;
    }

    @Override
    public Pane getAreaJuego() {
        return areaJuego;
    }

    @Override
    public double getAnchoAreaJuego() {
        return areaJuego.getWidth();
    }

    @Override
    public double getAltoAreaJuego() {
        return areaJuego.getHeight();
    }

    @Override
    public void agregarNodoJuego(Node nodo) {
        Objects.requireNonNull(nodo, "El nodo a agregar no puede ser nulo");
        areaJuego.getChildren().add(nodo);
    }

    @Override
    public void removerNodoJuego(Node nodo) {
        Objects.requireNonNull(nodo, "El nodo a remover no puede ser nulo");
        areaJuego.getChildren().remove(nodo);
    }

    @Override
    public void solicitarFoco() {
        requestFocus();
    }

    @Override
    public void setOnPausa(Runnable accion) {
        Objects.requireNonNull(accion, "La acción de pausa no puede ser nula");
        hud.setOnPausa(accion);
        botonPausarMejora.setOnAction(event -> accion.run());
    }

    @Override
    public void setOnReanudar(Runnable accion) {
        Objects.requireNonNull(accion, "La acción de reanudación no puede ser nula");
        botonReanudar.setOnAction(event -> accion.run());
    }

    @Override
    public void setOnVolverAlMenuDesdePausa(Runnable accion) {
        Objects.requireNonNull(accion, "La acción de volver al menú no puede ser nula");
        botonSalirPausa.setOnAction(event -> accion.run());
    }

    @Override
    public void setOnSeleccionarMejora(IntConsumer accion) {
        this.onSeleccionarMejora = Objects.requireNonNull(accion, "La acción de seleccionar mejora no puede ser nula");
    }

    @Override
    public void setOnReintentar(Runnable accion) {
        Objects.requireNonNull(accion, "La acción de reintentar no puede ser nula");
        botonReintentar.setOnAction(event -> accion.run());
    }

    @Override
    public void setOnVolverAlMenuDesdeFinPartida(Runnable accion) {
        Objects.requireNonNull(accion, "La acción de volver al menú no puede ser nula");
        botonVolverMenu.setOnAction(event -> accion.run());
    }

    @Override
    public void setPausaHabilitada(boolean habilitada) {
        hud.setPausaHabilitada(habilitada);
        botonPausarMejora.setDisable(!habilitada);
    }

    @Override
    public void mostrarFinPartida(String mensaje) {
        textoFinPartida.setText(Objects.requireNonNull(mensaje, "El mensaje de fin de partida no puede ser nulo"));
        panelFinPartida.setManaged(true);
        panelFinPartida.setVisible(true);
    }

    @Override
    public void mostrarEleccionMejora(
        int nivel,
        List<OpcionMejoraVisual> descripcionesMejoras
    ) {
        Objects.requireNonNull(descripcionesMejoras, "Las opciones de mejora no pueden ser nulas");
        Label subtitulo = new Label("ELIGE UNA BENDICIÓN ARCANA");
        Label titulo = new Label("¡NIVEL " + nivel + " ALCANZADO!");

        HBox filaCartas = new HBox(18);
        filaCartas.setAlignment(Pos.CENTER);
        for (int indice = 0; indice < descripcionesMejoras.size(); indice++) {
            filaCartas.getChildren().add(
                crearCartaMejora(descripcionesMejoras.get(indice), indice)
            );
        }

        VBox contenedor = new VBox(16, subtitulo, titulo, filaCartas);
        contenedor.setAlignment(Pos.CENTER);
        contenedor.setMaxSize(740, Region.USE_PREF_SIZE);

        BorderPane contenidoEleccion = new BorderPane(contenedor);
        contenidoEleccion.setTop(botonPausarMejora);
        BorderPane.setAlignment(botonPausarMejora, Pos.TOP_RIGHT);
        BorderPane.setMargin(botonPausarMejora, new Insets(14));
        panelEleccionMejora.getChildren().setAll(contenidoEleccion);
        panelEleccionMejora.setManaged(true);
        panelEleccionMejora.setVisible(true);
    }

    private Button crearCartaMejora(OpcionMejoraVisual opcion, int seleccion) {
        String rarezaTexto = opcion.rareza();
        String textoInsignia = rarezaTexto.equals("LEGENDARIA")
            ? "★ LEGENDARIA ★"
            : rarezaTexto;

        Label insigniaRareza = new Label(textoInsignia);

        Image icono = hud.resolverIconoMejora(opcion.claveIcono());
        StackPane casilleroIcono = hud.crearCasilleroIcono(icono, 54, 34);

        Label etiquetaAtributo = new Label(opcion.nombreAtributo().toUpperCase());
        etiquetaAtributo.setWrapText(true);

        Label etiquetaValor = new Label(opcion.valorBonus());

        Label descripcionBreve = new Label(opcion.descripcion());
        descripcionBreve.setWrapText(true);

        VBox contenidoCarta = new VBox(10,
            insigniaRareza,
            casilleroIcono,
            etiquetaAtributo,
            etiquetaValor,
            descripcionBreve
        );
        contenidoCarta.setAlignment(Pos.CENTER);

        Button carta = new Button();
        carta.setGraphic(contenidoCarta);
        carta.setMinSize(ANCHO_CARTA_MEJORA, ALTO_CARTA_MEJORA);
        carta.setPrefSize(ANCHO_CARTA_MEJORA, ALTO_CARTA_MEJORA);
        carta.setMaxSize(ANCHO_CARTA_MEJORA, ALTO_CARTA_MEJORA);
        carta.setOnAction(event -> {
            if (onSeleccionarMejora != null) {
                onSeleccionarMejora.accept(seleccion);
            }
        });
        return carta;
    }

    @Override
    public void cerrarPausa() {
        panelPausa.setVisible(false);
        panelPausa.setManaged(false);
    }

    @Override
    public void mostrarPausa() {
        panelPausa.setManaged(true);
        panelPausa.setVisible(true);
    }

    @Override
    public void cerrarEleccionMejora() {
        panelEleccionMejora.setVisible(false);
        panelEleccionMejora.setManaged(false);
    }

    @Override
    public void actualizarVida(double actual, double maxima) {
        hud.actualizarVida(actual, maxima);
    }

    @Override
    public void actualizarProgreso(int nivel, int experiencia, int experienciaSiguiente) {
        hud.actualizarProgreso(nivel, experiencia, experienciaSiguiente);
    }

    @Override
    public void actualizarTiempo(long segundos) {
        hud.actualizarTiempo(segundos);
    }

    @Override
    public void actualizarEnemigos(int cantidad) {
        hud.actualizarEnemigos(cantidad);
    }

    @Override
    public void actualizarOleada(int numero) {
        hud.actualizarOleada(numero);
    }

    @Override
    public void actualizarDanio(int valor) {
        hud.actualizarDanio(valor);
    }

    @Override
    public void actualizarPuntos(int actual, int siguiente) {
        hud.actualizarPuntos(actual, siguiente);
    }
}
