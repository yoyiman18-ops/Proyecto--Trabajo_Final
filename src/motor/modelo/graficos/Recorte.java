package motor.modelo.graficos;

import javafx.geometry.Rectangle2D;

public class Recorte {
    private final String nombreRecorte;
    private final double offsetX,offsetY,ancho,alto;
    private final Rectangle2D recorte;

    public Recorte(
        String nombreRecorte,
        double offsetX,
        double offsetY,
        double ancho,
        double alto) {
            if (nombreRecorte == null || nombreRecorte.isEmpty() || nombreRecorte.isBlank()) {
                throw new IllegalArgumentException("Nombre del recorte nulo o vacio es invalido");
            }
            if (ancho <= 0 || alto <= 0) {
                throw new IllegalArgumentException("Ancho y alto deben ser mayores a 0");
            }
            this.nombreRecorte = nombreRecorte;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.ancho = ancho;
            this.alto = alto;
            this.recorte = new Rectangle2D(offsetX, offsetY, ancho, alto);
        }

    public String getNombreRecorte() { return this.nombreRecorte; }
    public double getOffsetX() { return this.offsetX; }
    public double getOffsetY() { return this.offsetY; }
    public double getAncho() { return this.ancho; }
    public double getAlto() { return this.alto; }
    public Rectangle2D get() { return this.recorte; }

}
