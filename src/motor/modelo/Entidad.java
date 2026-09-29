package motor.modelo;

import motor.colisiones.Colisionable;
import motor.colisiones.hitboxes.Hitbox;

public class Entidad implements Colisionable {
    private final String nombre;
    private final Hitbox hitbox;

    private Entidad(String nombre, Hitbox hitbox) {
        this.nombre = nombre;
        this.hitbox = hitbox;
    }

    @Override public Hitbox getHitbox() { return this.hitbox; }
    @Override public boolean colisionesActivas() { return this.hitbox.estaActiva(); }
    @Override public void colisionar(Colisionable otra) { System.out.println(this.nombre +" ha colisionado."); }


}
