package modelo.proyectil;

import motor.colisiones.hitboxes.HitboxRectangular;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;

import java.awt.geom.Rectangle2D;
import motor.util.VecDouble2D;

public class ProyectilRojoFactory implements ProyectilFactory {

    @Override public Proyectil crearProyectil() {
        return new Proyectil(
            4,
            "Proyectil Rojo",
            new HitboxRectangular(
                new Rectangle2D.Double(60,60,0,0),
                TipoHitbox.FANTASMA,
                null
                , null),
            null,
            null,
            1,
            1,
            1,
            null
        );
    }
}
