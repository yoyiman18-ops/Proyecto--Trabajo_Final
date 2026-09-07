package modelo;

/** Modelo de un proyectil disparado por el jugador. */
public class Proyectil implements SpriteModelo {

    private final Vec2 posicion;

    public Proyectil(double x, double y) {
        posicion = new Vec2(x, y);
    }

    @Override
    public Vec2 getPosicion() {
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
