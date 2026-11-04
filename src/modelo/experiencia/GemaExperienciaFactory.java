package modelo.experiencia;

import java.util.ArrayList;

import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangularFactory;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.util.VecDouble2D;

public class GemaExperienciaFactory extends ExperienciaFactory {

    @Override
    public Experiencia get(VecDouble2D posicion, double cantidadExp) {
        return new GemaExperiencia(
            posicion != null ? posicion.clone() : new VecDouble2D(),
            new HitboxRectangularFactory().get(
                24, 24, 16, 16,
                TipoHitbox.FANTASMA,
                MascaraColision.of(CategoriaColision.EXP),
                MascaraColision.of(CategoriaColision.JUGADOR),
                true
            ),
            0.0,
            0.0,
            0.0,
            cantidadExp,
            null,
            new ArrayList<>(),
            new ArrayList<>()
        );
    }
}
