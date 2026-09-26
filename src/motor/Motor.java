package motor;

import java.util.ArrayList;

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
        this.sistemaColisiones = sistemaColisiones;
        this.gestorRecursos = gestorRecursos;
        this.relojDelta = new RelojDelta();
        this.root = root;
        this.gestorEntradaTeclado = new GestorEntradaTeclado(root);
        this.colisionables = new ArrayList<>();
        
        relojDelta.suscribirObservador(e -> {
            this.gestorEntradaTeclado.tick();
            this.sistemaColisiones.resolverColisiones(colisionables);
        });
    }

    public void instanciarEntidad(Entidad.Builder entidadBuilder) {
        Entidad entidad = entidadBuilder.build(gestorRecursos);
        colisionables.add(entidad);
    }

    public GestorRecursos recursos() { return this.gestorRecursos; }
    public GestorEntradaTeclado teclado() { return this.gestorEntradaTeclado; }
    public Parent root() { return this.root; }
    public void iniciar() { this.relojDelta.iniciar(); Platform.runLater(root::requestFocus);}
    public void detener() { this.relojDelta.detener(); }
}


