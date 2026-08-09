public abstract class Entidad {

    protected Vec2 posicion;
    private Entidad dueño;

    public Entidad() {
        posicion = new Vec2();
        dueño = null;
    }

    public Entidad(Vec2 posicion, Entidad dueño) {
        this.posicion = new Vec2(posicion.getX(),posicion.getY());
        this.dueño = dueño;
    }

    public Vec2 getPosicion() {
        return this.posicion;
    }

    public void setPosicion(double x, double y) {
        posicion.setX(x);
        posicion.setY(y);
    }

    public Entidad getDueño() {
        return this.dueño;
    }
} 