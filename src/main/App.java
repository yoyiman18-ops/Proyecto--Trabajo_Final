package main;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;
import javafx.geometry.Rectangle2D;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import modelo.*;
import motor.cache.CacheImagenes;
import vista.SpriteVista;
import motor.colisiones.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import controlador.SpriteControlador;
import controlador.JugadorControlador;
import controlador.EnemigoControlador;
import controlador.ProyectilControlador;

public class App extends Application {

    CacheImagenes cache = new CacheImagenes();
    ArrayList<Entidad> entidades = new ArrayList<>();
    List<EnemigoControlador> enemigos = new ArrayList<>();
    List<ProyectilControlador> proyectiles = new ArrayList<>();
    ControladorColisiones colisiones = new ControladorColisiones(64);
    private int siguienteTipoEnemigo;

    @Override
    public void start(Stage stage) {           
        EntidadViva e1 = new EntidadViva.Builder()
                        .nombre("Brotato")
                        .posicion(126,126)
                        .direccion(0.45, 0.55)
                        .velocidadMax(1)
                        .aceleracion(10)
                        .hitbox(10, 10, 0, 0,true)
                        .build();
        entidades.add(e1);

        SpriteVista vista1 = new SpriteVista(
                cache, "Brotato", new Rectangle2D(0, 0, 300, 300), 48, 48
        );
        new SpriteControlador(e1, vista1);
        Pane escenario = new Pane();
        escenario.getChildren().add(vista1);
        AtomicInteger bajas = new AtomicInteger();
        Label contadorBajas = new Label("Bajas: 0");
        contadorBajas.setLayoutX(10);
        contadorBajas.setLayoutY(10);
        escenario.getChildren().add(contadorBajas);
        JugadorControlador jugadorControlador = new JugadorControlador(
                e1, vista1, enemigos, proyectiles, escenario, cache
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

            @Override
            public void handle(long ahora) {
                jugadorControlador.actualizar();
                if (!e1.estaVivo()) {
                    contadorBajas.setText("Bajas: " + bajas.get() + " - Jugador derrotado");
                    return;
                }
                for (EnemigoControlador enemigo : enemigos) {
                    enemigo.actualizar();
                }
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
                if (ahora - ultimoSpawn >= intervaloSpawn) {
                    double angulo = siguienteTipoEnemigo++ * Math.PI / 3;
                    double x = 150 + Math.cos(angulo) * 125;
                    double y = 150 + Math.sin(angulo) * 125;
                    Rectangle2D recorte = recorteEnemigo(siguienteTipoEnemigo);
                    agregarEnemigo(x, y, recorte, e1, escenario, bajas, contadorBajas);
                    ultimoSpawn = ahora;
                }
                colisiones.resolverColisiones(entidades);
            }
        };
        cicloJuego.start();

    }

    private void agregarEnemigo(double x, double y, Rectangle2D recorte,
                                EntidadViva jugador, Pane escenario,
                                AtomicInteger bajas, Label contadorBajas) {
        EntidadViva modelo = new EntidadViva.Builder()
                .nombre("images")
                .posicion(x, y)
                .velocidadMax(0.5)
                .aceleracion(0.02)
                .vidaMax(10)
                .hitbox(11, 10, 0, 0, true)
                .build();
        SpriteVista vista = new SpriteVista(cache, "images", recorte, 42, 42);
        escenario.getChildren().add(vista);
        entidades.add(modelo);
        enemigos.add(new EnemigoControlador(modelo, jugador, vista, () -> {
            int total = bajas.incrementAndGet();
            contadorBajas.setText("Bajas: " + total);
        }));
    }

    private Rectangle2D recorteEnemigo(int numero) {
        return switch (numero % 3) {
            case 1 -> new Rectangle2D(18, 18, 45, 45);
            case 2 -> new Rectangle2D(83, 82, 55, 55);
            default -> new Rectangle2D(145, 80, 55, 60);
        };
    }

    public static void main(String[] args) throws Exception {

        launch();

    }
}
