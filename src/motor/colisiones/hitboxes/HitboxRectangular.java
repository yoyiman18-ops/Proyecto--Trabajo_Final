package motor.colisiones.hitboxes;

import java.awt.Shape;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
/**
 * 
 * Hitbox optimizada para una forma rectangular.
 */
public class HitboxRectangular implements Hitbox {
    private final Rectangle2D rectanguloColision;
    private boolean activa;
    private final Area area;

    public HitboxRectangular(Rectangle2D rectanguloColision) {
        this(rectanguloColision, true);
    }

    public HitboxRectangular(Rectangle2D rectanguloColision, boolean activa) {
        this.rectanguloColision = rectanguloColision;
        this.activa = activa;
        this.area = new Area(rectanguloColision);
    }

    @Override public Area getArea() { return this.area; }
    @Override public Shape getFormaColision() { return this.rectanguloColision; }
    @Override public boolean estaActiva() { return this.activa; }
    @Override public void setActiva(boolean activa) { this.activa = activa; }
    @Override 
    public boolean intersecta(HitboxRectangular otra) {
        return this.rectanguloColision.intersects(otra.getRectanguloColision());
    } 
    @Override 
    public boolean intersecta(Hitbox otra) {
        if (!(otra.getFormaColision().getBounds2D().intersects(this.rectanguloColision))) { return false; }
        else {
            Area interseccion = otra.getArea();
            interseccion.intersect(this.area);
            if (interseccion.isEmpty()) { return false; }
            else { return true; }
        }
    }
    
    public Rectangle2D getRectanguloColision() { return this.rectanguloColision; }

}
