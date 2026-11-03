package vista;

import java.util.ArrayList;
import java.util.Collections;
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
import modelo.OpcionMenu;
import motor.modelo.graficos.GestorSheetEstatica;
import motor.modelo.graficos.Recorte;
import motor.modelo.graficos.Sheet;
import motor.vista.VistaImagen;

/**
 * Vista del menú principal desacoplada y orientada al principio Abierto/Cerrado.
 * Renderiza dinámicamente cualquier colección de opciones (OpcionMenu),
 * adaptando el marco gráfico de spritesheet o el contenedor de respaldo.
 */
public final class VistaMenu extends StackPane {

    public static final String CORTE_ESQUINA_SUP_IZQ = "doble_esquina_superior_izquierda";
    public static final String CORTE_BORDE_ARRIBA = "doble_borde_arriba";
    public static final String CORTE_ESQUINA_SUP_DER = "doble_esquina_superior_derecha";
    public static final String CORTE_BORDE_IZQ = "doble_borde_izquierdo";
    public static final String CORTE_BORDE_DER = "doble_borde_derecho";
    public static final String CORTE_ESQUINA_INF_IZQ = "doble_esquina_inferior_izquierda";
    public static final String CORTE_BORDE_ABAJO = "doble_borde_abajo";
    public static final String CORTE_ESQUINA_INF_DER = "doble_esquina_inferior_derecha";

    private final Sheet sheetGui;
    private final List<BotonMenu> botones;
    private final List<GestorSheetEstatica> gestoresSheet;
    private final VBox contenedorBotones;
    private final StackPane marcoMenu;

    public VistaMenu(Image imagenFondo) {
        this(imagenFondo, null, Collections.emptyList());
    }

    public VistaMenu(Image imagenFondo, Sheet sheetGui) {
        this(imagenFondo, sheetGui, Collections.emptyList());
    }

    public VistaMenu(Image imagenFondo, Sheet sheetGui, List<OpcionMenu> opciones) {
        Objects.requireNonNull(imagenFondo, "La imagen de fondo del menú no puede ser nula");
        this.sheetGui = sheetGui;
        this.botones = new ArrayList<>();
        this.gestoresSheet = new ArrayList<>();

        BackgroundImage fondo = new BackgroundImage(
            imagenFondo,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(1, 1, true, true, false, true)
        );
        setBackground(new Background(fondo));

        this.contenedorBotones = new VBox(12);
        this.contenedorBotones.setAlignment(Pos.CENTER);

        if (opciones != null) {
            for (OpcionMenu opcion : opciones) {
                agregarOpcion(opcion);
            }
        }

        this.marcoMenu = envolverEnMarco(contenedorBotones);
        getChildren().add(this.marcoMenu);
        StackPane.setAlignment(this.marcoMenu, Pos.BOTTOM_CENTER);
        StackPane.setMargin(this.marcoMenu, new Insets(0, 0, 48, 0));
    }

    public void setOpciones(List<OpcionMenu> nuevasOpciones) {
        limpiarOpciones();
        if (nuevasOpciones != null) {
            for (OpcionMenu opcion : nuevasOpciones) {
                agregarOpcion(opcion);
            }
        }
    }

    public void agregarOpcion(OpcionMenu opcion) {
        Objects.requireNonNull(opcion, "La opción de menú no puede ser nula");
        BotonMenu boton = new BotonMenu(opcion, sheetGui);
        agregarBoton(boton);
    }

    public void agregarBoton(BotonMenu boton) {
        Objects.requireNonNull(boton, "El botón no puede ser nulo");
        botones.add(boton);
        contenedorBotones.getChildren().add(boton);
        if (boton.getGestorSheet() != null) {
            gestoresSheet.add(boton.getGestorSheet());
        }
    }

    public boolean removerOpcionPorId(String id) {
        if (id == null) return false;
        String idBuscado = id.trim().toLowerCase();
        for (int i = 0; i < botones.size(); i++) {
            BotonMenu boton = botones.get(i);
            if (idBuscado.equals(boton.getIdOpcion())) {
                removerBoton(boton);
                return true;
            }
        }
        return false;
    }

    public boolean removerBoton(BotonMenu boton) {
        if (boton == null) return false;
        boolean removido = botones.remove(boton);
        if (removido) {
            contenedorBotones.getChildren().remove(boton);
            if (boton.getGestorSheet() != null) {
                gestoresSheet.remove(boton.getGestorSheet());
            }
        }
        return removido;
    }

    public void limpiarOpciones() {
        botones.clear();
        gestoresSheet.clear();
        contenedorBotones.getChildren().clear();
    }

