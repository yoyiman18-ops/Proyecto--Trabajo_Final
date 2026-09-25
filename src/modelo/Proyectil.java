package modelo;

import motor.util.VecDouble2D;

/** Modelo de un proyectil disparado por el jugador. */
public class Proyectil implements SpriteModelo {

    private final VecDouble2D posicion;

    public Proyectil(double x, double y) {
        posicion = new VecDouble2D(x, y);
    }

    @Override
    public VecDouble2D getPosicion() {
        return posicion.clone();
    }

    public void setPosicion(double x, double y) {
        posicion.setX(x);
        posicion.setY(y);
    }

    @Override
    public String getNombre() {
        return "disparo";
    }
}
