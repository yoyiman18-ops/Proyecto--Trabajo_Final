package motor.colisiones.hitboxes;
import java.awt.Shape;

/**
 * 
 * Hitbox para cualquier Shape de java awt, optimizada solo contra rectángulos.
*/
public class HitboxGenerica extends Hitbox {

    public HitboxGenerica(Shape formaColision, TipoHitbox tipo, MascaraColision categoriasColision, MascaraColision capasColision) {
        super(formaColision, tipo, categoriasColision, capasColision, true);
    }

    public HitboxGenerica(Shape formaColision, TipoHitbox tipo, MascaraColision categoriasColision, MascaraColision capasColision, boolean activa) {
        super(formaColision, tipo, categoriasColision, capasColision, activa);
    }

    @Override 
    public boolean intersecta(Hitbox otra) {
        return switch (otra) {
            case HitboxRectangular rectangular -> rectangular.intersecta(this);
            default -> genericoConGenerico(otra);
        };
    }
}
