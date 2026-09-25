package motor.colisiones.hitboxes;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
/**
 * 
 * Hitbox optimizada para una forma rectangular contra formas genéricas y contra rectángulos.
 */
public class HitboxRectangular extends Hitbox {

    public HitboxRectangular(Rectangle2D rectanguloColision, TipoHitbox tipo) {
        super(rectanguloColision, tipo, true);
    }

    public HitboxRectangular(Rectangle2D rectanguloColision, TipoHitbox tipo, boolean activa) {
        super(rectanguloColision, tipo, activa);
    }

    @Override 
    public boolean intersecta(Hitbox otra) {
        return switch (otra) {
            case HitboxRectangular rectangular -> rectangularConRectangular(rectangular);
            default -> rectangularConGenerico(otra);
        };
    } 

    private boolean rectangularConRectangular(HitboxRectangular otra) {
        return this.getBounds().intersects(otra.getBounds());
    }

    private boolean rectangularConGenerico(Hitbox otra) {
        if (!otra.getBounds().intersects(this.getBounds())) { return false; }
        else {
            Area interseccion = otra.getArea();
            interseccion.intersect(getArea());
            if (interseccion.isEmpty()) { return false; }
            else { return true; }
        }
    }

}
