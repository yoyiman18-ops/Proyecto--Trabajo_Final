package motor.colisiones.hitboxes;

import java.awt.Shape;
import java.awt.geom.Area;

/**
 * 
 * Hitbox para cualquier Shape de java awt.
*/
public class HitboxGenerica implements Hitbox {
    private final Shape formaColision;
    private boolean activa;
    private final Area area;

    public HitboxGenerica(Shape formaColision) {
        this(formaColision, true);
    }

    public HitboxGenerica(Shape formaColision, boolean activa) {
        this.formaColision = formaColision;
        this.activa = activa;
        this.area = new Area(formaColision);
    }

    @Override public Area getArea() { return this.area; }
    @Override public Shape getFormaColision() { return this.formaColision; }
    @Override public boolean estaActiva() { return this.activa; }
    @Override public void setActiva(boolean activa) { this.activa = activa; }

    @Override 
    public boolean intersecta(Hitbox otra) {
        // fase general: comprueba que matematicamente puedan colisionar
        if (!(this.formaColision.getBounds2D().intersects(otra.getFormaColision().getBounds2D()))) {
            return false; 
        } else {
            Area interseccion = this.area;
            interseccion.intersect(otra.getArea());
            if (interseccion.isEmpty()) { return false; }
            else { return true; }
        }
    }

    @Override 
    public boolean intersecta(HitboxRectangular otra) { return otra.intersecta(this); }
}
