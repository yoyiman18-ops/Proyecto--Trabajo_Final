package modelo.proyectil;

import motor.modelo.EstrategiaMovimiento;
import motor.util.VecDouble2D;

public abstract class ProyectilFactory {

    public abstract Proyectil get(
        VecDouble2D posicion,
        EstrategiaMovimiento estrategiaMovimiento,
        double dañoAdicional
    );
}
