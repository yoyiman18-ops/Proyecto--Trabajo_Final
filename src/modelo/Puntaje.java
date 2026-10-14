package modelo;

public final class Puntaje {
    private final int fecha;
    private final int puntos;

    public Puntaje(int fecha, int puntos) {
        this.fecha = fecha;
        this.puntos = puntos;
    }

    public int getFecha() {
        return fecha;
    }

    public int getPuntos() {
        return puntos;
    }
}
