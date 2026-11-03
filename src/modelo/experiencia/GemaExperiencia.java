package modelo.experiencia;

import java.util.List;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

public class GemaExperiencia extends ExperienciaGema {

    public GemaExperiencia(
        VecDouble2D posicion,
        Hitbox hitbox,
        double cantidadExp,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(posicion, hitbox, cantidadExp, estrategiasExpiracion, interaccionesColision);
    }
}
