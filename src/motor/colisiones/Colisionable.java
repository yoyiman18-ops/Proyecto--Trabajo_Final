package motor.colisiones;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Interactuable;
import motor.modelo.Posicionable;

public interface Colisionable extends Posicionable, Interactuable {
    public boolean colisionesActivas();
    public void colisionar(Colisionable o);
    public Hitbox getHitbox();
}
