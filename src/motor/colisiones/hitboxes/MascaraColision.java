package motor.colisiones.hitboxes;

public class MascaraColision {

    private final int bitMask;
    private MascaraColision(int bitMask) { this.bitMask = bitMask; }

    /**
     * Retorna una máscara de colisión basada en una máscara de bits. Cada bit corresponderá a una categoria de 
     * colisión.
     * 
     * @param categorias
     * @return <code>int</code> máscara de bits.
     */
    public static MascaraColision of(CategoriaColision... categorias) {
        int bitMask = 0;
        for (CategoriaColision categoria : categorias) {
            bitMask |= 1 << categoria.ordinal();
        }
        return new MascaraColision(bitMask);
    }

    public int interseccion(MascaraColision otra) { return getMascara() & otra.getMascara(); }
    public int getMascara() { return this.bitMask; }
}

