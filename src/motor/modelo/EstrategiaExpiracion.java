package modelo;

import motor.modelo.ModeloEntidad;

public interface EstrategiaExpiracion {
    void ejecutar(ModeloEntidad entidad);
}
