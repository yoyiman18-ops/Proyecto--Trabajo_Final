package motor.modelo.graficos;

import java.util.Comparator;
import java.util.List;

public class Animacion {
    private final int idSheet;
    private final String nombreAnimacion;
    private final boolean esBucle;
    private final List<Frame> frames;

    public Animacion(int idSheet, String nombreAnimacion, boolean esBucle, List<Frame> frames) {
        if (nombreAnimacion == null || nombreAnimacion.isEmpty() || nombreAnimacion.isBlank()) {
            throw new IllegalArgumentException("nombreAnimacion no puede ser nulo");
        }
        if (frames == null || frames.isEmpty()) {
            throw new IllegalArgumentException("La lista de frames no puede ser nula ni vacia");
        }
        this.idSheet = idSheet;
        this.nombreAnimacion = nombreAnimacion;
        this.esBucle = esBucle;
        this.frames = frames;
        this.frames.sort(Comparator.comparing(Frame::getNroFrame));
    }

    public int getIdSheet() { return this.idSheet; }
    public String getNombreAnimacion() { return this.nombreAnimacion; }
    public boolean esBucle() { return this.esBucle; }
    public List<Frame> getFrames() { return this.frames; }

}
