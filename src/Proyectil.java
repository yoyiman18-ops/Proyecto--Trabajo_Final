public class Proyectil extends EntidadMovil { //implements Dañador {

    private static final double DAÑO_DEFAULT = 1;
    private static final int RANGO_DEFAULT = 1;
    private static final int PERFORACION_DEFAULT = 0;

    private double dañoBase;
    private int rango;
    private int perforacion;

    protected Proyectil(Builder builder) {
        super(builder);
        this.dañoBase = builder.dañoBase;
        this.rango = builder.rango;
        this.perforacion = builder.perforacion;
    }

    public static class Builder extends EntidadMovil.Builder<Builder,Proyectil> {

    private double dañoBase = DAÑO_DEFAULT;
    private int rango = RANGO_DEFAULT;
    private int perforacion = PERFORACION_DEFAULT;

        public Builder self() {
            return this;
        }

        public Proyectil build() {
            return new Proyectil(this);
        }

        public Builder rango(int rango) {
            this.rango = rango;
            return this;
        }

        public Builder dañoBase(double dañoBase) {
            this.dañoBase = dañoBase;
            return this;
        }

        public Builder perforacion(int perforacion) {
            this.perforacion = perforacion;
            return this;
        }
        

    }    

    // @Override
    public boolean dañar(EntidadViva objetivo) {
        if (!(objetivo.estaVivo())) {
            return false;
        }        
        objetivo.recibirDaño(0);
        this.perforacion--;
        return true;
    }
    
}
