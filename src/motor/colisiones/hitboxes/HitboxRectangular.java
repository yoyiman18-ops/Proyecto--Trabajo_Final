package motor.colisiones.hitboxes;

import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
/**
 * 
 * Hitbox optimizada para una forma rectangular contra formas genéricas y contra rectángulos.
 */
public class HitboxRectangular extends Hitbox {

    public HitboxRectangular(Rectangle2D rectanguloColision) {
        super(rectanguloColision, true);
    }

    public HitboxRectangular(Rectangle2D rectanguloColision, boolean activa) {
        super(rectanguloColision, activa);
    }

    @Override 
    public boolean intersecta(Hitbox otra) {
        return switch (otra) {
            case HitboxRectangular rectangular -> rectangularConRectangular(rectangular);
            default -> rectangularConGenerico(otra);
        };
    } 
    
    public Rectangle2D getRectanguloColision() { 
        return (Rectangle2D) getTransformacion().createTransformedShape(getFormaColision()); 
    }

    private boolean rectangularConRectangular(HitboxRectangular otra) {
        return this.getRectanguloColision().intersects(otra.getRectanguloColision());
    }

    private boolean rectangularConGenerico(Hitbox otra) {
        if (!(otra.getFormaColision().getBounds2D().intersects(this.getRectanguloColision()))) { return false; }
        else {
            Area interseccion = otra.getArea();
            interseccion.intersect(getArea());
            if (interseccion.isEmpty()) { return false; }
            else { return true; }
        }
    }

}
