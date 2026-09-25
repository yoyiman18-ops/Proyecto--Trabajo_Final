package motor.colisiones.hitboxes;

import java.awt.Shape;
import java.awt.geom.Area;

/**
* Una interfaz para hitboxes.
*/
public abstract class Hitbox {
    private final Area area;
    private final Shape formaColision;
    private boolean activa;

    public Hitbox(Shape formaColision) {
        this(formaColision, true);
    }

    public Hitbox(Shape formaColision, boolean activa) {
        this.formaColision = formaColision;
        this.area = new Area(formaColision);
        this.activa = activa;
    }

    public abstract boolean intersecta(Hitbox otra);
    public Shape getFormaColision() { return this.formaColision; }
    public Area getArea() { return this.area; }
    public boolean estaActiva() { return this.activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    protected boolean genericoConGenerico(Hitbox otra) {
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
}
