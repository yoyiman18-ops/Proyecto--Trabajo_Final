package modelo.experiencia;

import java.util.List;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.Interaccion;
import motor.util.VecDouble2D;

public class ExperienciaGema extends Experiencia {

    public ExperienciaGema(
        VecDouble2D posicion,
        Hitbox hitbox,
        double cantidadExp,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(
            50,
            "Gema Experiencia",
            hitbox,
            posicion,
            new VecDouble2D(0, 0),
            0.0,
            0.0,
            0.0,
            estrategiasExpiracion,
            interaccionesColision,
            cantidadExp,
            "idle",
            "expirar"
        );
    }
}
