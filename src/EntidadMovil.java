public abstract class EntidadMovil extends Entidad {

    protected Vec2 direccion; // precondicion para todo lo que use direccion: direccion es un vec2 normalizado
    private double aceleracion; // tasa de cambio de velocidad en el tiempo
    private double velocidad; // tasa de cambio de posicion en el tiempo en base a la direccion

    // ej: velocidad = velocidad + aceleracion
    // ej: posicion x = posicion x + direccion.x * velocidad

    protected EntidadMovil(Builder<?, ?> builder) {

        super(builder);
        this.direccion = builder.direccion;
        this.aceleracion = builder.aceleracion;
        this.velocidad = builder.velocidad;
    }

    // recursividad:
    // 
    public abstract static class Builder<B extends Builder<B,T>,T extends EntidadMovil> extends Entidad.Builder<B,T> {
        private Vec2 direccion;
        private double aceleracion;
        private double velocidad;
        

        public B direccion(double x, double y) {
            this.direccion.x = x;
            this.direccion.y = y;
            this.direccion.normalizar();
            return self();
        }

        public B aceleracion(double aceleracion) {
            if (aceleracion < 0) { throw new IllegalArgumentException("Aceleracion no puede ser < 0"); }
            this.aceleracion = aceleracion;
            return self();
        }

        public B velocidad(double velocidad) {
            this.velocidad = velocidad;
            if (velocidad < 0) { throw new IllegalArgumentException("Velocidad no puede ser < 0"); }
            return self();
        }

    }



    public void setDireccion(double x, double y) {
        direccion.x = x;
        direccion.y = y;
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
        if (velocidad < 0) {
            velocidad = 0;
        }
    }

    public boolean mover() {
        if (this.posicion == null || this.direccion == null) return false;
        this.posicion.x += velocidad * direccion.x;
        this.posicion.y += velocidad * direccion.y;
        return true;
    }

    @Override 
    public String toString() {
        String direccionString;
        if (this.direccion == null) { direccionString = "Null"; }
        else { direccionString = this.direccion.toString(); }

        return String.format("%s%nDireccion: %s%nVelocidad: %.2f%nAceleracion: %.2f",
                super.toString(),
                direccionString,
                velocidad,
                aceleracion);
    }


}
