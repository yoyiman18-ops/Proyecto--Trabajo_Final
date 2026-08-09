public class Personaje extends EntidadMovil {
    
    private float vida,vidaMax;
    private float daño;

    public Personaje() {
        vida = 100;
        vidaMax = 100;
        daño = 20;
    }

    public Personaje(float vida, float vidaMax, float daño) {
        this.vida = vida;
        this.vidaMax = vidaMax;
        this.daño = daño;
    }

}
