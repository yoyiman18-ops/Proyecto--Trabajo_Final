package modelo;

import motor.modelo.ModeloEntidad;

public class EstrategiaExpiracionSimple implements EstrategiaExpiracion {

    @Override
    public void ejecutar(ModeloEntidad entidad) {
        System.out.println(entidad.getHitbox().getId() + " ha ejecutado una estrategia al expirar");
    }
}
