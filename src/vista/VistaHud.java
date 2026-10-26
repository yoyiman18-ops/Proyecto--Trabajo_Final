package vista;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import motor.modelo.graficos.Recorte;
import motor.modelo.graficos.Sheet;

/**
 * Vista dedicada exclusivamente al HUD (Heads-Up Display) de la partida.
 * Muestra vitalidad, experiencia/nivel, oleada, tiempo, enemigos restantes,
 * poder de ataque, puntos y el botón de pausa.
 */
public final class VistaHud extends HBox {

    public static final int VIDA_POR_CORAZON_DEFAULT = 10;
    public static final int CANTIDAD_GEMAS_EXP_DEFAULT = 10;

    public static final double ANCHO_PLACA_ESTADO = 264;
    public static final double ALTO_PLACA_ESTADO = 44;
    public static final double ANCHO_BOTON_PAUSA = 112;
    public static final double ALTO_BOTON_PAUSA = 44;

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

    private final Map<String, Image> imagenesHud;
    private final Sheet recortesGui;
    private final Image corazonLleno;
    private final Image corazonVacio;
    private final Image iconoExperiencia;
    private final Image gemaExperienciaVacia;
    private final Image gemaExperienciaLlena;
    private final Image hojaGuiRpg;

    private final Label vida;
    private final FlowPane corazones;
    private final List<ImageView> iconosVida;

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

    public VistaHud(Map<String, Image> imagenesHud, Sheet recortesGui) {
        this(imagenesHud, recortesGui, VIDA_POR_CORAZON_DEFAULT, CANTIDAD_GEMAS_EXP_DEFAULT);
    }

    public VistaHud(
        Map<String, Image> imagenesHud,
        Sheet recortesGui,
        int vidaPorCorazon,
        int cantidadGemasExperiencia
    ) {
        super(8);
        Objects.requireNonNull(imagenesHud, "El mapa de imágenes de HUD no puede ser nulo");
        if (vidaPorCorazon <= 0 || cantidadGemasExperiencia <= 0) {
            throw new IllegalArgumentException("La vida por corazón y la cantidad de gemas deben ser positivas.");
        }
        this.vidaPorCorazon = vidaPorCorazon;
        this.cantidadGemasExperiencia = cantidadGemasExperiencia;
        this.imagenesHud = Map.copyOf(imagenesHud);
        this.recortesGui = recortesGui;

        this.corazonLleno = this.imagenesHud.get("corazonLleno");
        this.corazonVacio = this.imagenesHud.get("corazonVacio");
        this.iconoExperiencia = this.imagenesHud.get("iconoExperiencia");
        this.gemaExperienciaVacia = this.imagenesHud.get("gemaExperienciaVacia");
        this.gemaExperienciaLlena = this.imagenesHud.get("gemaExperienciaLlena");
        this.hojaGuiRpg = this.imagenesHud.get("hojaGuiRpg");

        this.vida = crearValor("100/100");
        this.corazones = new FlowPane(3, 2);
        this.iconosVida = new ArrayList<>();

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
            this.imagenesHud.get("iconoPuntos"),
            ANCHO_MEDALLON_PUNTOS,
            ALTO_MEDALLON
        );
        this.botonPausar = crearBotonPlacaRpg(
            "II  PAUSA",
            "placa_brillante",
            ANCHO_BOTON_PAUSA,
            ALTO_BOTON_PAUSA,
            "pause-button"
        );
        this.botonPausar.setFocusTraversable(false);

        // 1. Panel izquierdo: Vitalidad + Nivel/XP
        StackPane placaVida = crearPlacaVitalidad();
        StackPane placaExperiencia = crearPlacaExperiencia();
        VBox grupoJugador = new VBox(3, placaVida, placaExperiencia);
        grupoJugador.setAlignment(Pos.TOP_LEFT);
        grupoJugador.setPickOnBounds(false);

