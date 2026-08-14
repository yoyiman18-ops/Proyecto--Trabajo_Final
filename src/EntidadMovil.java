public abstract class EntidadMovil extends Entidad {

    protected Vec2 direccion; // precondicion para todo lo que use direccion: direccion es un vec2 normalizado
    private double aceleracion; // tasa de cambio de velocidad en el tiempo
    private double velocidad; // tasa de cambio de posicion en el tiempo en base a la direccion

    // ej: velocidad = velocidad + aceleracion
    // ej: posicion x = posicion x + direccion.x * velocidad

    protected EntidadMovil(Builder<?, ?> builder) {

        this.posicion = builder.posicion;
        this.direccion = builder.direccion;
        this.aceleracion = builder.aceleracion;
        this.velocidad = builder.velocidad;
    }

    public abstract static class Builder<T extends Builder<T,B>,B extends EntidadMovil> {
        private Vec2 posicion = new Vec2();
        private Vec2 direccion = new Vec2();
        private double aceleracion = 0;
        private double velocidad = 0;
        
        public abstract T self(); // debe devolver un constructor T que herede de este propio constructo

        public abstract B build(); // debe devolver un objeto de tipo B que herede de EntidadMovil

        public T posicion(double x, double y) {
            this.posicion.x = x;
            this.posicion.y = y;
            return self();
        }

        public T direccion(double x, double y) {
            this.direccion.x = x;
            this.direccion.y = y;
            this.direccion.normalizar();
            return self();
        }

        public T aceleracion(double aceleracion) {
            this.aceleracion = aceleracion;
            return self();
        }

        public T velocidad(double velocidad) {
            this.velocidad = velocidad;
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

    public void mover() {
        this.posicion.x += velocidad * direccion.x;
        this.posicion.y += velocidad * direccion.y;
    }



}
