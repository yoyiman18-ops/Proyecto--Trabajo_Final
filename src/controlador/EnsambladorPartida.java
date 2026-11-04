package controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

import dao.PuntajeDAO;
import dao.PuntajeDAOSQLite;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import modelo.Jugador;
import modelo.JugadorFactory;
import modelo.ModeloPartida;
import modelo.Partida;
import modelo.Puntaje;
import modelo.enemigo.EsqueletoFactory;
import modelo.enemigo.SpawnerEnemigos;
import motor.Motor;
import motor.entrada.Accion;
import motor.entrada.MapeoTeclado;
import motor.entrada.TipoEntrada;
import motor.modelo.graficos.Sheet;
import motor.persistencia.dao.sqlite.Conexion;
import motor.recursos.GestorRecursos;
import motor.util.VecDouble2D;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.NotificadorFuerte;
import vista.VistaPartida;

/**
 * Ensamblador responsable de configurar y enlazar los componentes de la partida:
 * modelo, vista, controlador y controles de entrada.
 * Desacopla la inicialización de bajo nivel de la clase principal App.
 */
public class EnsambladorPartida {

    /**
     * Representa el resultado del ensamblado de una partida lista para ejecutarse.
     */
    public record PartidaEnsamblada(
        ControladorPartida controlador,
        Scene escena
    ) {
        public PartidaEnsamblada {
            Objects.requireNonNull(controlador, "El controlador de la partida no puede ser nulo");
            Objects.requireNonNull(escena, "La escena no puede ser nula");
        }
    }

    private final Motor motor;
    private final GestorRecursos gestorRecursos;

    public EnsambladorPartida(Motor motor, GestorRecursos gestorRecursos) {
        this.motor = Objects.requireNonNull(motor, "El motor no puede ser nulo");
        this.gestorRecursos = Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
    }

    public PartidaEnsamblada ensamblar(double anchoEscena, double altoEscena) {
        configurarMapeosTeclado();

        ControladorVistaJuego controladorVista = new ControladorVistaJuego(gestorRecursos, 32);
        VistaPartida vista = controladorVista.getVistaJuego();
        Scene escena = new Scene(vista.getRaiz(), anchoEscena, altoEscena);

        Jugador jugador = crearJugador(anchoEscena, altoEscena);
        ModeloPartida modeloPartida = crearModeloPartida(jugador);
        PuntajeDAO puntajeDAO = crearPuntajeDAO();
        ControladorPartida controladorPartida = new ControladorPartida(
            modeloPartida,
            controladorVista,
            motor,
            puntajeDAO
        );

        return new PartidaEnsamblada(controladorPartida, escena);
    }

    private PuntajeDAO crearPuntajeDAO() {
        try {
            Connection conexion = Conexion.getInstancia("jdbc:sqlite:src/recursos/datos.db").getConexion();
            return new PuntajeDAOSQLite(conexion);
        } catch (SQLException e) {
            return new PuntajeDAO() {
                @Override public void guardar(Puntaje puntaje) {}
                @Override public void borrar(Puntaje puntaje) {}
            };
        }
    }

    private void configurarMapeosTeclado() {
        motor.teclado().añadirMapeo(new MapeoTeclado(KeyCode.A, TipoEntrada.MANTENER, Accion.MOVER_IZQUIERDA));
        motor.teclado().añadirMapeo(new MapeoTeclado(KeyCode.D, TipoEntrada.MANTENER, Accion.MOVER_DERECHA));
        motor.teclado().añadirMapeo(new MapeoTeclado(KeyCode.W, TipoEntrada.MANTENER, Accion.MOVER_ARRIBA));
        motor.teclado().añadirMapeo(new MapeoTeclado(KeyCode.S, TipoEntrada.MANTENER, Accion.MOVER_ABAJO));
        motor.teclado().añadirMapeo(new MapeoTeclado(KeyCode.SPACE, TipoEntrada.PRESIONAR, Accion.ATACAR));
    }

    private Jugador crearJugador(double anchoArea, double altoArea) {
        Sheet sheetJugador = motor.getSheetDAO().cargar(1);
        Rectangle2D rectanguloJugador = sheetJugador.getRecortes().getFirst().getRectangulo();
        return new JugadorFactory().get(
            rectanguloJugador.getWidth(),
            rectanguloJugador.getHeight(),
            new VecDouble2D(
                (anchoArea - rectanguloJugador.getWidth()) / 2.0,
                (altoArea - rectanguloJugador.getHeight()) / 2.0
            )
        );
    }

    private ModeloPartida crearModeloPartida(Jugador jugador) {
        SpawnerEnemigos spawner = new SpawnerEnemigos(new EsqueletoFactory());
        return new Partida(
            jugador,
            spawner,
            5,
            6,
            6.0,
            75.0,
            8.0,
            new NotificadorDebil<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            SonidoJuego.MOSTRAR_MEJORAS.getId(),
            SonidoJuego.MEJORA_APLICADA.getId(),
            SonidoJuego.VICTORIA.getId(),
            SonidoJuego.DERROTA.getId()
        );
    }
}
