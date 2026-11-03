package modelo;

import java.util.Objects;

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
import motor.modelo.graficos.GestorSheetEstatica;
import motor.modelo.graficos.Sheet;
import motor.vista.VistaImagen;

public class BotonMenu extends Button {

    public static final double ANCHO_DEFAULT = 240;
    public static final double ALTO_DEFAULT = 50;

    public static final String RECORTE_NORMAL_DEFAULT = "boton_largo";
    public static final String RECORTE_HOVER_DEFAULT = "boton_largo_hover";
    public static final String RECORTE_PRESIONADO_DEFAULT = "boton_largo_presionado";
    public static final String RECORTE_MARCADO_DEFAULT = "boton_largo_marcado";

    private static final Color COLOR_TEXTO_NORMAL = Color.web("#fff2d2");
    private static final Color COLOR_TEXTO_HOVER = Color.web("#ffe89e");
    private static final Font FUENTE_BOTON = Font.font("Georgia", FontWeight.BOLD, 16);

    private static final DropShadow SOMBRA_BOTON_NORMAL = new DropShadow(
        BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.85), 5, 0.45, 0, 2
    );
    private static final DropShadow SOMBRA_BOTON_HOVER = new DropShadow(
        BlurType.GAUSSIAN, Color.rgb(255, 215, 110, 0.55), 10, 0.45, 0, 0
    );

    private final GestorSheetEstatica gestorSheet;
    private final VistaImagen vistaSprite;
    private final String recorteNormal;
    private final String recorteHover;
    private final String recortePresionado;
    private final String recorteMarcado;

    public BotonMenu(String texto) {
        this(texto, null, ANCHO_DEFAULT, ALTO_DEFAULT);
    }

    public BotonMenu(String texto, Sheet sheetGui) {
        this(texto, sheetGui, ANCHO_DEFAULT, ALTO_DEFAULT);
    }

    public BotonMenu(String texto, Sheet sheetGui, double ancho, double alto) {
        this(
            texto,
            sheetGui,
            RECORTE_NORMAL_DEFAULT,
            RECORTE_HOVER_DEFAULT,
            RECORTE_PRESIONADO_DEFAULT,
            RECORTE_MARCADO_DEFAULT,
            ancho,
            alto
        );
    }

    public BotonMenu(
        String texto,
        Sheet sheetGui,
        String recorteNormal,
        String recorteHover,
        String recortePresionado,
        String recorteMarcado,
        double ancho,
        double alto
    ) {
        super(texto);
        this.recorteNormal = Objects.requireNonNullElse(recorteNormal, RECORTE_NORMAL_DEFAULT);
        this.recorteHover = Objects.requireNonNullElse(recorteHover, RECORTE_HOVER_DEFAULT);
        this.recortePresionado = Objects.requireNonNullElse(recortePresionado, RECORTE_PRESIONADO_DEFAULT);
        this.recorteMarcado = Objects.requireNonNullElse(recorteMarcado, RECORTE_MARCADO_DEFAULT);

        setMaxWidth(Double.MAX_VALUE);
        setMinHeight(alto);
        setPrefHeight(alto);
        setFont(FUENTE_BOTON);
        setCursor(Cursor.HAND);
        setAlignment(Pos.CENTER);

        if (sheetGui != null && sheetGui.getImagen() != null) {
            GestorSheetEstatica gestor = new GestorSheetEstatica(sheetGui);
            boolean recorteCargado = gestor.setRecorte(this.recorteNormal);

            if (recorteCargado) {
                this.gestorSheet = gestor;
                this.vistaSprite = new VistaImagen(sheetGui.getImagen());
                this.gestorSheet.suscribir(this.vistaSprite.getObservadorRectangulo());

                this.vistaSprite.setFitWidth(ancho);
                this.vistaSprite.setFitHeight(alto);
                this.vistaSprite.setPreserveRatio(false);
                this.vistaSprite.setSmooth(false);

                setGraphic(this.vistaSprite);
                setContentDisplay(ContentDisplay.CENTER);

                setBackground(Background.EMPTY);
                setBorder(Border.EMPTY);
                setPadding(Insets.EMPTY);
                setTextFill(COLOR_TEXTO_NORMAL);
                setEffect(SOMBRA_BOTON_NORMAL);

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
            if (isArmed()) {
                gestorSheet.setRecorte(recortePresionado);
            } else if (isHover()) {
                gestorSheet.setRecorte(recorteHover);
            } else if (isFocused()) {
                gestorSheet.setRecorte(recorteMarcado);
            } else {
                gestorSheet.setRecorte(recorteNormal);
            }

            boolean resaltado = isHover() || isFocused();
            setTextFill(resaltado ? COLOR_TEXTO_HOVER : COLOR_TEXTO_NORMAL);
            setEffect(resaltado ? SOMBRA_BOTON_HOVER : SOMBRA_BOTON_NORMAL);
        };

        armedProperty().addListener((obs, antes, ahora) -> actualizarEstado.run());
        hoverProperty().addListener((obs, antes, ahora) -> actualizarEstado.run());
        focusedProperty().addListener((obs, antes, ahora) -> actualizarEstado.run());
    }

    private void aplicarEstiloFallback() {
        Background fondoNormal = new Background(new BackgroundFill(Color.rgb(20, 28, 24, 0.9), CornerRadii.EMPTY, Insets.EMPTY));
        Background fondoHover = new Background(new BackgroundFill(Color.web("#52694d"), CornerRadii.EMPTY, Insets.EMPTY));
        Border bordeNormal = new Border(new BorderStroke(Color.web("#d5bd82"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1)));
        Border bordeHover = new Border(new BorderStroke(Color.web("#f0d98d"), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(1)));

        setBackground(fondoNormal);
        setBorder(bordeNormal);
        setTextFill(COLOR_TEXTO_NORMAL);
        setPadding(new Insets(12, 24, 12, 24));

        Runnable actualizarFallback = () -> {
            boolean resaltado = isHover() || isFocused();
            setBackground(resaltado ? fondoHover : fondoNormal);
            setBorder(resaltado ? bordeHover : bordeNormal);
        };
        hoverProperty().addListener((obs, antes, ahora) -> actualizarFallback.run());
        focusedProperty().addListener((obs, antes, ahora) -> actualizarFallback.run());
    }

    public GestorSheetEstatica getGestorSheet() {
        return gestorSheet;
    }

    public VistaImagen getVistaSprite() {
        return vistaSprite;
    }
}
