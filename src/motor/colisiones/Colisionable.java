package motor.colisiones;
import motor.colisiones.hitboxes.Hitbox;
import motor.util.VecDouble2D;

public interface Colisionable {
    public boolean colisionesActivas();
    public VecDouble2D getPosicionHitbox();
    public void colisionar(Colisionable otro);
    public Hitbox getHitbox();
}
