public class EntidadMovil extends Entidad {

    protected Vec2 direccion; // precondicion para todo lo que use direccion: direccion es un vec2 normalizado
    private double aceleracion; // tasa de cambio de velocidad en el tiempo
    private double velocidad; // tasa de cambio de posicion en el tiempo en base a la direccion

    // ej: velocidad = velocidad + aceleracion
    // ej: posicion x = posicion x + direccion.x * velocidad


    public EntidadMovil() {
        super();
        direccion = new Vec2();
        velocidad = 0;
        aceleracion = 0;
    }

    public EntidadMovil(Vec2 posicion, Entidad dueño, Vec2 direccion, double aceleracion, double velocidad) {
        super(posicion,dueño);
        this.direccion = new Vec2(direccion.getX(),direccion.getY());
        this.direccion.normalizar();
        this.aceleracion = aceleracion;
        this.velocidad = velocidad;
    }

    public void setDireccion(double x, double y) {
        direccion.setX(x);
        direccion.setY(y);
        direccion.normalizar();
    }

    public double getVelocidad() {
        return velocidad;
    }

    public double getAceleracion() {
        return aceleracion;
    }

    public void acelerar() {
        velocidad += aceleracion;
    }

    public void frenar() {
        velocidad -= aceleracion;
    }

    public void mover() {
        setPosicion(
            posicion.getX() + direccion.getX() * velocidad,
            posicion.getY() + direccion.getY() * velocidad
        );
    }



}