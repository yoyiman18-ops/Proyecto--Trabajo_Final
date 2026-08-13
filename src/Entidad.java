public abstract class Entidad {

    protected Vec2 posicion;

    public Entidad() {
        posicion = new Vec2();
    }

    public Entidad(Vec2 posicion) {
        this.posicion = posicion.clone();
    }

   public Vec2 getPosicion() {
    return this.posicion.clone();
}

    public void setPosicion(double x, double y) {
        posicion.x = y;
        posicion.x = y;
    }

} 
