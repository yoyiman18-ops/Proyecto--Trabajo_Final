import java.awt.image.BufferedImage;

public abstract class Entidad {

    protected Vec2 posicion;
    protected BufferedImage sprite;

    protected Entidad(Builder builder) {
        this.posicion = builder.posicion;
        this.sprite = builder.sprite;
    }

    public abstract static class Builder<T extends Builder<T,B>,B extends Entidad> {
        private Vec2 posicion;
        private BufferedImage sprite;
        
    }

    public Vec2 getPosicion() {
    return this.posicion.clone();
}

    public void setPosicion(double x, double y) {
        posicion.x = y;
        posicion.y = y;
    }

    public void setPosicion(Vec2 posicion) {
        this.posicion = posicion;
    }

} 
