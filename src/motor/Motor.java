package motor;

import java.util.ArrayList;
import motor.colisiones.Colisionable;
import motor.colisiones.SistemaColisiones;
import motor.modelo.Entidad;
import motor.recursos.GestorRecursos;
import motor.util.RelojDelta;

public class Motor {
    private final SistemaColisiones sistemaColisiones;
    private final GestorRecursos gestorRecursos;
    private final RelojDelta relojDelta;
    private final ArrayList<Colisionable> colisionables;

    public Motor(SistemaColisiones sistemaColisiones, GestorRecursos gestorRecursos) {
        this.sistemaColisiones = sistemaColisiones;
        this.gestorRecursos = gestorRecursos;
        this.colisionables = new ArrayList<>();
        this.relojDelta = new RelojDelta();
        
        relojDelta.suscribirObservador(e -> {
            this.sistemaColisiones.resolverColisiones(colisionables);
        });
    }

    public void instanciarEntidad(Entidad.Builder entidadBuilder) {
        Entidad entidad = entidadBuilder.build(gestorRecursos);
        colisionables.add(entidad);
    }

    public GestorRecursos recursos() { return this.gestorRecursos; }
}
