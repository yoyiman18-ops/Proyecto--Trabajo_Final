package motor.colisiones.hitboxes;

import java.awt.Shape;
import java.awt.geom.Area;

/**
* Una interfaz para hitboxes, capaz de manejar formas genéricas o rectangulos que son más eficientes.
*/
public interface Hitbox {
    boolean intersecta(Hitbox otra);
    boolean intersecta(HitboxRectangular otroPoligono);
    boolean estaActiva();
    Shape getFormaColision();
    Area getArea();
    void setActiva(boolean activa);
}
