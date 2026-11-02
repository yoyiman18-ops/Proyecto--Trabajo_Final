package modelo.experiencia;

import java.util.ArrayList;

import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangularFactory;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.util.VecDouble2D;

public class ExperienciaGemaFactory extends ExperienciaFactory {

    public ExperienciaGemaFactory(
        MascaraColision categoriasColision,
        MascaraColision capasColision,
        VecDouble2D posicion,
        double cantidadExp
    ) {
        super(
            new HitboxRectangularFactory(16, 16, TipoHitbox.FANTASMA, categoriasColision, capasColision),
            posicion,
            cantidadExp,
            new ArrayList<>(),
            new ArrayList<>()
        );
    }

    public ExperienciaGemaFactory(
        VecDouble2D posicion,
        double cantidadExp
    ) {
        this(
            MascaraColision.of(CategoriaColision.EXP),
            MascaraColision.of(CategoriaColision.JUGADOR),
            posicion,
            cantidadExp
        );
    }

    @Override
    public Experiencia get() {
        return new ExperienciaGema(
            getPosicion(),
            getHitboxFactory().getHitbox(),
            getCantidadExp(),
            getEstrategiasExpiracion(),
            getInteraccionesColision()
        );
    }
}
