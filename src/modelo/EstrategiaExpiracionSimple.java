package modelo;

import motor.modelo.EstrategiaExpiracion;
import motor.modelo.ModeloEntidad;

public class EstrategiaExpiracionSimple implements EstrategiaExpiracion {

    @Override
    public int ejecutar(ModeloEntidad entidad) {
        System.out.println(entidad.getHitbox().getId() + " ha ejecutado una estrategia al expirar");
        return 0;
    }
}