    public List<BotonMenu> getBotones() {
        return Collections.unmodifiableList(botones);
    }

    public List<OpcionMenu> getOpciones() {
        List<OpcionMenu> lista = new ArrayList<>();
        for (BotonMenu b : botones) {
            if (b.getModelo() != null) {
                lista.add(b.getModelo());
            }
        }
        return Collections.unmodifiableList(lista);
    }

    public BotonMenu getBotonPorId(String id) {
        if (id == null) return null;
        String idBuscado = id.trim().toLowerCase();
        for (BotonMenu boton : botones) {
            if (idBuscado.equals(boton.getIdOpcion())) {
                return boton;
            }
        }
        return null;
    }

    public BotonMenu getBotonPorTexto(String texto) {
        if (texto == null) return null;
        for (BotonMenu boton : botones) {
            if (texto.equalsIgnoreCase(boton.getText())) {
                return boton;
            }
        }
        return null;
    }

    public BotonMenu getBotonJugar() {
        BotonMenu boton = getBotonPorId("jugar");
        return boton != null ? boton : getBotonPorTexto("JUGAR");
    }

    public BotonMenu getBotonSalir() {
        BotonMenu boton = getBotonPorId("salir");
        return boton != null ? boton : getBotonPorTexto("SALIR");
    }

    public void setOnJugar(Runnable accion) {
        Objects.requireNonNull(accion, "La acción de jugar no puede ser nula");
        BotonMenu boton = getBotonJugar();
        if (boton != null && boton.getModelo() != null) {
            boton.getModelo().setAccion(accion);
        } else {
            agregarOpcion(new OpcionMenu("jugar", "JUGAR", accion));
        }
    }

    public void setOnSalir(Runnable accion) {
        Objects.requireNonNull(accion, "La acción de salir no puede ser nula");
        BotonMenu boton = getBotonSalir();
        if (boton != null && boton.getModelo() != null) {
            boton.getModelo().setAccion(accion);
        } else {
            agregarOpcion(new OpcionMenu("salir", "SALIR", accion));
        }
    }

    private StackPane envolverEnMarco(Node contenido) {
        LinearGradient degradadoFondo = new LinearGradient(
            0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
            new Stop(0.0, Color.rgb(28, 22, 18, 0.94)),
            new Stop(1.0, Color.rgb(16, 12, 10, 0.96))
        );
        DropShadow sombraMarco = new DropShadow(BlurType.GAUSSIAN, Color.rgb(0, 0, 0, 0.92), 16, 0.5, 0, 4);

        if (sheetGui == null || sheetGui.getImagen() == null) {
            StackPane marcoFallback = new StackPane(contenido);
            marcoFallback.setBackground(new Background(new BackgroundFill(degradadoFondo, new CornerRadii(4), Insets.EMPTY)));
            marcoFallback.setBorder(new Border(new BorderStroke(
                Color.web("#d5bd82"), BorderStrokeStyle.SOLID, new CornerRadii(4), new BorderWidths(2)
            )));
            marcoFallback.setPadding(new Insets(20, 26, 20, 26));
            marcoFallback.setEffect(sombraMarco);
            return marcoFallback;
        }

        Region fondo = new Region();
        fondo.setBackground(new Background(new BackgroundFill(degradadoFondo, new CornerRadii(4), Insets.EMPTY)));

        Recorte corteEsq = sheetGui.getRecorte(CORTE_ESQUINA_SUP_IZQ);
        double paddingV = corteEsq != null ? corteEsq.getAlto() : 22;
        double paddingH = (corteEsq != null ? corteEsq.getAncho() : 22) + 6;

        StackPane capaContenido = new StackPane(contenido);
        capaContenido.setPadding(new Insets(paddingV, paddingH, paddingV, paddingH));

        BorderPane marcoOverlay = crearMarcoOverlay(capaContenido);

        StackPane panelMarco = new StackPane(fondo, capaContenido, marcoOverlay);
        panelMarco.setEffect(sombraMarco);
        panelMarco.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        return panelMarco;
    }

