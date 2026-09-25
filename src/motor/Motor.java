package motor;

import motor.colisiones.SistemaColisiones;
import motor.recursos.GestorRecursos;

public class Motor {
    private final SistemaColisiones sistemaColisiones;
    private final GestorRecursos gestorRecursos;

    public Motor(SistemaColisiones sistemaColisiones, GestorRecursos gestorRecursos) {
        this.sistemaColisiones = sistemaColisiones;
        this.gestorRecursos = gestorRecursos;
    }
}
