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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import motor.colisiones.Colisionable;
import motor.colisiones.SistemaColisiones;
import motor.colisiones.sistema.FaseEspecificaSimple;
import motor.colisiones.sistema.FaseGeneralSpatialHashGrid;
import motor.controlador.ControladorEntidad;
import motor.entrada.GestorEntradaTeclado;
import motor.modelo.Ticking;
import motor.recursos.GestorRecursos;
import motor.util.RelojDelta;
import motor.util.observer.NotificadorFuerte;

public class Motor implements Ticking {
    private final SistemaColisiones sistemaColisiones;
    private final GestorRecursos gestorRecursos;
    private final GestorEntradaTeclado gestorEntradaTeclado;
    private final RelojDelta relojDelta;
    
    private final List<ControladorEntidad> controladores;
    private final List<Colisionable> colisionables;
    private final Queue<ControladorEntidad> colaInstanciacion;
    private final Queue<ControladorEntidad> colaLimpieza;
    private Pane root;

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
        Pane root
    ) {
        this(
            sistemaColisiones,
            gestorRecursos,
            new GestorEntradaTeclado(),
            root
        );
    }

    public Motor(
        SistemaColisiones sistemaColisiones,
        GestorRecursos gestorRecursos,
        GestorEntradaTeclado gestorEntradaTeclado,
        Pane root
    ) {
        this.sistemaColisiones = Objects.requireNonNull(sistemaColisiones, "El sistema de colisiones no puede ser nulo");
        this.gestorRecursos = Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        this.gestorEntradaTeclado = Objects.requireNonNull(gestorEntradaTeclado, "El gestor de entrada de teclado no puede ser nulo");
        this.relojDelta = new RelojDelta(new NotificadorFuerte<>());
        this.root = Objects.requireNonNull(root, "El nodo raíz no puede ser nulo");
        this.controladores = new ArrayList<>();
        this.colisionables = new ArrayList<>();
        this.colaInstanciacion = new ArrayDeque<>();
        this.colaLimpieza = new ArrayDeque<>();
        
        // el observador más importante del sistema, propaga los ticks a todos los demás
        this.relojDelta.suscribirObservador(this::tick); 

    };
    

    /* BUCLE PRINCIPAL */
    @Override public void tick(double dt) {
        instanciarControladores();
        this.gestorEntradaTeclado.tick(dt);
        this.colisionables.clear();
        for (ControladorEntidad c : controladores) { 
            c.tick(dt);
            if (!c.activo()) { colaLimpieza.add(c); }
            else {colisionables.add(c.getEntidad()); }
        }
        limpiarControladores();
        this.sistemaColisiones.resolverColisiones(colisionables);
    }

    public GestorRecursos recursos() { return this.gestorRecursos; }
    public GestorEntradaTeclado teclado() { return this.gestorEntradaTeclado; }
    public RelojDelta reloj() { return this.relojDelta; }
    public SistemaColisiones colisiones() { return this.sistemaColisiones; }
    public List<Colisionable> colisionables() { return this.colisionables; }
    public Pane getRoot() { return this.root; }
    public void setRoot(Pane root) { this.root = root; }
    public void iniciar() { this.relojDelta.iniciar(); Platform.runLater(root::requestFocus);}
    public void detener() { this.relojDelta.detener(); }

    public void encolarControlador(ControladorEntidad c) { colaInstanciacion.add(c); }

    private void instanciarControladores() {
        ControladorEntidad c;
        while ((c = colaInstanciacion.poll()) != null) { controladores.add(c); getRoot().getChildren().add(c.getVista()); }
    }

    private void limpiarControladores() {
        ControladorEntidad c;
        while ((c = colaLimpieza.poll()) != null) { controladores.remove(c); getRoot().getChildren().remove(c.getVista()); }
    }

}


