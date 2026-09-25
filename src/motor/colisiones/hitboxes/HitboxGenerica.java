package motor.colisiones.hitboxes;

import java.awt.Shape;
import java.awt.geom.Area;

/**
 * 
 * Hitbox para cualquier Shape de java awt, optimizada solo contra rectángulos.
*/
public class HitboxGenerica extends Hitbox {

    public HitboxGenerica(Shape formaColision) {
        super(formaColision, true);
    }

    public HitboxGenerica(Shape formaColision, boolean activa) {
        super(formaColision, activa);
    }

    @Override 
    public boolean intersecta(Hitbox otra) {
        return switch (otra) {
            case HitboxRectangular rectangular -> genericoConRectangular(rectangular);
            default -> genericoConGenerico(otra);
        };
    }

    private boolean genericoConRectangular(HitboxRectangular otra) {
        if (!(getFormaColision().getBounds2D().intersects(otra.getRectanguloColision()))) { return false; }
        else {
            Area interseccion = getArea();
            interseccion.intersect(new Area(otra.getRectanguloColision()));
            if (interseccion.isEmpty()) { return false; }
            else { return true; }
        }
    }
}
