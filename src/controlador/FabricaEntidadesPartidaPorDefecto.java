package controlador;

import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.util.function.Consumer;

import modelo.Enemigo;
import modelo.Experiencia;
import modelo.ModeloEnemigo;
import modelo.ModeloExperiencia;
import modelo.ModeloJugador;
import modelo.Personaje;
import modelo.proyectil.ModeloProyectil;
import modelo.proyectil.Proyectil;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangular;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.modelo.Posicionable;
import motor.modelo.graficos.GestorSheetAnimada;
import motor.modelo.graficos.Sheet;
import motor.persistencia.dao.DAOSoloLectura;
import motor.persistencia.dao.sqlite.Conexion;
import motor.persistencia.dao.sqlite.SheetDAOSQLite;
import motor.recursos.GestorRecursos;
import motor.util.VecDouble2D;
import motor.util.observer.Observador;
import vista.VistaImagen;

public final class FabricaEntidadesPartidaPorDefecto implements FabricaEntidadesPartida {
    private static final String URL_BD_POR_DEFECTO = "jdbc:sqlite:src/recursos/datos.db";
    private static final int ID_SHEET_JUGADOR = 1;
    private static final int ID_SHEET_ENEMIGO = 1;
    private static final int ID_SHEET_EXPERIENCIA = 1;
    private static final int ID_SHEET_PROYECTIL = 1;

    private final DAOSoloLectura<Integer, Sheet> sheetDAO;
    private final Consumer<VistaImagen> alCrearVista;
    private final Observador<Integer> observadorSonido;

    /* Constructores secundarios sin observador (conservados para pruebas o prototipos):
    public FabricaEntidadesPartidaPorDefecto(GestorRecursos gestorRecursos) {
        this(gestorRecursos, vista -> {});
    }

    public FabricaEntidadesPartidaPorDefecto(
        GestorRecursos gestorRecursos,
        Consumer<VistaImagen> alCrearVista
    ) {
        this(gestorRecursos, alCrearVista, id -> {});
    }
    */

    public FabricaEntidadesPartidaPorDefecto(
        GestorRecursos gestorRecursos,
        Consumer<VistaImagen> alCrearVista,
        Observador<Integer> observadorSonido
    ) {
        this(
            new SheetDAOSQLite(
                Conexion.getInstancia(URL_BD_POR_DEFECTO).getConexion(),
                Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo")
            ),
            alCrearVista,
            observadorSonido
        );
    }

    /* Constructor secundario con DAO sin observador:
    public FabricaEntidadesPartidaPorDefecto(
        DAOSoloLectura<Integer, Sheet> sheetDAO,
        Consumer<VistaImagen> alCrearVista
    ) {
        this(sheetDAO, alCrearVista, id -> {});
    }
    */

    public FabricaEntidadesPartidaPorDefecto(
        DAOSoloLectura<Integer, Sheet> sheetDAO,
        Consumer<VistaImagen> alCrearVista,
        Observador<Integer> observadorSonido
    ) {
        this.sheetDAO = Objects.requireNonNull(sheetDAO, "El DAO de sheets no puede ser nulo");
        this.alCrearVista = Objects.requireNonNull(alCrearVista, "El callback de vista no puede ser nulo");
        this.observadorSonido = Objects.requireNonNull(observadorSonido, "El observador de sonido no puede ser nulo");
    }

