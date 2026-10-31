package vista;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import motor.modelo.graficos.GestorSheetEstatica;
import modelo.BotonMenu;
import motor.modelo.graficos.Sheet;

public final class VistaMenu extends StackPane {

    public static final double ANCHO_MAX_BOTONES = 240;
    public static final double ALTO_BOTON = 50;
    public static final double ESPACIADO_BOTONES = 12;
    public static final double MARGEN_INFERIOR_BOTONES = 48;

    public static final double TAMANO_ESQUINA_MARCO = 22;
    public static final double GROSOR_BORDE_HORIZONTAL_MARCO = 13;
    public static final double GROSOR_BORDE_VERTICAL_MARCO = 13;
    public static final double PADDING_HORIZONTAL_MARCO = 28;
    public static final double PADDING_VERTICAL_MARCO = 22;

    private static final DropShadow SOMBRA_MARCO = new DropShadow(
        BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.92), 16, 0.5, 0, 4
    );

    private static final LinearGradient DEGRADADO_FONDO_MARCO = new LinearGradient(
        0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
        new Stop(0.0, Color.rgb(28, 22, 18, 0.94)),
        new Stop(1.0, Color.rgb(16, 12, 10, 0.96))
    );

    private final Sheet sheetGui;
    private final List<GestorSheetEstatica> gestoresSheet;
    private final BotonMenu botonJugar;
    private final BotonMenu botonSalir;
    private final StackPane marcoMenu;

    public VistaMenu(Image imagenFondo) {
        this(imagenFondo, null, null);
    }

    public VistaMenu(Image imagenFondo, Sheet sheetGui) {
        this(imagenFondo, sheetGui, null);
    }

    public VistaMenu(Image imagenFondo, String hojaEstilosUrl) {
        this(imagenFondo, null, hojaEstilosUrl);
    }

    public VistaMenu(Image imagenFondo, Sheet sheetGui, String hojaEstilosUrl) {
        this(
            imagenFondo,
            new BotonMenu("JUGAR", sheetGui, ANCHO_MAX_BOTONES, ALTO_BOTON),
            new BotonMenu("SALIR", sheetGui, ANCHO_MAX_BOTONES, ALTO_BOTON),
            sheetGui,
            hojaEstilosUrl
        );
    }

    public VistaMenu(
        Image imagenFondo,
        BotonMenu botonJugar,
        BotonMenu botonSalir,
        Sheet sheetGui,
        String hojaEstilosUrl
    ) {
        Objects.requireNonNull(imagenFondo, "La imagen de fondo del menú no puede ser nula");
        this.botonJugar = Objects.requireNonNull(botonJugar, "El botón de jugar no puede ser nulo");
        this.botonSalir = Objects.requireNonNull(botonSalir, "El botón de salir no puede ser nulo");
        this.sheetGui = sheetGui;
        this.gestoresSheet = new ArrayList<>();

        if (this.botonJugar.getGestorSheet() != null) {
            this.gestoresSheet.add(this.botonJugar.getGestorSheet());
        }
        if (this.botonSalir.getGestorSheet() != null) {
            this.gestoresSheet.add(this.botonSalir.getGestorSheet());
        }

        BackgroundImage fondo = new BackgroundImage(
            imagenFondo,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(1, 1, true, true, false, true)
        );
        setBackground(new Background(fondo));

        VBox botones = new VBox(ESPACIADO_BOTONES, this.botonJugar, this.botonSalir);
        botones.setAlignment(Pos.CENTER);
        botones.setMaxWidth(ANCHO_MAX_BOTONES);

        this.marcoMenu = envolverEnMarco(botones);
        getChildren().add(this.marcoMenu);
        StackPane.setAlignment(this.marcoMenu, Pos.BOTTOM_CENTER);
        StackPane.setMargin(this.marcoMenu, new Insets(0, 0, MARGEN_INFERIOR_BOTONES, 0));

        if (hojaEstilosUrl != null && !hojaEstilosUrl.isBlank()) {
            getStylesheets().add(hojaEstilosUrl);
        }
    }

    private StackPane envolverEnMarco(Node contenido) {
        if (sheetGui == null || sheetGui.getImagen() == null) {
            StackPane marcoFallback = new StackPane(contenido);
            marcoFallback.setBackground(new Background(new BackgroundFill(DEGRADADO_FONDO_MARCO, new CornerRadii(4), Insets.EMPTY)));
            marcoFallback.setBorder(new Border(new BorderStroke(
                Color.web("#d5bd82"), BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(2)
            )));
            marcoFallback.setPadding(new Insets(20, 26, 20, 26));
            marcoFallback.setEffect(SOMBRA_MARCO);
            return marcoFallback;
        }

        Region fondo = new Region();
        fondo.setBackground(new Background(new BackgroundFill(DEGRADADO_FONDO_MARCO, new CornerRadii(4), Insets.EMPTY)));

        StackPane capaContenido = new StackPane(contenido);
        capaContenido.setPadding(new Insets(PADDING_VERTICAL_MARCO, PADDING_HORIZONTAL_MARCO, PADDING_VERTICAL_MARCO, PADDING_HORIZONTAL_MARCO));

        BorderPane marcoOverlay = crearMarcoOverlay();

        StackPane panelMarco = new StackPane(fondo, capaContenido, marcoOverlay);
        panelMarco.setEffect(SOMBRA_MARCO);
        panelMarco.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        return panelMarco;
    }

    private BorderPane crearMarcoOverlay() {
        BorderPane marco = new BorderPane();
        marco.setMouseTransparent(true);

        VistaImagen esqSupIzq = crearRecorteVistaImagen("doble_esquina_superior_izquierda", TAMANO_ESQUINA_MARCO, TAMANO_ESQUINA_MARCO);
        VistaImagen bordeArriba = crearRecorteVistaImagen("doble_borde_arriba", 0, GROSOR_BORDE_HORIZONTAL_MARCO);
        VistaImagen esqSupDer = crearRecorteVistaImagen("doble_esquina_superior_derecha", TAMANO_ESQUINA_MARCO, TAMANO_ESQUINA_MARCO);

        HBox filaSuperior = new HBox();
        filaSuperior.setAlignment(Pos.TOP_CENTER);
        filaSuperior.setMinHeight(TAMANO_ESQUINA_MARCO);
        filaSuperior.setPrefHeight(TAMANO_ESQUINA_MARCO);
        filaSuperior.setMaxHeight(TAMANO_ESQUINA_MARCO);

        if (esqSupIzq != null) {
            filaSuperior.getChildren().add(esqSupIzq);
        }
        if (bordeArriba != null) {
            HBox.setHgrow(bordeArriba, Priority.ALWAYS);
            bordeArriba.fitWidthProperty().bind(Bindings.max(0, filaSuperior.widthProperty().subtract(TAMANO_ESQUINA_MARCO * 2)));
            filaSuperior.getChildren().add(bordeArriba);
        }
        if (esqSupDer != null) {
            filaSuperior.getChildren().add(esqSupDer);
        }

        VistaImagen esqInfIzq = crearRecorteVistaImagen("doble_esquina_inferior_izquierda", TAMANO_ESQUINA_MARCO, TAMANO_ESQUINA_MARCO);
        VistaImagen bordeAbajo = crearRecorteVistaImagen("doble_borde_abajo", 0, GROSOR_BORDE_HORIZONTAL_MARCO);
        VistaImagen esqInfDer = crearRecorteVistaImagen("doble_esquina_inferior_derecha", TAMANO_ESQUINA_MARCO, TAMANO_ESQUINA_MARCO);

        HBox filaInferior = new HBox();
        filaInferior.setAlignment(Pos.BOTTOM_CENTER);
        filaInferior.setMinHeight(TAMANO_ESQUINA_MARCO);
        filaInferior.setPrefHeight(TAMANO_ESQUINA_MARCO);
        filaInferior.setMaxHeight(TAMANO_ESQUINA_MARCO);

        if (esqInfIzq != null) {
            filaInferior.getChildren().add(esqInfIzq);
        }
        if (bordeAbajo != null) {
            HBox.setHgrow(bordeAbajo, Priority.ALWAYS);
            bordeAbajo.fitWidthProperty().bind(Bindings.max(0, filaInferior.widthProperty().subtract(TAMANO_ESQUINA_MARCO * 2)));
            filaInferior.getChildren().add(bordeAbajo);
        }
        if (esqInfDer != null) {
            filaInferior.getChildren().add(esqInfDer);
        }

        VistaImagen bordeIzq = crearRecorteVistaImagen("doble_borde_izquierdo", GROSOR_BORDE_VERTICAL_MARCO, 0);
        if (bordeIzq != null) {
            BorderPane.setAlignment(bordeIzq, Pos.CENTER_LEFT);
            bordeIzq.fitHeightProperty().bind(Bindings.max(0, marco.heightProperty().subtract(TAMANO_ESQUINA_MARCO * 2)));
            marco.setLeft(bordeIzq);
        }

        VistaImagen bordeDer = crearRecorteVistaImagen("doble_borde_derecho", GROSOR_BORDE_VERTICAL_MARCO, 0);
        if (bordeDer != null) {
            BorderPane.setAlignment(bordeDer, Pos.CENTER_RIGHT);
            bordeDer.fitHeightProperty().bind(Bindings.max(0, marco.heightProperty().subtract(TAMANO_ESQUINA_MARCO * 2)));
            marco.setRight(bordeDer);
        }

        marco.setTop(filaSuperior);
        marco.setBottom(filaInferior);
        return marco;
    }

    private VistaImagen crearRecorteVistaImagen(String nombreRecorte, double ancho, double alto) {
        if (sheetGui == null || sheetGui.getImagen() == null) {
            return null;
        }
        GestorSheetEstatica gestor = new GestorSheetEstatica(sheetGui);
        if (!gestor.setRecorte(nombreRecorte)) {
            return null;
        }
        gestoresSheet.add(gestor);

        VistaImagen vista = new VistaImagen(sheetGui.getImagen());
        gestor.suscribir(vista.getObservadorRectangulo());

        if (ancho > 0) {
            vista.setFitWidth(ancho);
        }
        if (alto > 0) {
            vista.setFitHeight(alto);
        }
        vista.setPreserveRatio(false);
        vista.setSmooth(false);
        return vista;
    }

    public Parent getRaiz() {
        return this;
    }

    public StackPane getMarcoMenu() {
        return marcoMenu;
    }

    public BotonMenu getBotonJugar() {
        return botonJugar;
    }

    public BotonMenu getBotonSalir() {
        return botonSalir;
    }

    public List<GestorSheetEstatica> getGestoresSheet() {
        return List.copyOf(gestoresSheet);
    }

    public void setOnJugar(Runnable accion) {
        Objects.requireNonNull(accion, "La acción de jugar no puede ser nula");
        botonJugar.setOnAction(event -> accion.run());
    }

    public void setOnSalir(Runnable accion) {
        Objects.requireNonNull(accion, "La acción de salir no puede ser nula");
        botonSalir.setOnAction(event -> accion.run());
    }
}
