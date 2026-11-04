package modelo.proyectil;

import java.util.ArrayList;

import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangularFactory;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.modelo.EstrategiaMovimiento;
import motor.util.VecDouble2D;

public class ProyectilRojoFactory extends ProyectilFactory {

    @Override
    public ProyectilRojo get(
        VecDouble2D posicion,
        EstrategiaMovimiento estrategiaMovimiento,
        double dañoAdicional
    ) {
        return new ProyectilRojo(
            posicion != null ? posicion.clone() : new VecDouble2D(),
            new HitboxRectangularFactory().get(
                24, 24, 16, 16,
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.ALIADO),
                MascaraColision.of(CategoriaColision.ENEMIGO, CategoriaColision.MURO),
                true
            ),
            1000.0,
            0.0,
            320.0,
            estrategiaMovimiento,
            dañoAdicional,
            2.0,
            0,
            new ArrayList<>(),
            new ArrayList<>()
        );
    }
}
