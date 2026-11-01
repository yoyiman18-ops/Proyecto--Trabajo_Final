package controlador;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import modelo.OpcionMenu;
import motor.modelo.graficos.Sheet;
import motor.persistencia.dao.DAOSoloLectura;
import motor.persistencia.dao.sqlite.Conexion;
import motor.persistencia.dao.sqlite.SheetDAOSQLite;
import motor.recursos.Extension;
import motor.recursos.GestorRecursos;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorFuerte;
import motor.util.observer.Observador;
import vista.BotonMenu;
import vista.VistaMenu;

/**
 * Controlador del menú principal en la arquitectura MVC.
 * Coordina los modelos de opciones de menú (OpcionMenu), el audio y la vista (VistaMenu).
 */
public final class MenuControlador {

    public static final String RECURSO_IMAGEN_MENU_DEFAULT = "menu";
    public static final String URL_BD_DEFAULT = "jdbc:sqlite:src/recursos/datos.db";
    public static final int ID_SHEET_HUD_DEFAULT = 2;

    private final VistaMenu vista;
    private final List<OpcionMenu> opciones;
    private final Notificador<Integer> notificadorSonido;

    public MenuControlador(
        GestorRecursos gestorRecursos,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this(gestorRecursos, (Observador<Integer>) null, onJugar, onSalir);
    }

    public MenuControlador(
        GestorRecursos gestorRecursos,
        Observador<Integer> observadorSonido,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this(
            gestorRecursos,
            cargarSheetHudDesdeBaseDeDatos(gestorRecursos, URL_BD_DEFAULT, ID_SHEET_HUD_DEFAULT),
            observadorSonido,
            onJugar,
            onSalir
        );
    }

    public MenuControlador(
        GestorRecursos gestorRecursos,
        Observador<Integer> observadorSonido,
        List<OpcionMenu> opciones
    ) {
        this(
            gestorRecursos,
            cargarSheetHudDesdeBaseDeDatos(gestorRecursos, URL_BD_DEFAULT, ID_SHEET_HUD_DEFAULT),
            observadorSonido,
            opciones
        );
    }

    public MenuControlador(
        GestorRecursos gestorRecursos,
        Sheet sheetHud,
        Observador<Integer> observadorSonido,
        List<OpcionMenu> opciones
    ) {
        this(
            crearVistaMenu(gestorRecursos, sheetHud, RECURSO_IMAGEN_MENU_DEFAULT, opciones),
            observadorSonido,
            opciones
        );
    }

    public MenuControlador(
        GestorRecursos gestorRecursos,
        Sheet sheetHud,
        Observador<Integer> observadorSonido,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this.notificadorSonido = new NotificadorFuerte<>();
        if (observadorSonido != null) {
            this.notificadorSonido.suscribirObservador(observadorSonido);
        }
        this.opciones = crearOpcionesPorDefecto(this.notificadorSonido, onJugar, onSalir);
        this.vista = crearVistaMenu(gestorRecursos, sheetHud, RECURSO_IMAGEN_MENU_DEFAULT, this.opciones);
    }

    public MenuControlador(
        VistaMenu vista,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this(vista, (Observador<Integer>) null, onJugar, onSalir);
    }

    public MenuControlador(
        VistaMenu vista,
        Observador<Integer> observadorSonido,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this.vista = Objects.requireNonNull(vista, "La vista del menú no puede ser nula");
        this.notificadorSonido = new NotificadorFuerte<>();
        if (observadorSonido != null) {
            this.notificadorSonido.suscribirObservador(observadorSonido);
        }
        this.opciones = crearOpcionesPorDefecto(this.notificadorSonido, onJugar, onSalir);
    }

    public MenuControlador(
        VistaMenu vista,
        Observador<Integer> observadorSonido,
        List<OpcionMenu> opciones
    ) {
        this.vista = Objects.requireNonNull(vista, "La vista del menú no puede ser nula");
        this.opciones = new ArrayList<>(Objects.requireNonNullElse(opciones, List.of()));
        this.notificadorSonido = new NotificadorFuerte<>();
        if (observadorSonido != null) {
            this.notificadorSonido.suscribirObservador(observadorSonido);
        }
    }

    private static List<OpcionMenu> crearOpcionesPorDefecto(
        Notificador<Integer> notificadorSonido,
        Runnable onJugar,
        Runnable onSalir
    ) {
        Objects.requireNonNull(onJugar, "La acción de jugar no puede ser nula");
        Objects.requireNonNull(onSalir, "La acción de salir no puede ser nula");

        return List.of(
            new OpcionMenu("JUGAR", () -> {
                notificadorSonido.notificar(SonidoJuego.BOTON.getId());
                onJugar.run();
            }),
            new OpcionMenu("SALIR", () -> {
                notificadorSonido.notificar(SonidoJuego.BOTON.getId());
                onSalir.run();
            })
        );
    }

    public static Sheet cargarSheetHudDesdeBaseDeDatos(GestorRecursos gestorRecursos) {
        return cargarSheetHudDesdeBaseDeDatos(gestorRecursos, URL_BD_DEFAULT, ID_SHEET_HUD_DEFAULT);
    }

    public static Sheet cargarSheetHudDesdeBaseDeDatos(
        GestorRecursos gestorRecursos,
        String urlBaseDatos,
        int idSheet
    ) {
        Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        Objects.requireNonNull(urlBaseDatos, "La URL de la base de datos no puede ser nula");
        DAOSoloLectura<Integer, Sheet> sheetDAO = new SheetDAOSQLite(
            Conexion.getInstancia(urlBaseDatos).getConexion(),
            gestorRecursos
        );
        return sheetDAO.cargar(idSheet);
    }

    private static VistaMenu crearVistaMenu(
        GestorRecursos gestorRecursos,
        Sheet sheetHud,
        String recursoFondo,
        List<OpcionMenu> opciones
    ) {
        Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        Objects.requireNonNull(recursoFondo, "El recurso de fondo no puede ser nulo");
        Image imagenMenu = gestorRecursos.getImagen(recursoFondo, Extension.Imagen.PNG);
        return new VistaMenu(imagenMenu, sheetHud, opciones);
    }

    public Parent getVista() {
        return vista.getRaiz();
    }

    public StackPane getMarcoMenu() {
        return vista.getMarcoMenu();
    }

    public List<OpcionMenu> getOpciones() {
        return List.copyOf(opciones);
    }

    public BotonMenu getBotonJugar() {
        return vista.getBotonJugar();
    }

    public BotonMenu getBotonSalir() {
        return vista.getBotonSalir();
    }
}
