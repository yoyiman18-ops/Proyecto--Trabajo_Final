public abstract class Entidad {

    protected Vec2 posicion;

    public Entidad() {
        posicion = new Vec2();
    }

    public Entidad(Vec2 posicion) {
        this.posicion = new Vec2(posicion.x,posicion.y);
    }

   public Vec2 getPosicion() {
    return new Vec2(this.posicion.x, this.posicion.y);
}

    public void setPosicion(double x, double y) {
        posicion.x = y;
        posicion.x = y;
    }

} 
