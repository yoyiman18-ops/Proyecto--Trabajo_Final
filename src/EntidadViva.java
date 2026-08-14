public class EntidadViva extends EntidadMovil {
   private int vida,vidaMax;
   private int defensa;

    private static final int VIDA_DEFAULT = 20;
    private static final int DEFENSA_DEFAULT = 1;

    protected EntidadViva(Builder builder) {
        super(builder);
        this.vida = builder.vida;
        this.vidaMax = builder.vidaMax;
        this.defensa = builder.defensa;
    }

    // nota - la estructura es:
    // public static class Builder extends Padre.Builder<Builder,Hijo>
    public static class Builder extends EntidadMovil.Builder<EntidadViva.Builder,EntidadViva> {
        private int vida = VIDA_DEFAULT;
        private int vidaMax = VIDA_DEFAULT;
        private int defensa = DEFENSA_DEFAULT;

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
        public Builder vida(int vida) {
            this.vida = vida;
            return this;
        }

        public Builder vidaMax(int vidaMax) {
            this.vidaMax = vidaMax;
            return this;
        }

        public Builder defensa(int defensa) {
            this.defensa = defensa;
            return this;
        }
    }

    public boolean recibirDaño(double cantidad) {
    // si el daño es menor a 1 falla
    if (cantidad < 1.0) {
        return false;
    }
   
    // calcula el daño recibido y reduce la vida
    if (defensa == 0) { this.vida -= cantidad; } 
    else { 
        double dañoRecibido = Math.max(Math.log10(cantidad + 10.0) - defensa/2 , 1.0); 
        this.vida -= dañoRecibido; 
    }

    // si la vida es menor a 0, la pone en 0
    if (this.vida < 0) {
        this.vida = 0;
    }
    return true; 
    }

    public boolean estaVivo() {
        return this.vida > 0;
    }
    public int getVida() {
        return this.vida;
    }
    public int getVidaMax() {
        return this.vidaMax;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
