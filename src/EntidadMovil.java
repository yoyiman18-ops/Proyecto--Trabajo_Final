public abstract class EntidadMovil extends Entidad {

    protected Vec2 direccion; // precondicion para todo lo que use direccion: direccion es un vec2 normalizado
    private double aceleracion; // tasa de cambio de velocidad en el tiempo
    private double velocidad; // tasa de cambio de posicion en el tiempo en base a la direccion

    // ej: velocidad = velocidad + aceleracion
    // ej: posicion x = posicion x + direccion.x * velocidad

    protected EntidadMovil(Builder builder) {
        this.direccion = builder.direccion;
        this.aceleracion = builder.aceleracion;
        this.velocidad = builder.velocidad;
    }

    public abstract static class Builder {
        private Vec2 direccion;
        private double aceleracion;
        private double velocidad;
        
        public Builder direccion(double x, double y) {
            this.direccion.x = x;
            this.direccion.y = y;
            return this;
        }

        public Builder aceleracion(double aceleracion) {
            this.aceleracion = aceleracion;
            return this;
        }

        public Builder velocidad(double velocidad) {
            this.velocidad = velocidad;
            return this;
        }

        public abstract EntidadMovil build();
    }

    // private EntidadMovil(Builder builder) {
    //     super();
    //     direccion = new Vec2(); // esto es la dirección de su movimiento, no a donde mira
    //     velocidad = 0;
    //     aceleracion = 0;
    // }
    //
    // public EntidadMovil(Vec2 posicion, Vec2 direccion, double aceleracion, double velocidad) {
    //     super(posicion);
    //     if (velocidad < 0) {
    //         throw new IllegalArgumentException("Velocidad no puede ser menor a 0.");
    //     }
    //     if (aceleracion < 0) {
    //         throw new IllegalArgumentException("Aceleracion no puede ser menora 0.");
    //     }
    //
    //     this.direccion = direccion.clone();
    //     this.direccion.normalizar();
    //     this.aceleracion = aceleracion;
    //     this.velocidad = velocidad;
    // }


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
        setPosicion(
            posicion.x + direccion.x * velocidad,
            posicion.y + direccion.y * velocidad
        );
    }



}
