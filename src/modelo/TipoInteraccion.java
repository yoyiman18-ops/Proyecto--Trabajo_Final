package modelo;

public enum TipoInteraccion {
    DAÑAR(1),
    SOLIDO(2),
    AÑADIR_EXP(3);

    private final int clase;
    private TipoInteraccion(int clase) { this.clase = clase; }
    public int clase() { return this.clase; }

    public static TipoInteraccion deInt(int clase) {
        for (TipoInteraccion interaccion : TipoInteraccion.values()) {
            if (interaccion.clase() == clase) { return interaccion; }
        }
        return null;
    }
}
