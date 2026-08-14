import java.awt.image.BufferedImage;

public abstract class Entidad {

    protected Vec2 posicion;
    protected BufferedImage sprite;

    protected Entidad(Builder<?, ?> builder) {
        this.posicion = builder.posicion;
        this.sprite = builder.sprite;
    }

    public abstract static class Builder<B extends Builder<B,T>,T extends Entidad> {
        private Vec2 posicion = new Vec2();
        private BufferedImage sprite;
        
        public abstract B self(); // debe devolver un builder B que herede de este propio builder

        public abstract T build(); // debe devolver un objeto de tipo B que herede de Entidad

        public B posicion(double x, double y) {
            this.posicion.x = x;
            this.posicion.y = y;
            return self();
        }

        public B sprite(BufferedImage sprite) {
            this.sprite = sprite;
            return self();
        }
        
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

    @Override
    public String toString() {
        return String.format("Posicion: %s%n" , posicion.toString());
    }

} 
