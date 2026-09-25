package motor.colisiones;

import motor.colisiones.hitboxes.Hitbox;

public interface Colisionable {
    public void Colisionar(Colisionable otro);
    public void getHitbox(Hitbox hitbox);
}
