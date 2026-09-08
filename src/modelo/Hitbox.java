package modelo;

public class Hitbox {
    private final Vec2 desplazamiento;
    private final Vec2 dimension;
    private boolean activa;

    public Hitbox() { desplazamiento = new Vec2(); dimension = new Vec2(); }

    public Hitbox(double ancho, double alto) {
        desplazamiento = new Vec2();
        dimension = new Vec2(ancho, alto);
    }

    public Hitbox(double ancho, double alto, double desplazamientoX, double desplazamientoY) {
        desplazamiento = new Vec2(desplazamientoX, desplazamientoY);
        dimension = new Vec2(ancho, alto);
    }

    public Hitbox(double ancho, double alto, double desplazamientoX, double desplazamientoY, boolean activa) {
        desplazamiento = new Vec2(desplazamientoX, desplazamientoY);
        dimension = new Vec2(ancho, alto);
        this.activa = activa;
    }

    public boolean vacia() { return this.dimension.getX() == 0 && this.dimension.getY() == 0; }
    public Vec2 getDimension() { return dimension.clone(); }
    public void setDimension(double x, double y) { dimension.setX(x); dimension.setY(y); }
    public Vec2 getDesplazamiento() { return this.desplazamiento.clone(); }
    public boolean estaActiva() { return this.activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

}
