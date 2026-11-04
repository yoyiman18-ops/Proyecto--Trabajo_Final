package main;

/*
 * Imports del prototipo anterior, conservados para futuras implementaciones:
 * import vista.SpriteVista;
 * import javafx.scene.control.Label;
 * import vista.MenuVista;
 * import javafx.scene.control.Label;
 * import controlador.JugadorControlador;
 * import javafx.animation.AnimationTimer;
 * import javafx.geometry.Rectangle2D;
 * import javafx.scene.layout.Pane;
 * import vista.SpriteVista;
 * import motor.colisiones.*;
 * import motor.colisiones.hitboxes.CategoriaColision;
 * import motor.colisiones.hitboxes.Hitbox;
 * import motor.colisiones.hitboxes.HitboxGenerica;
 * import motor.colisiones.hitboxes.HitboxRectangular;
 * import motor.colisiones.hitboxes.MascaraColision;
 * import motor.colisiones.hitboxes.TipoHitbox;
 * import motor.colisiones.sistema.FaseEspecificaSimple;
 * import motor.colisiones.sistema.FaseGeneralSpatialHashGrid;
 * import motor.entrada.EstadoEntradaTeclado;
 * import motor.entrada.GestorEntradaTeclado;
 * import motor.modelo.Entidad;
 * import motor.recursos.GestorRecursos;
 * import motor.recursos.cache.CacheImagenes;
 * import motor.util.VecDouble2D;
 * import motor.util.observer.Observador;
 * import java.awt.Shape;
 * import java.awt.geom.Ellipse2D;
 * import java.util.ArrayList;
 * import java.util.List;
 * import java.util.concurrent.atomic.AtomicInteger;
 * import javax.swing.Timer;
 * import modelo.Entidad;
 * import modelo.EntidadViva;
 * import controlador.EnemigoControlador;
 * import controlador.ExperienciaControlador;
 * import controlador.ProyectilControlador;
 * import controlador.VidaControlador;
 */

import controlador.ControladorFondoMazmorra;
import controlador.ControladorVistaJuego;
import controlador.EnsambladorPartida;
import controlador.MenuControlador;
import controlador.MusicaJuego;
import controlador.SonidoJuego;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import motor.Motor;
import motor.recursos.GestorAudio;
import motor.recursos.GestorRecursos;

// nota: en esta clase se prueban de forma arbitraria las características añadidas.

public class App extends Application {
    private static final double ANCHO_VENTANA = 960.0;
    private static final double ALTO_VENTANA = 720.0;

    private final GestorRecursos gestorRecursos;
    private final GestorAudio gestorAudio;
    private final EnsambladorPartida ensambladorPartida;

    public App() {
        this.gestorRecursos = new GestorRecursos();
        this.gestorAudio = new GestorAudio(this.gestorRecursos);
        this.ensambladorPartida = new EnsambladorPartida(Motor.getInstancia(), this.gestorRecursos);
        SonidoJuego.precargarTodos(this.gestorAudio);
        ControladorVistaJuego.precargarRecursosHud(this.gestorRecursos);
        ControladorFondoMazmorra.precargarTiles(this.gestorRecursos);
    }

    @Override
    public void start(Stage stage) {
        mostrarMenu(stage);
    }

    private void mostrarMenu(Stage stage) {
        MenuControlador menuControlador = new MenuControlador(
            gestorRecursos,
            gestorAudio,
            () -> iniciarPartida(stage),
            Platform::exit
        );
        Scene escena = new Scene(menuControlador.getVista(), ANCHO_VENTANA, ALTO_VENTANA);
        stage.setScene(escena);
        stage.setTitle("Waves 2D");
        MusicaJuego.MENU.reproducirEn(gestorAudio);
        stage.show();
    }

    private void iniciarPartida(Stage stage) {
        EnsambladorPartida.PartidaEnsamblada partida = ensambladorPartida.ensamblar(ANCHO_VENTANA, ALTO_VENTANA);
        stage.setScene(partida.escena());
        MusicaJuego.PARTIDA.reproducirEn(gestorAudio);
        partida.controlador().iniciar(
            partida.escena(),
            () -> mostrarMenu(stage),
            () -> iniciarPartida(stage)
        );
    }

    @Override
    public void stop() throws Exception {
        Motor.getInstancia().detener();
        gestorAudio.cerrar();
        gestorRecursos.cerrar();
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
