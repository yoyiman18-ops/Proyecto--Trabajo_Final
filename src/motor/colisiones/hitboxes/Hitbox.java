package motor.colisiones.hitboxes;
import java.awt.Shape;
import java.awt.geom.Area;
import java.util.concurrent.atomic.AtomicInteger;

/**
* Una interfaz para hitboxes.
*/
public abstract class Hitbox {
    private final static AtomicInteger contadorId = new AtomicInteger(0);

    private final int id;
    private final Area area;
    private final Shape formaColision;
    private boolean activa;

    public Hitbox(Shape formaColision) {
        this(formaColision, true);
    }

    public Hitbox(Shape formaColision, boolean activa) {
        this.id = contadorId.getAndIncrement();
        this.formaColision = formaColision;
        this.area = new Area(formaColision);
        this.activa = activa;
    }

    public abstract boolean intersecta(Hitbox otra);
    public Shape getFormaColision() { return this.formaColision; }
    public Area getArea() { return this.area; }
    public boolean estaActiva() { return this.activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
    public int getId() { return this.id; }

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
