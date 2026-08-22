package modelo;
import javafx.scene.image.Image;

public abstract class Entidad implements SpriteModelo {

    protected String nombre;
    protected Vec2 posicion;

    protected Entidad(Builder<?, ?> builder) {
        this.nombre = builder.nombre;
        this.posicion = builder.posicion;
    }

    public abstract static class Builder<B extends Builder<B,T>,T extends Entidad> {
        private Vec2 posicion = new Vec2();
        private String nombre;
        
        public abstract B self(); // debe devolver un builder B que herede de este propio builder

        public abstract T build(); // debe devolver un objeto de tipo B que herede de Entidad

        public B nombre(String nombre) {
            if (nombre.isBlank() || nombre.isEmpty() || nombre == null) { throw new IllegalArgumentException("Nombre no puede estar vacio"); }
            this.nombre = nombre;
            return self();
        }

        public B posicion(double x, double y) {
            this.posicion.setX(x);
            this.posicion.setX(x);
            return self();
        }
    }

    @Override
    public Vec2 getPosicion() {
    return this.posicion.clone();
}

    @Override
    public Image getImagen() {
        return new Image("hola");
    }

    public void setPosicion(double x, double y) {
        posicion.setX(x);
        posicion.setY(y);
    }

    public void setPosicion(Vec2 posicion) {
        this.posicion = posicion;
    }

    @Override
    public String toString() {
        String posicionString;
        if (this.posicion == null) { posicionString = "Null"; }
        else { posicionString = this.posicion.toString(); }

        return String.format("Nombre: %s%nPosicion: %s" , nombre, posicionString);
    }

} 
