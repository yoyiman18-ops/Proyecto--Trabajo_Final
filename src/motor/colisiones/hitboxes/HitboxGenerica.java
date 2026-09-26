package motor.colisiones.hitboxes;
import java.awt.Shape;

/**
 * 
 * Hitbox para cualquier Shape de java awt, optimizada solo contra rectángulos.
*/
public class HitboxGenerica extends Hitbox {

    public HitboxGenerica(Shape formaColision, TipoHitbox tipo, MascaraColision categoriasColision, MascaraColision capasColision) {
        super(formaColision, tipo, true, categoriasColision, capasColision);
    }

    public HitboxGenerica(Shape formaColision, TipoHitbox tipo, boolean activa, MascaraColision categoriasColision, MascaraColision capasColision) {
        super(formaColision, tipo, activa, categoriasColision, capasColision);
    }

    @Override 
    public boolean intersecta(Hitbox otra) {
        return switch (otra) {
            case HitboxRectangular rectangular -> rectangular.intersecta(this);
            default -> genericoConGenerico(otra);
        };
    }
}