        // 2. Panel central: Oleada, Tiempo y Enemigos
        StackPane medallonOleada = crearMedallonAtributo(
            "OLEADA",
            oleada,
            this.imagenesHud.get("iconoOleada"),
            ANCHO_MEDALLON_OLEADA,
            ALTO_MEDALLON
        );
        StackPane medallonTiempo = crearMedallonAtributo(
            "TIEMPO",
            tiempo,
            this.imagenesHud.get("iconoTiempo"),
            ANCHO_MEDALLON_TIEMPO,
            ALTO_MEDALLON
        );
        StackPane medallonEnemigos = crearMedallonAtributo(
            "ENEMIGOS",
            enemigos,
            this.imagenesHud.get("iconoEnemigos"),
            ANCHO_MEDALLON_ENEMIGOS,
            ALTO_MEDALLON
        );
        HBox grupoPartida = new HBox(8, medallonOleada, medallonTiempo, medallonEnemigos);
        grupoPartida.setAlignment(Pos.TOP_CENTER);
        grupoPartida.setPickOnBounds(false);

        // 3. Panel derecho: Poder, Puntos y Pausa
        StackPane medallonDanio = crearMedallonAtributo(
            "PODER",
            danio,
            this.imagenesHud.get("iconoDanio"),
            ANCHO_MEDALLON_DANIO,
            ALTO_MEDALLON
        );
        indicadorPuntos.setVisible(false);
        indicadorPuntos.setManaged(false);

        HBox grupoDerecho = new HBox(8, medallonDanio, indicadorPuntos, botonPausar);
        grupoDerecho.setAlignment(Pos.TOP_RIGHT);
        grupoDerecho.setPickOnBounds(false);

        Region espaciadorIzquierdo = new Region();
        Region espaciadorDerecho = new Region();
        espaciadorIzquierdo.setMouseTransparent(true);
        espaciadorDerecho.setMouseTransparent(true);
        HBox.setHgrow(espaciadorIzquierdo, Priority.ALWAYS);
        HBox.setHgrow(espaciadorDerecho, Priority.ALWAYS);

        getChildren().addAll(
            grupoJugador,
            espaciadorIzquierdo,
            grupoPartida,
            espaciadorDerecho,
            grupoDerecho
        );
        getStyleClass().add("game-hud-overlay");
        setAlignment(Pos.TOP_CENTER);
        setPadding(new Insets(6, 12, 0, 12));
        setPickOnBounds(false);
        setMaxHeight(Region.USE_PREF_SIZE);
    }

    public void setOnPausa(Runnable accion) {
        Objects.requireNonNull(accion, "La acción de pausa no puede ser nula");
        botonPausar.setOnAction(event -> accion.run());
    }

    public void setPausaHabilitada(boolean habilitada) {
        botonPausar.setDisable(!habilitada);
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

    public void actualizarProgreso(int nivel, int experiencia, int experienciaSiguiente) {
        int objetivo = Math.max(0, experienciaSiguiente);
        int actual = Math.max(0, Math.min(experiencia, objetivo));
        nivelExperiencia.setText("NIVEL " + nivel);
        valorExperiencia.setText(actual + "/" + objetivo + " XP");
        double progreso = objetivo == 0 ? 0 : (double) actual / objetivo;
        int gemasLlenas = (int) Math.round(progreso * cantidadGemasExperiencia);
        for (int indice = 0; indice < iconosExperiencia.size(); indice++) {
            iconosExperiencia.get(indice).setImage(
                indice < gemasLlenas ? gemaExperienciaLlena : gemaExperienciaVacia
            );
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

    public Button crearBotonPlacaRpg(
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
            boton.setContentDisplay(ContentDisplay.CENTER);
            boton.getStyleClass().add("rpg-plaque-button");
        }
        return boton;
    }

    public StackPane crearCasilleroIcono(Image imagenIcono, double tamanoMarco, double tamanoIcono) {
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

    public Image resolverIconoMejora(String claveIcono) {
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

    private Label crearValor(String valorInicial) {
        Label valor = new Label(valorInicial);
        valor.getStyleClass().add("hud-value");
        return valor;
    }

    private StackPane crearPlacaVitalidad() {
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

        return envolverEnPlacaRpg("placa_ancha", filaContenido, ANCHO_PLACA_ESTADO, ALTO_PLACA_ESTADO);
    }

    private StackPane crearPlacaExperiencia() {
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

        return envolverEnPlacaRpg("placa_ancha", contenido, ANCHO_PLACA_ESTADO, ALTO_PLACA_ESTADO);
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
