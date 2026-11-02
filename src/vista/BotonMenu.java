package vista;

import java.util.Objects;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import modelo.OpcionMenu;
import motor.modelo.graficos.GestorSheetEstatica;
import motor.modelo.graficos.Recorte;
import motor.modelo.graficos.Sheet;
import motor.vista.VistaImagen;

/**
 * Componente visual que renderiza una opción de menú con recortes de spritesheet
 * o estilos vectoriales de respaldo, sincronizándose reactivamente con el modelo OpcionMenu.
 */
public class BotonMenu extends Button {

    public static final String RECORTE_NORMAL_DEFAULT = "boton_largo";
    public static final String RECORTE_HOVER_DEFAULT = "boton_largo_hover";
    public static final String RECORTE_PRESIONADO_DEFAULT = "boton_largo_presionado";
    public static final String RECORTE_MARCADO_DEFAULT = "boton_largo_marcado";

    private final OpcionMenu modelo;
    private final GestorSheetEstatica gestorSheet;
    private final VistaImagen vistaSprite;
    private final String recorteNormal;
    private final String recorteHover;
    private final String recortePresionado;
    private final String recorteMarcado;

    private Color colorTextoNormal = Color.web("#fff2d2");
    private Color colorTextoHover = Color.web("#ffe89e");
    private Color colorTextoDeshabilitado = Color.web("#7a7263");
    private DropShadow sombraNormal = new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.85), 5, 0.45, 0, 2);
    private DropShadow sombraHover = new DropShadow(BlurType.GAUSSIAN, Color.rgb(255, 215, 110, 0.55), 10, 0.45, 0, 0);

    public BotonMenu(OpcionMenu modelo, Sheet sheetGui) {
        this(modelo, sheetGui, 0, 0);
    }

    public BotonMenu(OpcionMenu modelo, Sheet sheetGui, double ancho, double alto) {
        this(
            modelo,
            sheetGui,
            RECORTE_NORMAL_DEFAULT,
            RECORTE_HOVER_DEFAULT,
            RECORTE_PRESIONADO_DEFAULT,
            RECORTE_MARCADO_DEFAULT,
            ancho,
            alto
        );
    }

    public BotonMenu(String texto, Runnable accion, Sheet sheetGui) {
        this(new OpcionMenu(texto, accion), sheetGui);
    }

    public BotonMenu(String texto, Runnable accion, Sheet sheetGui, double ancho, double alto) {
        this(new OpcionMenu(texto, accion), sheetGui, ancho, alto);
    }

    public BotonMenu(
        OpcionMenu modelo,
        Sheet sheetGui,
        String recorteNormal,
        String recorteHover,
        String recortePresionado,
        String recorteMarcado,
        double ancho,
        double alto
    ) {
        super(modelo != null ? modelo.getTexto() : "");
        this.modelo = modelo;
        this.recorteNormal = Objects.requireNonNullElse(recorteNormal, RECORTE_NORMAL_DEFAULT);
        this.recorteHover = Objects.requireNonNullElse(recorteHover, RECORTE_HOVER_DEFAULT);
        this.recortePresionado = Objects.requireNonNullElse(recortePresionado, RECORTE_PRESIONADO_DEFAULT);
        this.recorteMarcado = Objects.requireNonNullElse(recorteMarcado, RECORTE_MARCADO_DEFAULT);

        if (this.modelo != null) {
            setDisable(!this.modelo.isHabilitada());
            setOnAction(event -> this.modelo.ejecutar());

            this.modelo.getNotificadorHabilitada().suscribirObservador(habilitada -> {
                Platform.runLater(() -> setDisable(!habilitada));
            });

            this.modelo.getNotificadorTexto().suscribirObservador(nuevoTexto -> {
                Platform.runLater(() -> setText(nuevoTexto));
            });
        }

        setFont(Font.font("Georgia", FontWeight.BOLD, 16));
        setCursor(Cursor.HAND);
        setAlignment(Pos.CENTER);

        Recorte corteBase = sheetGui != null ? sheetGui.getRecorte(this.recorteNormal) : null;
        double anchoFinal = ancho > 0 ? ancho : (corteBase != null ? corteBase.getAncho() : 240);
        double altoFinal = alto > 0 ? alto : (corteBase != null ? corteBase.getAlto() : 50);

        setMinWidth(anchoFinal);
        setPrefWidth(anchoFinal);
        setMaxWidth(anchoFinal);
        setMinHeight(altoFinal);
        setPrefHeight(altoFinal);
        setMaxHeight(altoFinal);

        if (sheetGui != null && sheetGui.getImagen() != null) {
            GestorSheetEstatica gestor = new GestorSheetEstatica(sheetGui);
            boolean recorteCargado = gestor.setAnimacion(this.recorteNormal);

            if (recorteCargado) {
                this.gestorSheet = gestor;
                this.vistaSprite = new VistaImagen(sheetGui.getImagen());
                this.gestorSheet.suscribir(this.vistaSprite.getObservadorRectangulo());

                this.vistaSprite.setFitWidth(anchoFinal);
                this.vistaSprite.setFitHeight(altoFinal);
                this.vistaSprite.setPreserveRatio(false);
                this.vistaSprite.setSmooth(false);

                setGraphic(this.vistaSprite);
                setContentDisplay(ContentDisplay.CENTER);

                setBackground(Background.EMPTY);
                setBorder(Border.EMPTY);
                setPadding(Insets.EMPTY);
                setTextFill(colorTextoNormal);
                setEffect(sombraNormal);

                configurarListenersInteraccion();
                return;
            }
        }

        this.gestorSheet = null;
        this.vistaSprite = null;
        aplicarEstiloFallback();
    }

    private void configurarListenersInteraccion() {
        Runnable actualizarEstado = () -> {
            if (isDisabled()) {
                if (gestorSheet != null) {
                    gestorSheet.setAnimacion(recorteNormal);
                }
                setTextFill(colorTextoDeshabilitado);
                setEffect(null);
                setOpacity(0.55);
                setCursor(Cursor.DEFAULT);
                return;
            }

            setOpacity(1.0);
            setCursor(Cursor.HAND);

            if (isArmed()) {
                if (gestorSheet != null) gestorSheet.setAnimacion(recortePresionado);
            } else if (isHover()) {
                if (gestorSheet != null) gestorSheet.setAnimacion(recorteHover);
            } else if (isFocused()) {
                if (gestorSheet != null) gestorSheet.setAnimacion(recorteMarcado);
            } else {
                if (gestorSheet != null) gestorSheet.setAnimacion(recorteNormal);
            }

            boolean resaltado = isHover() || isFocused();
            setTextFill(resaltado ? colorTextoHover : colorTextoNormal);
            setEffect(resaltado ? sombraHover : sombraNormal);
        };

        disabledProperty().addListener((obs, antes, ahora) -> actualizarEstado.run());
        armedProperty().addListener((obs, antes, ahora) -> actualizarEstado.run());
        hoverProperty().addListener((obs, antes, ahora) -> actualizarEstado.run());
        focusedProperty().addListener((obs, antes, ahora) -> actualizarEstado.run());
        actualizarEstado.run();
    }

    private void aplicarEstiloFallback() {
        Background fondoNormal = new Background(new BackgroundFill(Color.rgb(20, 28, 24, 0.9), CornerRadii.EMPTY, Insets.EMPTY));
        Background fondoHover = new Background(new BackgroundFill(Color.web("#52694d"), CornerRadii.EMPTY, Insets.EMPTY));
        Border bordeNormal = new Border(new BorderStroke(Color.web("#d5bd82"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1)));
        Border bordeHover = new Border(new BorderStroke(Color.web("#f0d98d"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1)));

        setBackground(fondoNormal);
        setBorder(bordeNormal);
        setTextFill(colorTextoNormal);
        setPadding(new Insets(12, 24, 12, 24));

        Runnable actualizarFallback = () -> {
            if (isDisabled()) {
                setBackground(fondoNormal);
                setBorder(bordeNormal);
                setTextFill(colorTextoDeshabilitado);
                setOpacity(0.55);
                setCursor(Cursor.DEFAULT);
                return;
            }
            setOpacity(1.0);
            setCursor(Cursor.HAND);
            boolean resaltado = isHover() || isFocused();
            setBackground(resaltado ? fondoHover : fondoNormal);
            setBorder(resaltado ? bordeHover : bordeNormal);
            setTextFill(resaltado ? colorTextoHover : colorTextoNormal);
        };

        disabledProperty().addListener((obs, antes, ahora) -> actualizarFallback.run());
        hoverProperty().addListener((obs, antes, ahora) -> actualizarFallback.run());
        focusedProperty().addListener((obs, antes, ahora) -> actualizarFallback.run());
        actualizarFallback.run();
    }

    public OpcionMenu getModelo() {
        return modelo;
    }

    public String getIdOpcion() {
        return modelo != null ? modelo.getId() : getText().trim().toLowerCase();
    }

    public GestorSheetEstatica getGestorSheet() {
        return gestorSheet;
    }

    public VistaImagen getVistaSprite() {
        return vistaSprite;
    }

    public void setColoresTexto(Color normal, Color hover) {
        this.colorTextoNormal = Objects.requireNonNull(normal);
        this.colorTextoHover = Objects.requireNonNull(hover);
        if (isDisabled()) {
            setTextFill(colorTextoDeshabilitado);
        } else {
            setTextFill(isHover() || isFocused() ? colorTextoHover : colorTextoNormal);
        }
    }
}
