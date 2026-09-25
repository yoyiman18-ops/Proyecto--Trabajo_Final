package motor;

import motor.colisiones.SistemaColisiones;
import motor.recursos.GestorRecursos;
import motor.util.RelojDelta;
import motor.util.observer.Observador;

public class Motor {
    private final SistemaColisiones sistemaColisiones;
    private final GestorRecursos gestorRecursos;
    private final RelojDelta relojDelta;
    private final ArrayList<Entidad> entidades;

    public Motor(SistemaColisiones sistemaColisiones, GestorRecursos gestorRecursos) {
        this.sistemaColisiones = sistemaColisiones;
        this.gestorRecursos = gestorRecursos;
        this.relojDelta = new RelojDelta();
        
        relojDelta.suscribirObservador(e -> {
            sistemaColisiones.resolverColisiones(entidades);
        });
    }


    public GestorRecursos recursos() { return this.gestorRecursos; }
}
