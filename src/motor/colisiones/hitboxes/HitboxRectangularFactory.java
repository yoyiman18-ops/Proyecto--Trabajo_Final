package motor.colisiones.hitboxes;

import java.awt.geom.Rectangle2D;

public class HitboxRectangularFactory extends HitboxFactory {

    @Override
    public HitboxRectangular get(
        double offsetX,
        double offsetY,
        double ancho,
        double alto,
        TipoHitbox tipo,
        MascaraColision categoriasColision,
        MascaraColision capasColision,
        boolean activa
    ) {
        return new HitboxRectangular(
            new Rectangle2D.Double(offsetX, offsetY, ancho, alto),
            tipo,
            categoriasColision,
            capasColision,
            activa
        );
    }
}
