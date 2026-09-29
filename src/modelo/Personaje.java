package modelo;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.util.VecDouble2D;

public class Personaje extends Entidad {
    private double vida,vidaMax;
    
    private Personaje(Builder b) {
        super(b.nombre, b.hitbox, b.posicion, b.velocidad, b.aceleracion, b.friccion, b.velocidadMax);
        this.vida = b.vida;
        this.vidaMax = b.vidaMax;
    }

    public static class Builder {
        private final String nombre;
        private final Hitbox hitbox;
        private VecDouble2D posicion;
        private VecDouble2D velocidad;
        private double aceleracion;
        private double friccion;
        private double velocidadMax;
        private double vida,vidaMax;

        public Builder(String nombre, Hitbox hitbox) {
            this.nombre = nombre;
            this.hitbox = hitbox;
            this.posicion = new VecDouble2D(0, 0);
            this.velocidad = new VecDouble2D(0,0);
            this.aceleracion = 1;
            this.friccion = 0.75;
            this.vidaMax = 100;
            this.velocidadMax = aceleracion * 2;
            this.vida = vidaMax;
        }

        public Builder posicion(VecDouble2D posicion) {
            this.posicion = posicion;
            return this;
        }

        public Builder velocidad(VecDouble2D velocidad) {
            this.velocidad = velocidad;
            return this;
        }

        public Builder aceleracion(double aceleracion) {
            if (aceleracion < 0) { throw new IllegalArgumentException("Aceleracion negativa no es valida"); }
            this.aceleracion = aceleracion;
            return this;
        }

        public Builder friccion(double friccion) {
            if (friccion < 0) { throw new IllegalArgumentException("Friccion negativa no es valida"); }
            this.friccion = friccion;
            return this;
        }

        public Builder velocidadMax(double velocidadMax) {
            this.velocidadMax = velocidadMax;
            return this;
        }

        public Builder vidaMax(double vidaMax) {
            if (vidaMax <= 0) { throw new IllegalArgumentException("Vida maxima menor o igual a 0 no es valida"); }
            this.vidaMax = vidaMax;
            return this;
        }

        public Builder vida(double vida) {
            if (vida < 0) { throw new IllegalArgumentException("Vida negativa no es valida"); }
            this.vida = vida;
            return this;
        }

        public Personaje build() {
            if (this.vida > this.vidaMax) { this.vida = this.vidaMax; }
            if (this.vida < 0) { this.vida = 0; }
            return new Personaje(this);
        }
    }


}
