public class EntidadViva extends EntidadMovil {
   private float vida,vidaMax;
   private float daño;

   public EntidadViva(float vida, float vidaMax, float daño){
    if (vidaMax <= 0) { 
        throw new IllegalArgumentException(String.format("Vida max invalida %f", vidaMax));
    }
    this.vidaMax = vidaMax;
  
   if (vida <= 0) { 
        throw new IllegalArgumentException(String.format("Vida invalida %f", vida)); 
    } else if (vida > vidaMax) { 
        this.vida = vidaMax; 
    } else {
        this.vida = vida; 
    }

    if (daño <= 0) {
        throw new IllegalArgumentException(String.format("Daño invalido %f",daño));
    }
    this.daño = daño;
}

public boolean recibirDaño(float cantidad
    if (daño <= 0) {
        throw new IllegalArgumentException(String.format("Daño invalido %f",daño));
    }
    this.vida -= cantidad;
    if (this.vida <= 0){
        this.vida = 0;
        }
        return true;
}
public boolean EstaVivo() {
    return this.vida > 0;
}
public float getVida() {
    return this.vida;
}
public float getVidaMax() {
    return this.vidaMax;
}
public float getDaño() {
    return this.daño;
    }
}