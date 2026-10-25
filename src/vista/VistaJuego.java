package vista;

import java.util.ArrayList;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import motor.modelo.graficos.Recorte;
import motor.modelo.graficos.Sheet;

/**
 * Vista principal de la pantalla de juego y su interfaz gráfica (HUD integrado, menús de pausa, mejoras y fin de partida).
 * Cumple con MVC puro: solo contiene elementos y lógica de presentación,
 * sin acoplamiento a las clases del modelo de dominio ni rutas físicas de recursos.
 */
public class VistaJuego extends StackPane implements VistaPartida {

    public static final int VIDA_POR_CORAZON_DEFAULT = 10;
    public static final int CANTIDAD_GEMAS_EXP_DEFAULT = 10;

    public static final double ANCHO_PLACA_ESTADO = 264;
    public static final double ALTO_PLACA_ESTADO = 44;
    public static final double ANCHO_BOTON_MENU = 260;
    public static final double ALTO_BOTON_MENU = 48;
    public static final double ANCHO_BOTON_PAUSA = 112;
    public static final double ALTO_BOTON_PAUSA = 44;

    public static final double ANCHO_CARTA_MEJORA = 195;
    public static final double ALTO_CARTA_MEJORA = 235;

    public static final double TAMANO_CORAZON = 13;
    public static final double TAMANO_GEMA_EXP = 11;
    public static final double TAMANO_MARCO_ICONO = 30;

    public static final double ALTO_MEDALLON = 44;
    public static final double ANCHO_MEDALLON_OLEADA = 118;
    public static final double ANCHO_MEDALLON_TIEMPO = 126;
    public static final double ANCHO_MEDALLON_ENEMIGOS = 126;
    public static final double ANCHO_MEDALLON_DANIO = 116;
    public static final double ANCHO_MEDALLON_PUNTOS = 116;

    private final int vidaPorCorazon;
    private final int cantidadGemasExperiencia;

    private final Pane areaJuego;
    private final Label vida;
    private final FlowPane corazones;
    private final List<ImageView> iconosVida;
    private final Image corazonLleno;
    private final Image corazonVacio;
    private final Image iconoExperiencia;
    private final Image gemaExperienciaVacia;
    private final Image gemaExperienciaLlena;
    private final Image hojaGuiRpg;
    private final Map<String, Image> imagenesHud;
    private final Sheet recortesGui;

    private final Label nivelExperiencia;
    private final Label valorExperiencia;
    private final HBox gemasExperiencia;
    private final List<ImageView> iconosExperiencia;

    private final Label tiempo;
    private final Label oleada;
    private final Label enemigos;
    private final Label danio;
    private final Label puntos;
    private final StackPane indicadorPuntos;

    private final Button botonPausar;
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

    public VistaJuego(Map<String, Image> imagenesHud, String hojaEstilosUrl) {
        this(imagenesHud, null, hojaEstilosUrl);
    }

    public VistaJuego(
        Map<String, Image> imagenesHud,
        Sheet recortesGui,
        String hojaEstilosUrl
    ) {
        this(imagenesHud, recortesGui, hojaEstilosUrl, VIDA_POR_CORAZON_DEFAULT, CANTIDAD_GEMAS_EXP_DEFAULT);
    }

