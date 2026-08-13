public class EntidadViva extends EntidadMovil {
   private double vida,vidaMax;
   private double defensa;

    private static final double VIDA_DEFAULT = 20;
    private static final double DEFENSA_DEFAULT = 1;

    public EntidadViva() {
        super();
        this.vida = VIDA_DEFAULT;
        this.defensa = DEFENSA_DEFAULT;
    }

   public EntidadViva(double vida, double vidaMax, double defensa) {
    super();
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
}