package motor.colisiones.hitboxes;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.atomic.AtomicInteger;

import motor.util.VecDouble2D;

/**
* Una interfaz para hitboxes.
*/
public abstract class Hitbox {
    private final static AtomicInteger contadorId = new AtomicInteger(0);

    private final int id;
    private final Shape formaColision;
    private final Rectangle2D bounds;
    private final Area area;

    private final AffineTransform transformacion;
    private boolean activa;
    private Shape formaColisionTransformada;
    private Rectangle2D boundsTransformados;
    private Area areaTransformada;

    public Hitbox(Shape formaColision) {
        this(formaColision, true);
    }

    public Hitbox(Shape formaColision, boolean activa) {
        this.id = contadorId.getAndIncrement();
        this.formaColision = formaColision;
        this.formaColisionTransformada = formaColision;
        this.bounds = formaColision.getBounds2D();
        this.boundsTransformados = this.bounds;
        this.area = new Area(formaColision);
        this.areaTransformada = this.area;
        this.activa = activa;
        this.transformacion = new AffineTransform();

    }

    public abstract boolean intersecta(Hitbox otra);

    public int getId() { return this.id; }
    public boolean estaActiva() { return this.activa; }
    public void setActiva(boolean activa) { this.activa = activa; }
    public Shape getFormaColision() { return this.formaColisionTransformada; }
    public Area getArea() { return this.areaTransformada; }
    public Rectangle2D getBounds() { return this.boundsTransformados; }
    public void actualizarTransformacion(VecDouble2D posicion) {
        this.transformacion.setToTranslation(posicion.getX(),posicion.getY());
        this.formaColisionTransformada = transformacion.createTransformedShape(formaColision);
        this.boundsTransformados = transformacion.createTransformedShape(bounds).getBounds2D();
        this.areaTransformada = area.createTransformedArea(transformacion);
    }

    protected AffineTransform getTransformacion() { return this.transformacion; }

    protected boolean genericoConGenerico(Hitbox otra) {
        // fase general: comprueba que matematicamente puedan colisionar
        System.out.printf("(%f,%f)%n",this.getBounds().getX(), this.getBounds().getY());
        System.out.printf("(%f,%f)%n",otra.getBounds().getX(), otra.getBounds().getY());
        if (!(this.getBounds().intersects(otra.getBounds()))) {
            return false; 
        } else {
            Area interseccion = this.getArea();
            interseccion.intersect(otra.getArea());
            if (interseccion.isEmpty()) { return false; }
            else { return true; }
        }  
    }

}
