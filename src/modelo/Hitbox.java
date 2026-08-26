package modelo;

public class Hitbox {
    private final Vec2 desplazamiento;
    private final Vec2 dimension;
    private boolean activa = true;

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

    public Vec2 getDimension() { return dimension.clone(); }
    public void setDimension(double x, double y) { dimension.setX(x); dimension.setY(y); }
    public boolean estaActiva() { return this.activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

}
