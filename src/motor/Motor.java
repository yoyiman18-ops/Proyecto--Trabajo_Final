package motor;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

import controlador.Controlador;
import controlador.JugadorControlador;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import motor.colisiones.Colisionable;
import motor.colisiones.SistemaColisiones;
import motor.colisiones.sistema.FaseEspecificaSimple;
import motor.colisiones.sistema.FaseGeneralSpatialHashGrid;
import motor.entrada.GestorEntradaTeclado;
import motor.modelo.Entidad;
import motor.recursos.GestorRecursos;
import motor.util.RelojDelta;

public class Motor {
    private final SistemaColisiones sistemaColisiones;
    private final GestorRecursos gestorRecursos;
    private final GestorEntradaTeclado gestorEntradaTeclado;
    private final RelojDelta relojDelta;
    private final Parent root;
    private final CopyOnWriteArrayList<Controlador> controladores;
    private final CopyOnWriteArrayList<Colisionable> colisionables;

    /**
     * Crear motor default.
     */
    public Motor() {
        this(new Pane());
    }

    public Motor(Parent root) {
        this(
            new SistemaColisiones(new FaseGeneralSpatialHashGrid(100), new FaseEspecificaSimple()),
            new GestorRecursos(),
            root
        );
    }

    public Motor(
        SistemaColisiones sistemaColisiones,
        GestorRecursos gestorRecursos,
        Parent root
        ) {
        this.sistemaColisiones = sistemaColisiones;
        this.gestorRecursos = gestorRecursos;
        this.relojDelta = new RelojDelta();
        this.root = root;
        this.gestorEntradaTeclado = new GestorEntradaTeclado(root);
        this.colisionables = new CopyOnWriteArrayList<>();
        this.controladores = new CopyOnWriteArrayList<>();
        
        relojDelta.suscribirObservador(e -> {
            this.gestorEntradaTeclado.tick();
            this.sistemaColisiones.resolverColisiones(colisionables);
            for (Controlador c : controladores) { c.tick(e); }
        });
    }

    public void instanciarJugador(String nombre) {
        JugadorControlador c = new JugadorControlador(nombre, gestorRecursos);
        this.gestorEntradaTeclado.suscribir(c.getObservadorAcciones());
        this.controladores.add(c);   
    }




    public GestorRecursos recursos() { return this.gestorRecursos; }
    public GestorEntradaTeclado teclado() { return this.gestorEntradaTeclado; }
    public Parent root() { return this.root; }
    public void iniciar() { this.relojDelta.iniciar(); Platform.runLater(root::requestFocus);}
    public void detener() { this.relojDelta.detener(); }
}


