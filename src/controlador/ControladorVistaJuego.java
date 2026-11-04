package controlador;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.IntConsumer;
import java.util.concurrent.CompletableFuture;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import modelo.ModeloPartida;
import motor.Motor;
import motor.modelo.graficos.Sheet;
import motor.persistencia.dao.DAOSoloLectura;
import motor.persistencia.dao.sqlite.Conexion;
import motor.persistencia.dao.sqlite.SheetDAOSQLite;
import motor.recursos.Extension;
import motor.recursos.GestorRecursos;
import motor.util.observer.Observador;
import vista.ConfiguracionMazmorra;
import vista.OpcionMejoraVisual;
import vista.VistaFondo;
import vista.VistaHud;
import vista.VistaJuego;
import vista.VistaPartida;

public final class ControladorVistaJuego {

    public static final int ID_SHEET_HUD = 2;

    private static final Map<String, String> RECURSOS_ICONOS_HUD = Map.of(
        "corazonLleno", "0x72_DungeonTilesetII_v1.7/frames/ui_heart_full",
        "corazonVacio", "0x72_DungeonTilesetII_v1.7/frames/ui_heart_empty",
        "iconoExperiencia", "0x72_DungeonTilesetII_v1.7/frames/flask_big_blue",
        "gemaExperienciaVacia", "hud_gema_gris",
        "gemaExperienciaLlena", "hud_gema_verde",
        "iconoTiempo", "0x72_DungeonTilesetII_v1.7/frames/flask_big_yellow",
        "iconoOleada", "0x72_DungeonTilesetII_v1.7/frames/wall_banner_red",
        "iconoEnemigos", "0x72_DungeonTilesetII_v1.7/frames/skull",
        "iconoDanio", "0x72_DungeonTilesetII_v1.7/frames/weapon_red_magic_staff",
        "iconoPuntos", "0x72_DungeonTilesetII_v1.7/frames/coin_anim_f0"
    );

    public static CompletableFuture<Void> precargarRecursosHud(GestorRecursos gestorRecursos) {
        if (gestorRecursos == null) {
            return CompletableFuture.completedFuture(null);
        }
        return gestorRecursos.precargarImagenes(RECURSOS_ICONOS_HUD.values(), Extension.Imagen.PNG);
    }

    private final VistaPartida vista;
    private final VistaFondo fondoMazmorra;
    private final ControladorFondoMazmorra controladorFondo;
    private final Observador<Double> observadorActualizacion;
    private boolean eleccionMejoraActiva;
    private boolean enPausa;

    public ControladorVistaJuego(VistaPartida vista) {
        this(vista, null);
    }

    public ControladorVistaJuego(VistaPartida vista, VistaFondo fondoMazmorra) {
        this.vista = Objects.requireNonNull(vista, "La vista del juego no puede ser nula");
        this.fondoMazmorra = fondoMazmorra;
        this.controladorFondo = fondoMazmorra != null ? new ControladorFondoMazmorra(fondoMazmorra) : null;
        this.observadorActualizacion = this::actualizar;
        this.eleccionMejoraActiva = false;
        this.enPausa = false;
    }

    public ControladorVistaJuego(GestorRecursos gestorRecursos, double tamanoTile) {
        this(gestorRecursos, null, new ConfiguracionMazmorra(tamanoTile));
    }

    public ControladorVistaJuego(
        GestorRecursos gestorRecursos,
        Sheet sheetHud,
        ConfiguracionMazmorra configuracionMazmorra
    ) {
        Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        Objects.requireNonNull(configuracionMazmorra, "La configuración de mazmorra no puede ser nula");

        Map<String, Image> imagenesHud = new HashMap<>();
        for (Map.Entry<String, String> entrada : RECURSOS_ICONOS_HUD.entrySet()) {
            imagenesHud.put(
                entrada.getKey(),
                gestorRecursos.getImagen(entrada.getValue(), Extension.Imagen.PNG)
            );
        }
        Sheet recortesHud = sheetHud != null ? sheetHud : cargarSheetHudDesdeBaseDeDatos(gestorRecursos);
        if (recortesHud != null && recortesHud.getImagen() != null) {
            imagenesHud.put("hojaGuiRpg", recortesHud.getImagen());
        }

        VistaHud vistaHud = new VistaHud(imagenesHud, recortesHud);
        this.vista = new VistaJuego(vistaHud);

        this.controladorFondo = new ControladorFondoMazmorra(
            this.vista.getAreaJuego(),
            gestorRecursos,
            configuracionMazmorra
        );
        this.fondoMazmorra = this.controladorFondo.getVista();
        this.vista.agregarNodoJuego(this.fondoMazmorra.getNodo());
        this.observadorActualizacion = this::actualizar;
        this.eleccionMejoraActiva = false;
        this.enPausa = false;
    }

    public static Sheet cargarSheetHudDesdeBaseDeDatos(GestorRecursos gestorRecursos) {
        Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        DAOSoloLectura<Integer, Sheet> sheetDAO = new SheetDAOSQLite(
            Conexion.getInstancia().get(),
            gestorRecursos
        );
        return sheetDAO.cargar(ID_SHEET_HUD);
    }

