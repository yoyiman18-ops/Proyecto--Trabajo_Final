package motor.colisiones;
import motor.colisiones.hitboxes.Hitbox;

public interface Colisionable {
    public boolean colisionesActivas();
    public void colisionar(Colisionable otro);
    public Hitbox getHitbox();
}
