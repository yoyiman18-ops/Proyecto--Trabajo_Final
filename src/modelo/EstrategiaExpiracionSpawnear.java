package modelo;

import java.util.List;

import motor.modelo.EstrategiaExpiracion;
import motor.modelo.ModeloEntidad;

public class EstrategiaExpiracionSpawnear implements EstrategiaExpiracion {
    private final List<ModeloEntidad> spawneados;

    public EstrategiaExpiracionSpawnear(List<ModeloEntidad> spawneados) {
        this.spawneados = spawneados;
    }

    @Override 
    public int ejecutar(ModeloEntidad entidad) {
        for (ModeloEntidad e : spawneados) {
            e.setPosicion(entidad.getPosicion() != null ? entidad.getPosicion().clone() : new motor.util.VecDouble2D());
            entidad.getNotificadorInstanciacion().notificar(e);
        }
        return 0;
    }
}
