package motor.util;

public final class Codificacion {
    
    /**
     * Combina dos enteros de 32 bits en un entero de 64 bits.
     * 
     * @param x 32 bits más significativos
     * @param y 32 bit menos significativos
     * @return <code>long</code> que contiene los 64 bits originales.
     */
    public static long combinarInt(int x, int y) { return ((long) x <<32) | (y & 0xffffffffL); }
}