    public VistaJuego(
        Map<String, Image> imagenesHud,
        Sheet recortesGui,
        String hojaEstilosUrl,
        int vidaPorCorazon,
        int cantidadGemasExperiencia
    ) {
        Objects.requireNonNull(imagenesHud, "El mapa de imágenes de HUD no puede ser nulo");
        Objects.requireNonNull(hojaEstilosUrl, "La URL de la hoja de estilos no puede ser nula");
        if (vidaPorCorazon <= 0 || cantidadGemasExperiencia <= 0) {
            throw new IllegalArgumentException("La vida por corazón y la cantidad de gemas deben ser positivas.");
        }
        this.vidaPorCorazon = vidaPorCorazon;
        this.cantidadGemasExperiencia = cantidadGemasExperiencia;
        this.areaJuego = new Pane();
        this.vida = crearValor("100/100");
        this.corazones = new FlowPane(3, 2);
        this.iconosVida = new ArrayList<>();
        this.imagenesHud = Map.copyOf(imagenesHud);
        this.corazonLleno = imagenesHud.get("corazonLleno");
        this.corazonVacio = imagenesHud.get("corazonVacio");
        this.iconoExperiencia = imagenesHud.get("iconoExperiencia");
        this.gemaExperienciaVacia = imagenesHud.get("gemaExperienciaVacia");
        this.gemaExperienciaLlena = imagenesHud.get("gemaExperienciaLlena");
        this.hojaGuiRpg = imagenesHud.get("hojaGuiRpg");
        this.recortesGui = recortesGui;

        this.nivelExperiencia = new Label("NIVEL 1");
        this.valorExperiencia = crearValor("0 / 100 XP");
        this.gemasExperiencia = new HBox(2);
        this.iconosExperiencia = new ArrayList<>();
        this.tiempo = crearValor("--:--");
        this.oleada = crearValor("--");
        this.enemigos = crearValor("--");
        this.danio = crearValor("--");
        this.puntos = crearValor("--/--");
        this.indicadorPuntos = crearMedallonAtributo(
            "PUNTOS",
            this.puntos,
            imagenesHud.get("iconoPuntos"),
            ANCHO_MEDALLON_PUNTOS,
            ALTO_MEDALLON
        );
        this.botonPausar = crearBotonPlacaRpg("II  PAUSA", "placa_brillante", ANCHO_BOTON_PAUSA, ALTO_BOTON_PAUSA, "pause-button");
        this.botonPausarMejora = crearBotonPlacaRpg("II  PAUSA", "placa_brillante", ANCHO_BOTON_PAUSA, ALTO_BOTON_PAUSA, "pause-button");
        this.textoPausa = new Label("PARTIDA EN PAUSA");
        this.botonReanudar = crearBotonPlacaRpg("CONTINUAR", "placa_ancha", ANCHO_BOTON_MENU, ALTO_BOTON_MENU, "game-over-button");
        this.botonSalirPausa = crearBotonPlacaRpg("VOLVER AL MENÚ", "placa_ancha", ANCHO_BOTON_MENU, ALTO_BOTON_MENU, "game-over-button");
        this.panelPausa = new StackPane();
        this.textoFinPartida = new Label("FIN DE LA PARTIDA");
        this.botonReintentar = crearBotonPlacaRpg("REINTENTAR", "placa_ancha", ANCHO_BOTON_MENU, ALTO_BOTON_MENU, "game-over-button");
        this.botonVolverMenu = crearBotonPlacaRpg("VOLVER AL MENÚ", "placa_ancha", ANCHO_BOTON_MENU, ALTO_BOTON_MENU, "game-over-button");
        this.panelFinPartida = new StackPane();
        this.panelEleccionMejora = new StackPane();

        // 1. Panel izquierdo: Estado del jugador (Salud + Nivel/XP sobre placas RPG)
        StackPane placaVida = crearPlacaVitalidad();
        StackPane placaExperiencia = crearPlacaExperiencia();
        VBox grupoJugador = new VBox(3, placaVida, placaExperiencia);
        grupoJugador.setAlignment(Pos.TOP_LEFT);
        grupoJugador.setPickOnBounds(false);

        // 2. Panel central: Crónica de la mazmorra (Oleada, Tiempo, Enemigos)
        StackPane medallonOleada = crearMedallonAtributo(
            "OLEADA",
            oleada,
            imagenesHud.get("iconoOleada"),
            ANCHO_MEDALLON_OLEADA,
            ALTO_MEDALLON
        );
        StackPane medallonTiempo = crearMedallonAtributo(
            "TIEMPO",
            tiempo,
            imagenesHud.get("iconoTiempo"),
            ANCHO_MEDALLON_TIEMPO,
            ALTO_MEDALLON
        );
        StackPane medallonEnemigos = crearMedallonAtributo(
            "ENEMIGOS",
            enemigos,
            imagenesHud.get("iconoEnemigos"),
            ANCHO_MEDALLON_ENEMIGOS,
            ALTO_MEDALLON
        );
        HBox grupoPartida = new HBox(8, medallonOleada, medallonTiempo, medallonEnemigos);
        grupoPartida.setAlignment(Pos.TOP_CENTER);
        grupoPartida.setPickOnBounds(false);

        // 3. Panel derecho: Atributos de combate y botón de pausa integrado
        StackPane medallonDanio = crearMedallonAtributo(
            "PODER",
            danio,
            imagenesHud.get("iconoDanio"),
            ANCHO_MEDALLON_DANIO,
            ALTO_MEDALLON
        );
        indicadorPuntos.setVisible(false);
        indicadorPuntos.setManaged(false);

        botonPausar.setFocusTraversable(false);
        botonPausarMejora.setFocusTraversable(false);

        HBox grupoDerecho = new HBox(8, medallonDanio, indicadorPuntos, botonPausar);
        grupoDerecho.setAlignment(Pos.TOP_RIGHT);
        grupoDerecho.setPickOnBounds(false);

        Region espaciadorIzquierdo = new Region();
        Region espaciadorDerecho = new Region();
        espaciadorIzquierdo.setMouseTransparent(true);
        espaciadorDerecho.setMouseTransparent(true);
        HBox.setHgrow(espaciadorIzquierdo, Priority.ALWAYS);
        HBox.setHgrow(espaciadorDerecho, Priority.ALWAYS);

        HBox barraHud = new HBox(8,
            grupoJugador,
            espaciadorIzquierdo,
            grupoPartida,
            espaciadorDerecho,
            grupoDerecho
        );
        barraHud.getStyleClass().add("game-hud-overlay");
        barraHud.setAlignment(Pos.TOP_CENTER);
        barraHud.setPadding(new Insets(6, 12, 0, 12));
        barraHud.setPickOnBounds(false);
        barraHud.setMaxHeight(Region.USE_PREF_SIZE);
        StackPane.setAlignment(barraHud, Pos.TOP_CENTER);

        // 4. Paneles modales (Pausa, Fin de partida, Subida de nivel) con estética RPG
        panelPausa.getStyleClass().add("pause-overlay");
        panelPausa.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        textoPausa.getStyleClass().add("game-over-title");
        VBox opcionesPausa = new VBox(14, textoPausa, botonReanudar, botonSalirPausa);
        opcionesPausa.getStyleClass().add("rpg-modal-card");
        opcionesPausa.setAlignment(Pos.CENTER);
        opcionesPausa.setMaxSize(380, Region.USE_PREF_SIZE);
        panelPausa.getChildren().add(opcionesPausa);
        panelPausa.setVisible(false);
        panelPausa.setManaged(false);

        areaJuego.getStyleClass().add("game-area");
        areaJuego.setMinSize(0, 0);
        areaJuego.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        areaJuego.setFocusTraversable(true);

        panelFinPartida.getStyleClass().add("game-over-overlay");
        panelFinPartida.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        textoFinPartida.getStyleClass().add("game-over-title");
        VBox opcionesFinPartida = new VBox(14,
            textoFinPartida,
            botonReintentar,
            botonVolverMenu);
        opcionesFinPartida.getStyleClass().add("rpg-modal-card");
        opcionesFinPartida.setAlignment(Pos.CENTER);
        opcionesFinPartida.setMaxSize(420, Region.USE_PREF_SIZE);
        panelFinPartida.getChildren().add(opcionesFinPartida);
        panelFinPartida.setVisible(false);
        panelFinPartida.setManaged(false);

        panelEleccionMejora.getStyleClass().add("level-up-overlay");
        panelEleccionMejora.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        panelEleccionMejora.setVisible(false);
        panelEleccionMejora.setManaged(false);

        // El área de juego ocupa toda la ventana y el HUD se integra flotando encima
        getChildren().addAll(areaJuego, barraHud, panelEleccionMejora, panelPausa, panelFinPartida);
        getStyleClass().add("game-root");

        getStylesheets().add(hojaEstilosUrl);
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
        botonPausar.setOnAction(event -> accion.run());
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
        botonPausar.setDisable(!habilitada);
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
        subtitulo.getStyleClass().add("hud-label");
        Label titulo = new Label("¡NIVEL " + nivel + " ALCANZADO!");
        titulo.getStyleClass().add("game-over-title");

        HBox filaCartas = new HBox(18);
        filaCartas.setAlignment(Pos.CENTER);
        for (int indice = 0; indice < descripcionesMejoras.size(); indice++) {
            filaCartas.getChildren().add(
                crearCartaMejora(descripcionesMejoras.get(indice), indice)
            );
        }

        VBox contenedor = new VBox(16, subtitulo, titulo, filaCartas);
        contenedor.getStyleClass().add("rpg-modal-card");
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
        String claseRareza = switch (rarezaTexto) {
            case "RARA" -> "upgrade-card-rare";
            case "ÉPICA" -> "upgrade-card-epic";
            case "LEGENDARIA" -> "upgrade-card-legendary";
            default -> "upgrade-card-common";
        };
        String textoInsignia = rarezaTexto.equals("LEGENDARIA")
            ? "★ LEGENDARIA ★"
            : rarezaTexto;

        Label insigniaRareza = new Label(textoInsignia);
        insigniaRareza.getStyleClass().addAll("upgrade-rarity-badge", claseRareza + "-badge");

        Image icono = resolverIconoMejora(opcion.claveIcono());
        StackPane casilleroIcono = crearCasilleroIcono(icono, 54, 34);

        Label etiquetaAtributo = new Label(opcion.nombreAtributo().toUpperCase());
        etiquetaAtributo.getStyleClass().add("upgrade-attr-title");
        etiquetaAtributo.setWrapText(true);

        Label etiquetaValor = new Label(opcion.valorBonus());
        etiquetaValor.getStyleClass().addAll("upgrade-bonus-value", claseRareza + "-value");

        Label descripcionBreve = new Label(opcion.descripcion());
        descripcionBreve.getStyleClass().add("upgrade-flavor-text");
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
        carta.getStyleClass().addAll("upgrade-card", claseRareza);
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

    private Image resolverIconoMejora(String claveIcono) {
        if (claveIcono == null) {
            return iconoExperiencia;
        }
        return switch (claveIcono) {
            case "corazon" -> corazonLleno;
            case "danio" -> imagenesHud.get("iconoDanio");
            case "tiempo" -> imagenesHud.get("iconoTiempo");
            case "defensa" -> imagenesHud.get("iconoOleada");
            default -> iconoExperiencia;
        };
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
            corazon.setFitWidth(TAMANO_CORAZON);
            corazon.setFitHeight(TAMANO_CORAZON);
            corazon.setPreserveRatio(true);
            corazon.setSmooth(false);
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

    @Override
    public void actualizarProgreso(int nivel, int experiencia, int experienciaSiguiente) {
        int objetivo = Math.max(0, experienciaSiguiente);
        int actual = Math.max(0, Math.min(experiencia, objetivo));
        nivelExperiencia.setText("NIVEL " + nivel);
        valorExperiencia.setText(actual + "/" + objetivo + " XP");
        double progreso = objetivo == 0 ? 0 : (double) actual / objetivo;
        int gemasLlenas = (int) Math.round(progreso * cantidadGemasExperiencia);
        for (int indice = 0; indice < iconosExperiencia.size(); indice++) {
            iconosExperiencia.get(indice).setImage(
                indice < gemasLlenas ? gemaExperienciaLlena : gemaExperienciaVacia);
        }
    }

    @Override
    public void actualizarTiempo(long segundos) {
        tiempo.setText(String.format("%02d:%02d", segundos / 60, segundos % 60));
    }

    @Override
    public void actualizarEnemigos(int cantidad) {
        enemigos.setText(Integer.toString(cantidad));
    }

    @Override
    public void actualizarOleada(int numero) {
        oleada.setText(Integer.toString(numero));
    }

    @Override
    public void actualizarDanio(int valor) {
        danio.setText(Integer.toString(valor));
    }

    @Override
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

    private Button crearBotonPlacaRpg(
        String texto,
        String nombreRecorte,
        double ancho,
        double alto,
        String claseEstilo
    ) {
        Button boton = new Button(texto);
        boton.getStyleClass().add(claseEstilo);
        boton.setMinSize(ancho, alto);
        boton.setPrefSize(ancho, alto);
        boton.setMaxSize(ancho, alto);

        ImageView fondoPlaca = crearRecorteRpg(nombreRecorte, ancho, alto);
        if (fondoPlaca != null) {
            boton.setGraphic(fondoPlaca);
            boton.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
            boton.getStyleClass().add("rpg-plaque-button");
        }
        return boton;
    }

    private StackPane crearPlacaVitalidad() {
        double anchoPlaca = ANCHO_PLACA_ESTADO;
        double altoPlaca = ALTO_PLACA_ESTADO;

        corazones.setPrefWrapLength(135);
        corazones.setAlignment(Pos.CENTER_LEFT);
        vida.getStyleClass().add("hp-value");

        Label tituloVida = new Label("VITALIDAD");
        tituloVida.getStyleClass().add("hud-label");

        HBox cabeceraVida = new HBox(8, tituloVida, vida);
        cabeceraVida.setAlignment(Pos.CENTER_LEFT);

        VBox detalleVida = new VBox(2, cabeceraVida, corazones);
        detalleVida.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(detalleVida, Priority.ALWAYS);

        StackPane casilleroCorazon = crearCasilleroIcono(corazonLleno, TAMANO_MARCO_ICONO, 18);
        HBox filaContenido = new HBox(8, casilleroCorazon, detalleVida);
        filaContenido.setAlignment(Pos.CENTER_LEFT);
        filaContenido.setPadding(new Insets(4, 22, 4, 20));

        return envolverEnPlacaRpg("placa_ancha", filaContenido, anchoPlaca, altoPlaca);
    }

    private StackPane crearPlacaExperiencia() {
        double anchoPlaca = ANCHO_PLACA_ESTADO;
        double altoPlaca = ALTO_PLACA_ESTADO;

        for (int indice = 0; indice < cantidadGemasExperiencia; indice++) {
            ImageView gema = new ImageView(gemaExperienciaVacia);
            gema.setFitWidth(TAMANO_GEMA_EXP);
            gema.setFitHeight(TAMANO_GEMA_EXP);
            gema.setPreserveRatio(true);
            gema.setSmooth(false);
            iconosExperiencia.add(gema);
            gemasExperiencia.getChildren().add(gema);
        }
        gemasExperiencia.setAlignment(Pos.CENTER_LEFT);

        nivelExperiencia.getStyleClass().add("xp-level");
        valorExperiencia.getStyleClass().add("xp-value");

        HBox etiquetas = new HBox(8, nivelExperiencia, valorExperiencia);
        etiquetas.setAlignment(Pos.CENTER_LEFT);

        VBox informacion = new VBox(2, etiquetas, gemasExperiencia);
        informacion.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(informacion, Priority.ALWAYS);

        StackPane casilleroMagia = crearCasilleroIcono(iconoExperiencia, TAMANO_MARCO_ICONO, 20);
        HBox contenido = new HBox(8, casilleroMagia, informacion);
        contenido.setAlignment(Pos.CENTER_LEFT);
        contenido.setPadding(new Insets(4, 22, 4, 20));

        return envolverEnPlacaRpg("placa_ancha", contenido, anchoPlaca, altoPlaca);
    }

    private StackPane crearMedallonAtributo(
        String titulo,
        Node nodoValor,
        Image iconoAtributo,
        double ancho,
        double alto
    ) {
        Label etiqueta = new Label(titulo);
        etiqueta.getStyleClass().add("hud-label");

        VBox textos = new VBox(0, etiqueta, nodoValor);
        textos.setAlignment(Pos.CENTER_LEFT);

        StackPane casilleroIcono = crearCasilleroIcono(iconoAtributo, TAMANO_MARCO_ICONO, 19);
        HBox contenido = new HBox(7, casilleroIcono, textos);
        contenido.setAlignment(Pos.CENTER_LEFT);
        contenido.setPadding(new Insets(4, 12, 4, 10));

        return envolverEnPlacaRpg("placa_recta", contenido, ancho, alto);
    }

    private StackPane crearCasilleroIcono(Image imagenIcono, double tamanoMarco, double tamanoIcono) {
        StackPane casillero = new StackPane();
        casillero.getStyleClass().add("hud-icon-slot");
        casillero.setMinSize(tamanoMarco, tamanoMarco);
        casillero.setPrefSize(tamanoMarco, tamanoMarco);
        casillero.setMaxSize(tamanoMarco, tamanoMarco);

        ImageView marco = crearRecorteRpg("marco_icono", tamanoMarco, tamanoMarco);
        if (marco != null) {
            casillero.getChildren().add(marco);
        }

        if (imagenIcono != null) {
            ImageView vistaIcono = new ImageView(imagenIcono);
            vistaIcono.setFitWidth(tamanoIcono);
            vistaIcono.setFitHeight(tamanoIcono);
            vistaIcono.setPreserveRatio(true);
            vistaIcono.setSmooth(false);
            casillero.getChildren().add(vistaIcono);
        }
        return casillero;
    }

    private StackPane envolverEnPlacaRpg(
        String nombreRecorte,
        Node contenido,
        double ancho,
        double alto
    ) {
        StackPane tarjeta = new StackPane();
        tarjeta.getStyleClass().add("hud-plaque");
        tarjeta.setMinSize(ancho, alto);
        tarjeta.setPrefSize(ancho, alto);
        tarjeta.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        tarjeta.setMouseTransparent(true);

        ImageView fondoPlaca = crearRecorteRpg(nombreRecorte, ancho, alto);
        if (fondoPlaca != null) {
            tarjeta.getChildren().add(fondoPlaca);
        } else {
            tarjeta.getStyleClass().add("hud-plaque-fallback");
        }
        tarjeta.getChildren().add(contenido);
        return tarjeta;
    }

    private ImageView crearRecorteRpg(String nombreRecorte, double ancho, double alto) {
        if (recortesGui == null) {
            return null;
        }
        Recorte recorte = recortesGui.getRecorte(nombreRecorte);
        if (recorte == null) {
            return null;
        }
        Image imagenSheet = recortesGui.getImagen() != null ? recortesGui.getImagen() : hojaGuiRpg;
        if (imagenSheet == null) {
            return null;
        }
        ImageView vistaRecorte = new ImageView(imagenSheet);
        vistaRecorte.setViewport(recorte.getRectangulo());
        vistaRecorte.setFitWidth(ancho);
        vistaRecorte.setFitHeight(alto);
        vistaRecorte.setPreserveRatio(false);
        vistaRecorte.setSmooth(true);
        return vistaRecorte;
    }
}
