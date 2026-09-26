package main;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import vista.SpriteVista;
import motor.Motor;
import motor.colisiones.*;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.Hitbox;
import motor.colisiones.hitboxes.HitboxGenerica;
import motor.colisiones.hitboxes.HitboxRectangular;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.colisiones.sistema.FaseEspecificaSimple;
import motor.colisiones.sistema.FaseGeneralSpatialHashGrid;
import motor.entrada.Accion;
import motor.entrada.EstadoAcciones;
import motor.entrada.EstadoEntradaTeclado;
import motor.entrada.GestorEntradaTeclado;
import motor.entrada.MapeoTeclado;
import motor.entrada.TipoEntrada;
import motor.modelo.Entidad;
import motor.recursos.GestorRecursos;
import motor.recursos.cache.CacheImagenes;
import motor.util.VecDouble2D;
import motor.util.observer.Observador;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.Timer;

import controlador.EnemigoControlador;
import controlador.ProyectilControlador;
import controlador.ExperienciaControlador;
import controlador.VidaControlador;



// nota: en esta clase se prueban de forma arbitraria las características añadidas.

public class App extends Application {
    private Observador<EstadoAcciones> parlante;
    private final Timer llamadorRecolector = new Timer(1000, e -> { System.gc(); }); // eventualmente debería liberar el parlante
    private Timer temporizador;
    private Motor motor;

    @Override
    public void start(Stage stage) {      

        motor = new Motor();
        stage.setScene(motor.getVentanaRoot());
        stage.setTitle("ejemplo del motor");
        stage.show();


    }

    @Override
    public void stop() throws Exception {
        temporizador.stop();
        llamadorRecolector.stop();
    }

        /*
        AtomicInteger bajas = new AtomicInteger();
        Label contadorBajas = new Label("Bajas: 0");
        contadorBajas.setLayoutX(10);
        contadorBajas.setLayoutY(10);
        escenario.getChildren().add(contadorBajas);
        Label progreso = new Label();
        progreso.setLayoutX(10);
        progreso.setLayoutY(35);
        escenario.getChildren().add(progreso);
        Label vida = new Label();
        vida.setLayoutX(10);
        vida.setLayoutY(60);
        escenario.getChildren().add(vida);
        Label tiempo = new Label("Tiempo: 00:00");
        tiempo.setLayoutX(10);
        tiempo.setLayoutY(85);
        escenario.getChildren().add(tiempo);
        JugadorControlador jugadorControlador = new JugadorControlador(
                e1, vista1, enemigos, proyectiles, escenario, experiencias, progreso
        );

        agregarEnemigo(25, 25, new Rectangle2D(18, 18, 45, 45), e1, escenario, bajas, contadorBajas);
        agregarEnemigo(225, 25, new Rectangle2D(83, 82, 55, 55), e1, escenario, bajas, contadorBajas);
        agregarEnemigo(25, 225, new Rectangle2D(145, 80, 55, 60), e1, escenario, bajas, contadorBajas);

        Scene escena = new Scene(escenario, 300, 300);
        stage.setScene(escena);
        stage.show();
        vista1.requestFocus();
        jugadorControlador.iniciar(escena);

        AnimationTimer cicloJuego = new AnimationTimer() {
            private long ultimoSpawn;
            private final long inicioPartida = System.nanoTime();

            @Override
            public void handle(long ahora) {
                jugadorControlador.actualizar();
                vida.setText("Vida: " + e1.getVida() + "/" + e1.getVidaMax());
                long segundos = (ahora - inicioPartida) / 1_000_000_000L;
                tiempo.setText(String.format("Tiempo: %02d:%02d", segundos / 60, segundos % 60));
                if (!e1.estaVivo()) {
                    contadorBajas.setText("Bajas: " + bajas.get() + " - Jugador derrotado");
                    return;
                }
                for (EnemigoControlador enemigo : enemigos) {
                    enemigo.actualizar();
                }
                for (ExperienciaControlador experiencia : experiencias) {
                    experiencia.actualizar(e1);
                }
                experiencias.removeIf(experiencia -> {
                    if (experiencia.fueRecogida()) {
                        escenario.getChildren().remove(experiencia.getVista());
                        return true;
                    }
                    return false;
                });
                for (VidaControlador recuperacion : recuperaciones) {
                    recuperacion.actualizar(e1);
                }
                recuperaciones.removeIf(recuperacion -> {
                    if (recuperacion.fueRecogida()) {
                        escenario.getChildren().remove(recuperacion.getVista());
                        return true;
                    }
                    return false;
                });
                for (ProyectilControlador proyectil : proyectiles) {
                    proyectil.actualizar(enemigos);
                }
                proyectiles.removeIf(proyectil -> {
                    if (!proyectil.estaActivo()) {
                        escenario.getChildren().remove(proyectil.getVista());
                        return true;
                    }
                    return false;
                });
                if (ultimoSpawn == 0) {
                    ultimoSpawn = ahora;
                }
                long intervaloSpawn = Math.max(1_500_000_000L,
                        5_000_000_000L - (ahora - ultimoSpawn) / 3);
                if (ahora - ultimoSpawn >= intervaloSpawn
                        && enemigos.size() < MAX_ENEMIGOS_ACTIVOS) {
                    double angulo = siguienteTipoEnemigo++ * Math.PI / 3;
                    double x = 150 + Math.cos(angulo) * 155;
                    double y = 150 + Math.sin(angulo) * 155;
                    Rectangle2D recorte = recorteEnemigo(siguienteTipoEnemigo);
                    agregarEnemigo(x, y, recorte, e1, escenario, bajas, contadorBajas);
                    ultimoSpawn = ahora;
                }
                colisiones.resolverColisiones(entidades);
            }
        };
        cicloJuego.start();

    }
    */

    public static void main(String[] args) throws Exception {

        launch();

    }
}
