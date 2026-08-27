package modelo;
import javafx.geometry.Rectangle2D;

public abstract class Entidad implements 
    SpriteModelo,
    Comparable<Entidad> {

    private static int idActual = 0;

    private final int id;
    private final String nombre;
    protected final Vec2 posicion;
    private final Hitbox hitbox;

    protected Entidad(Builder<?, ?> builder) {
        this.id = siguienteId();
        this.nombre = builder.nombre;
        this.posicion = builder.posicion;
        this.hitbox = builder.hitbox;
    }

    public abstract static class Builder<B extends Builder<B,T>,T extends Entidad> {
        private String nombre;
        private final Vec2 posicion = new Vec2();
        private Hitbox hitbox = new Hitbox();
        
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

        public B hitbox(double ancho, double alto, double desplazamientoX, double desplazamientoY, boolean activa) { 
            this.hitbox = new Hitbox(ancho, alto, desplazamientoX, desplazamientoY, activa);
            return self(); 
        }

    }

    private int siguienteId() { idActual++; return idActual-1;  }

    public int getId() { return id; }
    public void setPosicion(double x, double y) { posicion.setX(x); posicion.setY(y); }
    @Override public Vec2 getPosicion() { return posicion.clone(); }
    @Override public String getNombre() { return nombre; }
    public boolean hitboxActiva() { return (hitbox.estaActiva() && !hitbox.getDimension().equals(Vec2.ORIGEN)); }
    public boolean intersecta(Entidad otro) { return getPoligonoColision().intersects(otro.getPoligonoColision()); }
    public Rectangle2D getPoligonoColision() {
        return new Rectangle2D(
            this.posicion.getX() + this.hitbox.getDesplazamiento().getX(),
            this.posicion.getY() + this.hitbox.getDesplazamiento().getY(),
            this.hitbox.getDimension().getX(),
            this.hitbox.getDimension().getY()
        );
    }

    public abstract void colisionar(Entidad otra);

    @Override
    public int compareTo(Entidad otro) {
        return Integer.compare(this.id, otro.id);
    }

    @Override
    public String toString() {
        String posicionString;
        if (this.posicion == null) { posicionString = "Null"; }
        else { posicionString = this.posicion.toString(); }

        return String.format("Nombre: %s%nPosicion: %s" , nombre, posicionString);
    }

}
