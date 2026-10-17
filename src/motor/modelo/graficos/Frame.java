package motor.modelo.graficos;

public class Frame {
    private final int nroFrame;
    private final Recorte recorte;
    private final int duracion; //ms

    public Frame(int nroFrame, Recorte recorte, int duracion) {
        if (recorte == null) { throw new IllegalArgumentException("Recorte no puede ser nulo"); }
        this.recorte = recorte;
        this.nroFrame = nroFrame;
        this.duracion = duracion;
    }

    public int getNroFrame() { return this.nroFrame; }
    public Recorte getRecorte() { return this.recorte; }
    public int getDuracion() { return this.duracion; }
}
