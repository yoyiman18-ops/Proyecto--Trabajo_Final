package motor;

import java.util.concurrent.CopyOnWriteArrayList;

import controlador.EnemigoControlador;
import controlador.JugadorControlador;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import modelo.Personaje;
import motor.colisiones.Colisionable;
import motor.colisiones.SistemaColisiones;
import motor.colisiones.sistema.FaseEspecificaSimple;
import motor.colisiones.sistema.FaseGeneralSpatialHashGrid;
import motor.entrada.GestorEntradaTeclado;
import motor.mvc.Controlador;
import motor.recursos.GestorRecursos;
import motor.util.RelojDelta;
import motor.util.observer.NotificadorFuerte;
import motor.util.observer.Observador;

public class Motor {
    private final SistemaColisiones sistemaColisiones;
    private final GestorRecursos gestorRecursos;
    private final GestorEntradaTeclado gestorEntradaTeclado;
    private final RelojDelta relojDelta;
    private final Pane root;
    private final CopyOnWriteArrayList<Controlador> controladores;
    private final CopyOnWriteArrayList<Colisionable> colisionables;

    /**
     * Crear motor default.
     */
    public Motor() {
        this(new Pane());
    }

    public Motor(Pane root) {
        this(
            new SistemaColisiones(new FaseGeneralSpatialHashGrid(100), new FaseEspecificaSimple()),
            new GestorRecursos(),
            root
        );
    }

    public Motor(
        SistemaColisiones sistemaColisiones,
        GestorRecursos gestorRecursos,
        Pane root
        ) {
        this.sistemaColisiones = sistemaColisiones;
        this.gestorRecursos = gestorRecursos;
        this.relojDelta = new RelojDelta(new NotificadorFuerte<>());
        this.root = root;
        this.gestorEntradaTeclado = new GestorEntradaTeclado();
        this.colisionables = new CopyOnWriteArrayList<>();
        this.controladores = new CopyOnWriteArrayList<>();
        
        relojDelta.suscribirObservador(e -> {
            this.gestorEntradaTeclado.tick();
            this.sistemaColisiones.resolverColisiones(colisionables);
            for (Controlador c : controladores) { c.tick(e); }
        });
    }

    public void instanciarJugador(String nombre) {
        instanciarJugador(nombre, personaje -> {});
    }

    public void instanciarJugador(String nombre, Observador<Personaje> observadorVida) {
        JugadorControlador c = new JugadorControlador(nombre, gestorRecursos, observadorVida);
        EnemigoControlador enemigo = new EnemigoControlador(c.getPersonaje());
        this.controladores.add(c);
        this.controladores.add(enemigo);
        this.gestorEntradaTeclado.suscribir(c.getObservadorAcciones());
        Platform.runLater(() -> root.getChildren().addAll(c.getVista(), enemigo.getVista()));
    }

    public GestorRecursos recursos() { return this.gestorRecursos; }
    public GestorEntradaTeclado teclado() { return this.gestorEntradaTeclado; }
    public Pane root() { return this.root; }
    public void iniciar() { this.relojDelta.iniciar(); }
    public void detener() { this.relojDelta.detener(); }
}


