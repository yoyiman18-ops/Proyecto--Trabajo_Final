package motor.modelo;

import motor.colisiones.Colisionable;
import motor.colisiones.hitboxes.Hitbox;
import motor.recursos.GestorRecursos;

public class Entidad implements Colisionable {
    private final String nombre;
    private final Hitbox hitbox;
    private final GestorRecursos recursos;

    private Entidad(Builder b) {
        this.nombre = b.nombre;
        this.hitbox = b.hitbox;
        this.recursos = b.recursos;
    }

    public static class Builder {
        private final String nombre;
        private Hitbox hitbox;
        private GestorRecursos recursos;

        public Builder(String nombre) {
            this.nombre = nombre;
        }

        public Builder hitbox(Hitbox hitbox) {
            this.hitbox = hitbox;
            return this;
        }

        public Entidad build(GestorRecursos recursos) {
            if (this.hitbox == null) { throw new IllegalArgumentException("Hitbox es obligatoria"); }
            this.recursos = recursos;

            return new Entidad(this);
        }
    }

    @Override public Hitbox getHitbox() { return this.hitbox; }
    @Override public boolean colisionesActivas() { return this.hitbox.estaActiva(); }
    @Override public void colisionar(Colisionable otra) { System.out.println(this.nombre +" ha colisionado."); }


}
