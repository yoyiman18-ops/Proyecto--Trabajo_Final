package motor.colisiones.hitboxes;
import java.awt.Shape;
import java.awt.geom.Area;

/**
 * 
 * Hitbox para cualquier Shape de java awt, optimizada solo contra rectángulos.
*/
public class HitboxGenerica extends Hitbox {

    public HitboxGenerica(Shape formaColision, TipoHitbox tipo) {
        super(formaColision, tipo, true);
    }

    public HitboxGenerica(Shape formaColision, TipoHitbox tipo, boolean activa) {
        super(formaColision, tipo, activa);
    }

    @Override 
    public boolean intersecta(Hitbox otra) {
        return switch (otra) {
            case HitboxRectangular rectangular -> rectangular.intersecta(this);
            default -> genericoConGenerico(otra);
        };
    }
}
