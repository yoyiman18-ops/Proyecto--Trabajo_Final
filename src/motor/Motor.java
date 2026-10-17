/**
 * 
 *      NO VOLVER A MODIFICAR CON LÓGICA ACOPLADA AL JUEGO
 * 
 * 
 *      LA LÓGICA DEL JUEGO NO VA EN ESTA CLASE
 * 
 * 
 * 
 * 
 * 
 * 
 *
 *
 *
 *
 *
 *
 *
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */


package motor;

import java.util.ArrayList;
import java.util.Objects;

import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import motor.colisiones.Colisionable;
import motor.colisiones.SistemaColisiones;
import motor.colisiones.sistema.FaseEspecificaSimple;
import motor.colisiones.sistema.FaseGeneralSpatialHashGrid;
import motor.entrada.GestorEntradaTeclado;
import motor.recursos.GestorRecursos;
import motor.util.RelojDelta;
import motor.util.observer.NotificadorFuerte;

public class Motor {
    private final SistemaColisiones sistemaColisiones;
    private final GestorRecursos gestorRecursos;
    private final GestorEntradaTeclado gestorEntradaTeclado;
    private final RelojDelta relojDelta;
    private final Parent root;
    private final ArrayList<Colisionable> colisionables;

    /**
     * Crear motor default.
     */
    public Motor() {
        this(
            new SistemaColisiones(new FaseGeneralSpatialHashGrid(100), new FaseEspecificaSimple()),
            new GestorRecursos(),
            new Pane()
        );
    }

    public Motor(
        SistemaColisiones sistemaColisiones,
        GestorRecursos gestorRecursos,
        Parent root
    ) {
        this(
            sistemaColisiones,
            gestorRecursos,
            new GestorEntradaTeclado(),
            new RelojDelta(new NotificadorFuerte<>()),
            root
        );
    }

    public Motor(
        SistemaColisiones sistemaColisiones,
        GestorRecursos gestorRecursos,
        GestorEntradaTeclado gestorEntradaTeclado,
        RelojDelta relojDelta,
        Parent root
    ) {
        this.sistemaColisiones = Objects.requireNonNull(sistemaColisiones, "El sistema de colisiones no puede ser nulo");
        this.gestorRecursos = Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        this.gestorEntradaTeclado = Objects.requireNonNull(gestorEntradaTeclado, "El gestor de entrada de teclado no puede ser nulo");
        this.relojDelta = Objects.requireNonNull(relojDelta, "El reloj delta no puede ser nulo");
        this.root = Objects.requireNonNull(root, "El nodo raíz no puede ser nulo");
        this.colisionables = new ArrayList<>();
        
        this.relojDelta.suscribirObservador(e -> {
            this.gestorEntradaTeclado.tick();
            this.sistemaColisiones.resolverColisiones(colisionables);
        });
    }

    public GestorRecursos recursos() { return this.gestorRecursos; }
    public GestorEntradaTeclado teclado() { return this.gestorEntradaTeclado; }
    public RelojDelta reloj() { return this.relojDelta; }
    public SistemaColisiones colisiones() { return this.sistemaColisiones; }
    public ArrayList<Colisionable> colisionables() { return this.colisionables; }
    public Parent root() { return this.root; }
    public void iniciar() { this.relojDelta.iniciar(); Platform.runLater(root::requestFocus);}
    public void detener() { this.relojDelta.detener(); }
}


