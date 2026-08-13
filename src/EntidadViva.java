public class EntidadViva extends EntidadMovil {
   private double vida,vidaMax;
   private double dañoBase;

   public EntidadViva(double vida, double vidaMax, double dañoBase) {
    if (vidaMax <= 0) { 
        throw new IllegalArgumentException("Vida max invalida");
    }
    this.vidaMax = vidaMax;
  
   if (vida <= 0) { 
        throw new IllegalArgumentException("Vida invalida"); 
    } else if (vida > vidaMax) { 
        this.vida = vidaMax; 
    } else {
        this.vida = vida; 
    }

    if (dañoBase < 0) {
        throw new IllegalArgumentException("Daño invalido");
    }
    this.dañoBase = dañoBase;
    }

    public boolean recibirDaño(double cantidad) {
    if (cantidad <= 0) {
        return false;
    }

    this.vida -= cantidad;
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

    public double getDañoBase() {
        return this.dañoBase;
        }
}