    public VistaPartida getVistaJuego() {
        return vista;
    }

    public VistaFondo getFondoMazmorra() {
        return fondoMazmorra;
    }

    public ControladorFondoMazmorra getControladorFondo() {
        return controladorFondo;
    }

    public Observador<Double> getObservadorActualizacion() {
        return observadorActualizacion;
    }

    public boolean isEleccionMejoraActiva() {
        return eleccionMejoraActiva;
    }

    public boolean isEnPausa() {
        return enPausa;
    }

    public void setEnPausa(boolean enPausa) {
        this.enPausa = enPausa;
    }

    public void actualizar(double dt) {
        if (controladorFondo != null && !enPausa) {
            controladorFondo.actualizar(dt);
        }
    }

    public void mostrarPausa() {
        vista.mostrarPausa();
    }

    public void cerrarPausa() {
        vista.cerrarPausa();
    }

    public void mostrarFinPartida(String mensaje) {
        vista.mostrarFinPartida(mensaje);
    }

    public void mostrarEleccionMejora(int nivel, List<OpcionMejoraVisual> opciones) {
        this.eleccionMejoraActiva = true;
        vista.mostrarEleccionMejora(nivel, opciones);
    }

    public void cerrarEleccionMejora() {
        this.eleccionMejoraActiva = false;
        vista.cerrarEleccionMejora();
    }

    public void configurarControlesModales(
        Motor motor,
        ModeloPartida modeloPartida,
        Runnable onVolverAlMenu,
        Runnable onReintentar
    ) {
        configurarControlesModales(motor, modeloPartida, onVolverAlMenu, onReintentar, null);
    }

    public void configurarControlesModales(
        Motor motor,
        ModeloPartida modeloPartida,
        Runnable onVolverAlMenu,
        Runnable onReintentar,
        IntConsumer onSeleccionarMejora
    ) {
        Objects.requireNonNull(motor, "El motor no puede ser nulo");
        Objects.requireNonNull(modeloPartida, "El modelo de partida no puede ser nulo");
        Objects.requireNonNull(onVolverAlMenu, "La acción de volver al menú no puede ser nula");
        Objects.requireNonNull(onReintentar, "La acción de reintentar no puede ser nula");

        Runnable accionPausar = () -> {
            if (!modeloPartida.isPartidaFinalizada()) {
                this.enPausa = true;
                modeloPartida.setEnPausa(true);
                motor.setPausa(true);
                vista.mostrarPausa();
            }
        };

        Runnable accionReanudar = () -> {
            vista.cerrarPausa();
            if (!isEleccionMejoraActiva()) {
                this.enPausa = false;
                modeloPartida.setEnPausa(false);
                motor.setPausa(false);
            }
            Platform.runLater(vista::solicitarFoco);
        };

        vista.setOnPausa(accionPausar);
        vista.setOnReanudar(accionReanudar);

        vista.setOnVolverAlMenuDesdePausa(() -> {
            vista.cerrarPausa();
            this.enPausa = false;
            motor.setPausa(false);
            motor.detener();
            motor.limpiar();
            onVolverAlMenu.run();
        });

        vista.setOnReintentar(() -> {
            this.enPausa = false;
            motor.detener();
            motor.limpiar();
            motor.setPausa(false);
            onReintentar.run();
        });

        vista.setOnVolverAlMenuDesdeFinPartida(() -> {
            this.enPausa = false;
            motor.detener();
            motor.limpiar();
            motor.setPausa(false);
            onVolverAlMenu.run();
        });

        vista.setOnSeleccionarMejora(indice -> {
            if (onSeleccionarMejora != null) {
                onSeleccionarMejora.accept(indice);
            } else {
                cerrarEleccionMejora();
                this.enPausa = false;
                motor.setPausa(false);
                modeloPartida.setEnPausa(false);
                Platform.runLater(vista::solicitarFoco);
            }
        });
    }

    public void configurarAtajoTecladoPausa(Scene escena, Motor motor, ModeloPartida modeloPartida) {
        if (escena == null || motor == null || modeloPartida == null) {
            return;
        }
        escena.addEventFilter(KeyEvent.KEY_PRESSED, evento -> {
            if (evento.getCode() == KeyCode.ESCAPE || evento.getCode() == KeyCode.P) {
                if (modeloPartida.isPartidaFinalizada()) {
                    return;
                }
                if (modeloPartida.isEnPausa()) {
                    vista.cerrarPausa();
                    if (!isEleccionMejoraActiva()) {
                        this.enPausa = false;
                        modeloPartida.setEnPausa(false);
                        motor.setPausa(false);
                    }
                    Platform.runLater(vista::solicitarFoco);
                } else {
                    this.enPausa = true;
                    modeloPartida.setEnPausa(true);
                    motor.setPausa(true);
                    vista.mostrarPausa();
                }
                evento.consume();
            }
        });
    }

    public Node getVista() {
        return vista.getRaiz();
    }
}
