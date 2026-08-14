public class EntidadViva extends EntidadMovil {
   private double vida,vidaMax;
   private double defensa;

    private static final double VIDA_DEFAULT = 20;
    private static final double DEFENSA_DEFAULT = 1;

    protected EntidadViva(Builder builder) {
        super(builder);
        this.vida = builder.vida;
        this.vidaMax = builder.vidaMax;
        this.defensa = builder.defensa;
    }

    // nota - la estructura es:
    // public static class Builder extends Padre.Builder<Builder,Hijo>
    public static class Builder extends EntidadMovil.Builder<EntidadViva.Builder,EntidadViva> {
        private double vida = VIDA_DEFAULT;
        private double vidaMax = VIDA_DEFAULT;
        private double defensa = DEFENSA_DEFAULT;

        // método requerido por clase padre abstracta, porque no puede hacer "this" ya una abstracta no puede instanciarse como objeto, en cambio la clase concreta si
        public Builder self() {
            return this;
        }

        // este es el método que aplica los parámetros del builder para crear una nueva EntidadViva
        // en base al objeto Builder, que queda descartado tras el uso
        public EntidadViva build() {
            return new EntidadViva(this);
        }

        // se crea un método que retorna EntidadViva.Builder para cada atributo relevante
        // lo que sucede es que cada vez se usa el mismo Builder, no se crea un nuevo objeto
        // al terminar
        public Builder vida(double vida) {
            this.vida = vida;
            return this;
        }

        public Builder vidaMax(double vidaMax) {
            this.vidaMax = vidaMax;
            return this;
        }

        public Builder defensa(double defensa) {
            this.defensa = defensa;
            return this;
        }
    }

    public boolean recibirDaño(double cantidad) {
    if (cantidad <= 0) {
        return false;
    }
    this.vida -= (cantidad / defensa);
    if (this.vida < 0) {
        this.vida = 0;
    }
    return true; 
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }
    public double getVida() {
        return this.vida;
    }
    public double getVidaMax() {
        return this.vidaMax;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
