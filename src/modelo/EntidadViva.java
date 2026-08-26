package modelo;

public class EntidadViva extends EntidadMovil {
   private int vida,vidaMax;
   private int defensa;

    private static final int VIDA_DEFAULT = 20;

    private EntidadViva(Builder builder) {
        super(builder);
        this.vida = builder.vida;
        this.vidaMax = builder.vidaMax;
        this.defensa = builder.defensa;
    }

    // nota - la estructura es:
    // public static class Builder extends Padre.Builder<Builder,Hijo>
    public static class Builder extends EntidadMovil.Builder<EntidadViva.Builder,EntidadViva> {
        private int vida = 0; // valor sentinela, no se puede setear normalmente en 0. para setear vida = vidaMax en .build
        private int vidaMax = VIDA_DEFAULT;
        private int defensa;

        // se crea un método que retorna EntidadViva.Builder para cada atributo relevante de EntidadViva
        // lo que sucede es que cada vez se usa el mismo Builder, no se crea un nuevo objeto Builder.
        // al terminar la configuración (ya sea por defecto o personalizada) se usa .build() y retorna un nuevo objeto EntidadViva
        public Builder vida(int vida) {
            if (vida <= 0) { throw new IllegalArgumentException("La vida no puede ser <= 0"); }
            this.vida = vida;
            return this;
        }

        public Builder vidaMax(int vidaMax) {
            if (vidaMax <= 0) { throw new IllegalArgumentException("La vida maxima no puede ser <= 0"); }
            this.vidaMax = vidaMax;
            return this;
        }

        public Builder defensa(int defensa) {
            if (defensa < 0 || defensa > 10) { throw new IllegalArgumentException("La defensa debe ser [0,10]"); } 
            this.defensa = defensa;
            return this;
        }

        // método requerido por clase padre abstracta, porque no puede hacer "this" ya una abstracta no puede instanciarse como objeto, en cambio la clase concreta si
        public Builder self() { return this; }

        // este es el método que aplica los parámetros del builder para crear una nueva EntidadViva
        // en base al objeto Builder, que queda descartado tras el uso
        public EntidadViva build() {
            if (vida > vidaMax) { vida = vidaMax; }
            else if (vida == 0) { vida = vidaMax; }

            return new EntidadViva(this);
        }
    }

    private double reducirDaño(double cantidad) {
        return Math.max(cantidad/Math.log10(defensa + 10.0) - defensa/2 , 1.0);
    }

    public boolean recibirDaño(double cantidad) {
        if (!estaVivo() || cantidad < 1.0) { return false; }
       
        double dañoRecibido;
        // calcula el daño recibido con defensa == 0, o con defensa >= 1
        if (defensa == 0) { dañoRecibido = cantidad; } 
        else { dañoRecibido = reducirDaño(cantidad); }

        vida -= (int) dañoRecibido; 
        if (vida < 0) { vida = 0; }
        System.out.println(getNombre() + " ha recibido " + (int) dañoRecibido + " de daño");
        if (this.vida == 0) { morir(); }
        return true; 
    }

    public boolean estaVivo() { return this.vida > 0; }
    public int getVida() { return this.vida; }
    public int getVidaMax() { return this.vidaMax; }

    public void morir() { System.out.println(getNombre() + " ha muerto."); }


    @Override
    public String toString() {
        return String.format("%s%nVida: %s/%s%nDefensa: %s",super.toString(),vida,vidaMax,defensa);
    }
}
