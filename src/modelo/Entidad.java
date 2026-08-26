package modelo;
import javafx.geometry.Rectangle2D;

public abstract class Entidad implements SpriteModelo, Colisionable {

    private final String nombre;
    protected final Vec2 posicion;
    private final Vec2 hitbox; // dimensión lógica de la hitbox

    protected Entidad(Builder<?, ?> builder) {
        this.nombre = builder.nombre;
        this.posicion = builder.posicion;
        this.hitbox = builder.hitbox;
    }

    public abstract static class Builder<B extends Builder<B,T>,T extends Entidad> {
        private String nombre;
        private final Vec2 posicion = new Vec2();
        private Vec2 hitbox;
        
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

        public B hitbox(double x, double y) { this.hitbox = new Vec2(x,y); return self(); }
    }

    @Override public boolean intersecta(Colisionable otro) { return getHitbox().intersects(otro.getHitbox()); }

    public void setPosicion(double x, double y) { posicion.setX(x); posicion.setY(y); }
    public String getNombre() { return nombre; }
    public Rectangle2D getHitbox() { return new Rectangle2D(posicion.getX(), posicion.getY(), posicion.getY(), hitbox.getY()); }
    @Override public String getId() { return nombre; }
    @Override public Vec2 getPosicion() { return posicion.clone(); }

    @Override
    public String toString() {
        String posicionString;
        if (this.posicion == null) { posicionString = "Null"; }
        else { posicionString = this.posicion.toString(); }

        return String.format("Nombre: %s%nPosicion: %s" , nombre, posicionString);
    }

} 
