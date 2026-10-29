package motor.colisiones;
import motor.modelo.Solido;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Dañable;
import motor.modelo.Posicionable;

public interface Colisionable extends Posicionable {
    public boolean colisionesActivas();
    public void colisionar(Colisionable o);
    public void colisionarCon(Dañable o);
    public void colisionarCon(Solido o);
    public Hitbox getHitbox();
}
