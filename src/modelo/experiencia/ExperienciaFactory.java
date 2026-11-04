package modelo.experiencia;

import motor.util.VecDouble2D;

public abstract class ExperienciaFactory {

    public abstract Experiencia get(VecDouble2D posicion, double cantidadExp);
}