    private BorderPane crearMarcoOverlay(Region capaContenido) {
        BorderPane marco = new BorderPane();
        marco.setMouseTransparent(true);
        marco.setManaged(false);

        marco.prefWidthProperty().bind(capaContenido.widthProperty());
        marco.prefHeightProperty().bind(capaContenido.heightProperty());
        capaContenido.widthProperty().addListener((obs, o, n) -> marco.resize(n.doubleValue(), marco.getHeight()));
        capaContenido.heightProperty().addListener((obs, o, n) -> marco.resize(marco.getWidth(), n.doubleValue()));

        Recorte rEsq = sheetGui.getRecorte(CORTE_ESQUINA_SUP_IZQ);
        double anchoEsquina = rEsq != null ? rEsq.getAncho() : 22;
        double altoEsquina = rEsq != null ? rEsq.getAlto() : 22;

        Recorte rBordeH = sheetGui.getRecorte(CORTE_BORDE_ARRIBA);
        double grosorBordeH = rBordeH != null ? rBordeH.getAlto() : 13;

        Recorte rBordeV = sheetGui.getRecorte(CORTE_BORDE_IZQ);
        double grosorBordeV = rBordeV != null ? rBordeV.getAncho() : 13;

        VistaImagen esqSupIzq = crearRecorteVistaImagen(CORTE_ESQUINA_SUP_IZQ, anchoEsquina, altoEsquina);
        VistaImagen bordeArriba = crearRecorteVistaImagen(CORTE_BORDE_ARRIBA, 0, grosorBordeH);
        VistaImagen esqSupDer = crearRecorteVistaImagen(CORTE_ESQUINA_SUP_DER, anchoEsquina, altoEsquina);

        HBox filaSuperior = new HBox();
        filaSuperior.setAlignment(Pos.TOP_CENTER);
        filaSuperior.setMinHeight(altoEsquina);
        filaSuperior.setPrefHeight(altoEsquina);
        filaSuperior.setMaxHeight(altoEsquina);

        if (esqSupIzq != null) filaSuperior.getChildren().add(esqSupIzq);
        if (bordeArriba != null) {
            HBox.setHgrow(bordeArriba, Priority.ALWAYS);
            bordeArriba.fitWidthProperty().bind(Bindings.max(0, capaContenido.widthProperty().subtract(anchoEsquina * 2)));
            filaSuperior.getChildren().add(bordeArriba);
        }
        if (esqSupDer != null) filaSuperior.getChildren().add(esqSupDer);

        VistaImagen esqInfIzq = crearRecorteVistaImagen(CORTE_ESQUINA_INF_IZQ, anchoEsquina, altoEsquina);
        VistaImagen bordeAbajo = crearRecorteVistaImagen(CORTE_BORDE_ABAJO, 0, grosorBordeH);
        VistaImagen esqInfDer = crearRecorteVistaImagen(CORTE_ESQUINA_INF_DER, anchoEsquina, altoEsquina);

        HBox filaInferior = new HBox();
        filaInferior.setAlignment(Pos.BOTTOM_CENTER);
        filaInferior.setMinHeight(altoEsquina);
        filaInferior.setPrefHeight(altoEsquina);
        filaInferior.setMaxHeight(altoEsquina);

        if (esqInfIzq != null) filaInferior.getChildren().add(esqInfIzq);
        if (bordeAbajo != null) {
            HBox.setHgrow(bordeAbajo, Priority.ALWAYS);
            bordeAbajo.fitWidthProperty().bind(Bindings.max(0, capaContenido.widthProperty().subtract(anchoEsquina * 2)));
            filaInferior.getChildren().add(bordeAbajo);
        }
        if (esqInfDer != null) filaInferior.getChildren().add(esqInfDer);

        VistaImagen bordeIzq = crearRecorteVistaImagen(CORTE_BORDE_IZQ, grosorBordeV, 0);
        if (bordeIzq != null) {
            BorderPane.setAlignment(bordeIzq, Pos.CENTER_LEFT);
            bordeIzq.fitHeightProperty().bind(Bindings.max(0, capaContenido.heightProperty().subtract(altoEsquina * 2)));
            marco.setLeft(bordeIzq);
        }

        VistaImagen bordeDer = crearRecorteVistaImagen(CORTE_BORDE_DER, grosorBordeV, 0);
        if (bordeDer != null) {
            BorderPane.setAlignment(bordeDer, Pos.CENTER_RIGHT);
            bordeDer.fitHeightProperty().bind(Bindings.max(0, capaContenido.heightProperty().subtract(altoEsquina * 2)));
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
        if (!gestor.setAnimacion(nombreRecorte)) {
            return null;
        }
        gestoresSheet.add(gestor);

        VistaImagen vista = new VistaImagen(sheetGui.getImagen());
        gestor.suscribir(vista.getObservadorRectangulo());

        if (ancho > 0) vista.setFitWidth(ancho);
        if (alto > 0) vista.setFitHeight(alto);
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

    public List<GestorSheetEstatica> getGestoresSheet() {
        return List.copyOf(gestoresSheet);
    }
}
