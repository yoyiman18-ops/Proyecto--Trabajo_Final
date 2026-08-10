public abstract class Entidad {

    protected Vec2 posicion;

    public Entidad() {
        posicion = new Vec2();
    }

    public Entidad(Vec2 posicion) {
        this.posicion = new Vec2(posicion.getX(),posicion.getY());
    }

    public Vec2 getPosicion() {
        return this.posicion;
    }

    public void setPosicion(double x, double y) {
        posicion.setX(x);
        posicion.setY(y);
    }

} 
