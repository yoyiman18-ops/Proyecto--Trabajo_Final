package motor.colisiones;
import motor.modelo.Dañador;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Dañable;

public interface Colisionable {
    public boolean colisionesActivas();
    public void colisionar(Colisionable o);
    public void colisionarCon(Dañador o);
    public void colisionarCon(Dañable o);
    public Hitbox getHitbox();
}
