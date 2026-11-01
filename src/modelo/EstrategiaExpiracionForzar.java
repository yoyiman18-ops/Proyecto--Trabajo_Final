package modelo;

import motor.modelo.EstrategiaExpiracion;
import motor.modelo.ModeloEntidad;

public class EstrategiaExpiracionForzar implements EstrategiaExpiracion {
    @Override public int ejecutar(ModeloEntidad entidad) { return 1; }
    
}
