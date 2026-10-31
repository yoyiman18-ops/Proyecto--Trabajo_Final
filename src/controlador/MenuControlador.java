package controlador;

import java.util.Objects;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import motor.modelo.graficos.Sheet;
import motor.persistencia.dao.DAOSoloLectura;
import motor.persistencia.dao.sqlite.Conexion;
import motor.persistencia.dao.sqlite.SheetDAOSQLite;
import motor.recursos.Extension;
import motor.recursos.GestorAudio;
import motor.recursos.GestorRecursos;
import modelo.BotonMenu;
import vista.VistaMenu;

public final class MenuControlador {

    public static final String RECURSO_IMAGEN_MENU = "menu";
    public static final String URL_BD = "jdbc:sqlite:src/recursos/datos.db";
    public static final int ID_SHEET_HUD = 2;

    private final VistaMenu vista;

    public MenuControlador(
        GestorRecursos gestorRecursos,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this(
            crearVistaMenu(gestorRecursos),
            null,
            onJugar,
            onSalir
        );
    }

    public MenuControlador(
        GestorRecursos gestorRecursos,
        GestorAudio gestorAudio,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this(
            crearVistaMenu(gestorRecursos),
            gestorAudio,
            onJugar,
            onSalir
        );
    }

    public MenuControlador(
        GestorRecursos gestorRecursos,
        Sheet sheetHud,
        GestorAudio gestorAudio,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this(
            crearVistaMenu(gestorRecursos, sheetHud),
            gestorAudio,
            onJugar,
            onSalir
        );
    }

    public MenuControlador(
        GestorRecursos gestorRecursos,
        BotonMenu botonJugar,
        BotonMenu botonSalir,
        Sheet sheetHud,
        GestorAudio gestorAudio,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this(
            new VistaMenu(
                gestorRecursos.getImagen(RECURSO_IMAGEN_MENU, Extension.Imagen.PNG),
                botonJugar,
                botonSalir,
                sheetHud,
                null
            ),
            gestorAudio,
            onJugar,
            onSalir
        );
    }

    public MenuControlador(
        GestorRecursos gestorRecursos,
        String hojaEstilosUrl,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this(
            crearVistaMenu(gestorRecursos, hojaEstilosUrl),
            null,
            onJugar,
            onSalir
        );
    }

    public MenuControlador(
        GestorRecursos gestorRecursos,
        GestorAudio gestorAudio,
        String hojaEstilosUrl,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this(
            crearVistaMenu(gestorRecursos, hojaEstilosUrl),
            gestorAudio,
            onJugar,
            onSalir
        );
    }

    public MenuControlador(
        GestorRecursos gestorRecursos,
        Sheet sheetHud,
        GestorAudio gestorAudio,
        String hojaEstilosUrl,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this(
            crearVistaMenu(gestorRecursos, sheetHud, hojaEstilosUrl),
            gestorAudio,
            onJugar,
            onSalir
        );
    }

    public MenuControlador(
        VistaMenu vista,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this(vista, null, onJugar, onSalir);
    }

    public MenuControlador(
        VistaMenu vista,
        GestorAudio gestorAudio,
        Runnable onJugar,
        Runnable onSalir
    ) {
        this.vista = Objects.requireNonNull(vista, "La vista del menú no puede ser nula");
        Objects.requireNonNull(onJugar, "La acción de jugar no puede ser nula");
        Objects.requireNonNull(onSalir, "La acción de salir no puede ser nula");

        MusicaJuego.MENU.reproducirEn(gestorAudio);

        this.vista.setOnJugar(() -> {
            SonidoJuego.BOTON.reproducirEn(gestorAudio);
            onJugar.run();
        });
        this.vista.setOnSalir(() -> {
            SonidoJuego.BOTON.reproducirEn(gestorAudio);
            onSalir.run();
        });
    }

    public static Sheet cargarSheetHudDesdeBaseDeDatos(GestorRecursos gestorRecursos) {
        Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        DAOSoloLectura<Integer, Sheet> sheetDAO = new SheetDAOSQLite(
            Conexion.getInstancia(URL_BD).getConexion(),
            gestorRecursos
        );
        return sheetDAO.cargar(ID_SHEET_HUD);
    }

    private static VistaMenu crearVistaMenu(GestorRecursos gestorRecursos) {
        return crearVistaMenu(gestorRecursos, cargarSheetHudDesdeBaseDeDatos(gestorRecursos));
    }

    private static VistaMenu crearVistaMenu(GestorRecursos gestorRecursos, Sheet sheetHud) {
        Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        Image imagenMenu = gestorRecursos.getImagen(RECURSO_IMAGEN_MENU, Extension.Imagen.PNG);
        return new VistaMenu(imagenMenu, sheetHud);
    }

    private static VistaMenu crearVistaMenu(GestorRecursos gestorRecursos, String hojaEstilosUrl) {
        return crearVistaMenu(gestorRecursos, cargarSheetHudDesdeBaseDeDatos(gestorRecursos), hojaEstilosUrl);
    }

    private static VistaMenu crearVistaMenu(GestorRecursos gestorRecursos, Sheet sheetHud, String hojaEstilosUrl) {
        Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        Image imagenMenu = gestorRecursos.getImagen(RECURSO_IMAGEN_MENU, Extension.Imagen.PNG);
        return new VistaMenu(imagenMenu, sheetHud, hojaEstilosUrl);
    }

    public Parent getVista() {
        return vista.getRaiz();
    }

    public StackPane getMarcoMenu() {
        return vista.getMarcoMenu();
    }

    public BotonMenu getBotonJugar() {
        return vista.getBotonJugar();
    }

    public BotonMenu getBotonSalir() {
        return vista.getBotonSalir();
    }
}
