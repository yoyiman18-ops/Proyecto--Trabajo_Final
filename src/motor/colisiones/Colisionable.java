package motor.colisiones;
import motor.modelo.Solido;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Dañable;

public interface Colisionable {
    public boolean colisionesActivas();
    public void colisionar(Colisionable o);
    public void colisionarCon(Dañable o);
    public void colisionarCon(Solido o);
    public Hitbox getHitbox();
}
