package motor.modelo.graficos;

public class Frame {
    private final int idSheet;
    private final String nombreAnimacion;
    private final int nroFrame;
    private final String nombreRecorte;
    private final Recorte recorte;
    private final int duracion; //ms

    public Frame(int idSheet, String nombreAnimacion, int nroFrame, String nombreRecorte, Recorte recorte, int duracion) {
        if (recorte == null) { throw new IllegalArgumentException("Recorte no puede ser nulo"); }
        if (nombreAnimacion == null || nombreAnimacion.isEmpty() || nombreAnimacion.isBlank()) {
            throw new IllegalArgumentException("NombreAnimacion no puede ser nulo ni vacio.");
        }
        if (nombreRecorte == null || nombreRecorte.isEmpty() || nombreRecorte.isBlank()) {
            throw new IllegalArgumentException("NombreRecorte no puede ser nulo ni vacio.");
        }
        this.idSheet = idSheet;
        this.nombreAnimacion = nombreAnimacion;
        this.nombreRecorte = nombreRecorte;
        this.nroFrame = nroFrame;
        this.duracion = duracion;
        this.recorte = recorte;
    }

    public int getIdSheet() { return this.idSheet; }
    public String getNombreAnimacion() { return this.nombreAnimacion; }
    public int getNroFrame() { return this.nroFrame; }
    public String getNombreRecorte() { return this.nombreRecorte; }
    public Recorte getRecorte() { return this.recorte; }
    public int getDuracion() { return this.duracion; }
}
