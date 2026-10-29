package motor.modelo;

public interface Dañador {
    public double getDaño();
    public boolean dañar(Dañable o);
    public Dañador getDañador();
}
