public interface Personaje {
    
    public void disparar();
    // {
    //    System.out.println("disparo");
    // }

    /*
    public Personaje() {
        this(100,100,20);
    }

    public Personaje(float vida, float vidaMax, float daño) {

        if (vidaMax <= 0) { 
            throw new IllegalArgumentException(String.format("Vida max invalida %f", vidaMax));
        }
        this.vidaMax = vidaMax;

        if (vida <= 0) { 
            throw new IllegalArgumentException(String.format("Vida invalida %f", vida)); 
        } 
        else if (vida > vidaMax) { 
            this.vida = vidaMax; 
        } 
        else 
        {
            this.vida = vida; 
        }

        if (daño <= 0) {
            throw new IllegalArgumentException(String.format("Daño invalido %d",daño));
        }
        this.daño = daño;
    }

    public void dañar(int daño) {
        this.vida = this.vida - daño;
        if (this.vida <= 0) {
            this.vida = 0;
            morir();
        }
    }

    public void morir() {
        System.out.printf("murio %s%n", nombre);
    }
    */
}
