public class Personaje extends EntidadMovil {
    
    private float vida,vidaMax;
    private float daño;

    public Personaje() {
        vida = 100;
        vidaMax = 100;
        daño = 20;
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
            throw new IllegalArgumentException(String.format("Daño invalido %f",daño));
        }
        this.daño = daño;
    }

}
