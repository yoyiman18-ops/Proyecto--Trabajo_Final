public abstract class Entidad {

    protected Vec2 posicion;

    protected Entidad() {
        posicion = new Vec2();
    }

   public Vec2 getPosicion() {
    return this.posicion.clone();
}

    public void setPosicion(double x, double y) {
        posicion.x = y;
        posicion.x = y;
    }

} 
