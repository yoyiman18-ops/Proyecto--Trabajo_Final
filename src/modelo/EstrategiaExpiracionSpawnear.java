package modelo;

import java.util.List;
import motor.modelo.ModeloEntidad;

public class EstrategiaExpiracionSpawnear implements EstrategiaExpiracion {
    private final List<ModeloEntidad> spawneados;

    public EstrategiaExpiracionSpawnear(List<ModeloEntidad> spawneados) {
        this.spawneados = spawneados;
    }

    @Override 
    public void ejecutar(ModeloEntidad entidad) {
        for (ModeloEntidad e : spawneados) { e.setPosicion(entidad.getPosicion()); entidad.getNotificadorInstanciacion().notificar(e); }
    }
}
