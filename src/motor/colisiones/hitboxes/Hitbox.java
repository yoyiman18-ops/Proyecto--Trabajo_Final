package motor.colisiones.hitboxes;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.util.concurrent.atomic.AtomicInteger;

import motor.util.VecDouble2D;

/**
* Una interfaz para hitboxes.
*/
public abstract class Hitbox {
    private final static AtomicInteger contadorId = new AtomicInteger(0);

    private final int id;
    private final Shape formaColision;
    private final Area area;
    private boolean activa;
    private final AffineTransform transformacion;

    public Hitbox(Shape formaColision) {
        this(formaColision, true);
    }

    public Hitbox(Shape formaColision, boolean activa) {
        this.id = contadorId.getAndIncrement();
        this.formaColision = formaColision;
        this.area = new Area(formaColision);
        this.activa = activa;
        this.transformacion = new AffineTransform();
    }

    public abstract boolean intersecta(Hitbox otra);
    public Shape getFormaColision() { return transformacion.createTransformedShape(this.formaColision); }
    public boolean estaActiva() { return this.activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
    public int getId() { return this.id; }
    public Area getArea() { return area.createTransformedArea(transformacion); }
    public void actualizarTransformacion(VecDouble2D posicion) {
        this.transformacion.setToTranslation(posicion.getX(),posicion.getY());
    }

    protected AffineTransform getTransformacion() { return this.transformacion; }

    protected boolean genericoConGenerico(Hitbox otra) {
        // fase general: comprueba que matematicamente puedan colisionar
        if (!(this.getFormaColision().getBounds2D().intersects(otra.getFormaColision().getBounds2D()))) {
            return false; 
        } else {
            Area interseccion = this.getArea();
            interseccion.intersect(otra.getArea());
            if (interseccion.isEmpty()) { return false; }
            else { return true; }
        }  
    }

}
