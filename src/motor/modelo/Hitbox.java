package motor.modelo;

import java.awt.geom.Rectangle2D;
import java.awt.Shape;
import motor.util.VecDouble2D;

public class Hitbox {
    private final Shape poligonoColision;
    private boolean activa;

    public Hitbox(Shape poligonoColision) { 
        this(poligonoColision, true);
    }

    public Hitbox(Shape poligonoColision, boolean activa) {
        this.poligonoColision = poligonoColision;
        this.activa = activa;
    }

    public boolean estaActiva() { return this.activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

}
