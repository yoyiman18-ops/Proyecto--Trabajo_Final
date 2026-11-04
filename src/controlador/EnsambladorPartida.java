package controlador;

import java.util.Objects;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import modelo.Jugador;
import modelo.JugadorFactory;
import modelo.ModeloPartida;
import modelo.Partida;
import motor.Motor;
import motor.entrada.Accion;
import motor.entrada.MapeoTeclado;
import motor.entrada.TipoEntrada;
import motor.modelo.graficos.Sheet;
import motor.recursos.GestorRecursos;
import motor.util.VecDouble2D;
import vista.VistaPartida;

/**
 * Ensamblador responsable de configurar y enlazar los componentes de la partida:
 * modelo, vista, controlador y controles de entrada.
 * Desacopla la inicialización de bajo nivel de la clase principal App.
 */
public class EnsambladorPartida {

    public static final int ID_SHEET_JUGADOR = 1;
    public static final double TAMANIO_TILE_DEFECTO = 32.0;

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

        ControladorVistaJuego controladorVista = new ControladorVistaJuego(gestorRecursos, TAMANIO_TILE_DEFECTO);
        VistaPartida vista = controladorVista.getVistaJuego();
        Scene escena = new Scene(vista.getRaiz(), anchoEscena, altoEscena);

        Jugador jugador = crearJugador(anchoEscena, altoEscena);
        ModeloPartida modeloPartida = crearModeloPartida(jugador);
        ControladorPartida controladorPartida = new ControladorPartida(
            modeloPartida,
            controladorVista,
            motor
        );

        return new PartidaEnsamblada(controladorPartida, escena);
    }

    private void configurarMapeosTeclado() {
        motor.teclado().añadirMapeo(new MapeoTeclado(KeyCode.A, TipoEntrada.MANTENER, Accion.MOVER_IZQUIERDA));
        motor.teclado().añadirMapeo(new MapeoTeclado(KeyCode.D, TipoEntrada.MANTENER, Accion.MOVER_DERECHA));
        motor.teclado().añadirMapeo(new MapeoTeclado(KeyCode.W, TipoEntrada.MANTENER, Accion.MOVER_ARRIBA));
        motor.teclado().añadirMapeo(new MapeoTeclado(KeyCode.S, TipoEntrada.MANTENER, Accion.MOVER_ABAJO));
        motor.teclado().añadirMapeo(new MapeoTeclado(KeyCode.SPACE, TipoEntrada.PRESIONAR, Accion.ATACAR));
    }

    private Jugador crearJugador(double anchoArea, double altoArea) {
        Sheet sheetJugador = motor.getSheetDAO().cargar(ID_SHEET_JUGADOR);
        Rectangle2D rectanguloJugador = sheetJugador.getRecortes().getFirst().getRectangulo();
        return new JugadorFactory(
            rectanguloJugador.getWidth(),
            rectanguloJugador.getHeight(),
            new VecDouble2D(
                (anchoArea - rectanguloJugador.getWidth()) / 2.0,
                (altoArea - rectanguloJugador.getHeight()) / 2.0
            )
        ).get();
    }

    private ModeloPartida crearModeloPartida(Jugador jugador) {
        return new Partida(
            jugador,
            SonidoJuego.MOSTRAR_MEJORAS.getId(),
            SonidoJuego.MEJORA_APLICADA.getId(),
            SonidoJuego.VICTORIA.getId(),
            SonidoJuego.DERROTA.getId()
        );
    }
}