    @Override
    public ModeloJugador crearModeloJugador() {
        return new Personaje.Builder(
            ID_SHEET_JUGADOR,
            "jugador",
            new HitboxRectangular(
                new Rectangle2D.Double(0, 0, 50, 50),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.JUGADOR),
                MascaraColision.of(
                    CategoriaColision.ENEMIGO,
                    CategoriaColision.EXP,
                    CategoriaColision.MURO
                )
            )
        )
        .posicion(new VecDouble2D(455, 335))
        .friccion(2500)
        .aceleracion(3000)
        .velocidadMax(240)
        .idSonidoDaño(SonidoJuego.DAÑO_JUGADOR.getId())
        .idSonidoMuerte(SonidoJuego.MUERTE_JUGADOR.getId())
        .idSonidoSubirNivel(SonidoJuego.SUBIR_NIVEL.getId())
        .build();
    }

    @Override
    public JugadorControlador crearControladorJugador(
        ModeloJugador modelo,
        Observador<ModeloJugador> observadorVida,
        Runnable accionAtaque
    ) {
        Sheet sheet = cargarSheet(modelo.getIdGlobal());
        VistaImagen vista = new VistaImagen(sheet.getImagen());
        GestorSheetAnimada gestorSheet = new GestorSheetAnimada(sheet);
        alCrearVista.accept(vista);
        return new JugadorControlador(
            modelo,
            vista,
            gestorSheet,
            observadorVida,
            observadorSonido,
            accionAtaque
        );
    }

    @Override
    public EnemigoControlador crearEnemigo(
        double x,
        double y,
        double velocidad,
        Posicionable objetivo
    ) {
        ModeloEnemigo modeloEnemigo = new Enemigo(
            ID_SHEET_ENEMIGO,
            "enemigo",
            new HitboxRectangular(
                new Rectangle2D.Double(0, 0, 32, 32),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.ENEMIGO),
                MascaraColision.of(CategoriaColision.JUGADOR, CategoriaColision.ALIADO)
            ),
            new VecDouble2D(x, y),
            velocidad,
            50,
            10,
            10,
            objetivo,
            SonidoJuego.DAÑO_ENEMIGO.getId(),
            SonidoJuego.EXPIRAR_ENEMIGO.getId()
        );

        Sheet sheet = cargarSheet(modeloEnemigo.getIdGlobal());
        VistaImagen vista = new VistaImagen(sheet.getImagen());
        GestorSheetAnimada gestorSheet = new GestorSheetAnimada(sheet);
        alCrearVista.accept(vista);
        return new EnemigoControlador(modeloEnemigo, vista, gestorSheet, observadorSonido);
    }

    @Override
    public ExperienciaControlador crearExperiencia(
        ModeloJugador jugador,
        double x,
        double y,
        int cantidad
    ) {
        ModeloExperiencia modeloExp = new Experiencia(
            ID_SHEET_EXPERIENCIA,
            "experiencia",
            new HitboxRectangular(
                new Rectangle2D.Double(0, 0, 18, 18),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.EXP),
                MascaraColision.of(CategoriaColision.JUGADOR)
            ),
            new VecDouble2D(x, y),
            cantidad,
            jugador,
            SonidoJuego.RECOGER_EXPERIENCIA.getId()
        );

        Sheet sheet = cargarSheet(modeloExp.getIdGlobal());
        VistaImagen vista = new VistaImagen(sheet.getImagen());
        GestorSheetAnimada gestorSheet = new GestorSheetAnimada(sheet);
        alCrearVista.accept(vista);
        return new ExperienciaControlador(modeloExp, vista, gestorSheet, observadorSonido);
    }

    @Override
    public EntidadControlador crearProyectil(
        double x,
        double y,
        VecDouble2D direccion,
        double danio,
        boolean esCritico
    ) {
        Objects.requireNonNull(direccion, "La dirección del proyectil no puede ser nula");

        double tamanoHitbox = esCritico ? 20 : 16;
        double rapidez = esCritico ? 430 : 380;
        VecDouble2D velocidad = direccion.clone().normalizado();
        velocidad.producto(rapidez);

        ModeloProyectil modeloProyectil = new Proyectil(
            ID_SHEET_PROYECTIL,
            "proyectil",
            new HitboxRectangular(
                new Rectangle2D.Double(0, 0, tamanoHitbox, tamanoHitbox),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.ALIADO),
                MascaraColision.of(CategoriaColision.ENEMIGO)
            ),
            new VecDouble2D(x, y),
            velocidad,
            danio,
            1.6,
            1
        );

        Sheet sheet = cargarSheet(modeloProyectil.getIdGlobal());
        VistaImagen vista = new VistaImagen(sheet.getImagen());
        GestorSheetAnimada gestorSheet = new GestorSheetAnimada(sheet);
        alCrearVista.accept(vista);
        return new EntidadControlador(modeloProyectil, vista, gestorSheet, observadorSonido);
    }

    private Sheet cargarSheet(int idGlobal) {
        Sheet sheet = sheetDAO.cargar(idGlobal);
        if (sheet == null) {
            throw new IllegalStateException("No se encontró el Sheet con idGlobal: " + idGlobal);
        }
        return sheet;
    }
}
