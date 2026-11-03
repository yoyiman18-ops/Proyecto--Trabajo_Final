package modelo.experiencia;

import motor.colisiones.hitboxes.MascaraColision;
import motor.util.VecDouble2D;

public class GemaExperienciaFactory extends ExperienciaGemaFactory {

    public GemaExperienciaFactory(
        MascaraColision categoriasColision,
        MascaraColision capasColision,
        VecDouble2D posicion,
        double cantidadExp
    ) {
        super(categoriasColision, capasColision, posicion, cantidadExp);
    }

    public GemaExperienciaFactory(VecDouble2D posicion, double cantidadExp) {
        super(posicion, cantidadExp);
    }
}
