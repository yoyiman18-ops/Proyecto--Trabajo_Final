package controlador;

import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.util.function.Consumer;

import modelo.Enemigo;
import modelo.Experiencia;
import modelo.ModeloEnemigo;
import modelo.ModeloExperiencia;
import modelo.ModeloJugador;
import modelo.ModeloProyectil;
import modelo.Personaje;
import modelo.Proyectil;
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
    private static final int ID_SHEET_JUGADOR = 1;
    private static final int ID_SHEET_ENEMIGO = 1;
    private static final int ID_SHEET_EXPERIENCIA = 1;
    private static final int ID_SHEET_PROYECTIL = 1;

    private final DAOSoloLectura<Integer, Sheet> sheetDAO;
    private final Consumer<VistaImagen> alCrearVista;

    public FabricaEntidadesPartidaPorDefecto(GestorRecursos gestorRecursos) {
        this(gestorRecursos, vista -> {});
    }

    public FabricaEntidadesPartidaPorDefecto(
        GestorRecursos gestorRecursos,
        Consumer<VistaImagen> alCrearVista
    ) {
        this(
            new SheetDAOSQLite(
                Conexion.getInstancia().get(),
                Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo")
            ),
            alCrearVista
        );
    }

    public FabricaEntidadesPartidaPorDefecto(
        DAOSoloLectura<Integer, Sheet> sheetDAO,
        Consumer<VistaImagen> alCrearVista
    ) {
        this.sheetDAO = Objects.requireNonNull(sheetDAO, "El DAO de sheets no puede ser nulo");
        this.alCrearVista = Objects.requireNonNull(alCrearVista, "El callback de vista no puede ser nulo");
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
            objetivo
        );

        Sheet sheet = cargarSheet(modeloEnemigo.getIdGlobal());
        VistaImagen vista = new VistaImagen(sheet.getImagen());
        GestorSheetAnimada gestorSheet = new GestorSheetAnimada(sheet);
        alCrearVista.accept(vista);
        return new EnemigoControlador(modeloEnemigo, vista, gestorSheet);
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
            jugador
        );

        Sheet sheet = cargarSheet(modeloExp.getIdGlobal());
        VistaImagen vista = new VistaImagen(sheet.getImagen());
        GestorSheetAnimada gestorSheet = new GestorSheetAnimada(sheet);
        alCrearVista.accept(vista);
        return new ExperienciaControlador(modeloExp, vista, gestorSheet);
    }

    @Override
    public ProyectilControlador crearProyectil(
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
        return new ProyectilControlador(modeloProyectil, vista, gestorSheet);
    }

    private Sheet cargarSheet(int idGlobal) {
        Sheet sheet = sheetDAO.cargar(idGlobal);
        if (sheet == null) {
            throw new IllegalStateException("No se encontró el Sheet con idGlobal: " + idGlobal);
        }
        return sheet;
    }
}
