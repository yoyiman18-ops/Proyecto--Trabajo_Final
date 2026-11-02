package motor.colisiones;

/**
 * Estrategia encargada de resolver la separación física (desplazamiento)
 * entre dos colisionables que intersectan en el espacio.
 */
public interface EstrategiaResolucionColision {

    /**
     * Resuelve la separación física entre dos colisionables para evitar solapamientos.
     *
     * @param c1 Primer colisionable.
     * @param c2 Segundo colisionable.
     * @return {@code true} si se aplicó separación física, {@code false} si no hubo colisión
     *         o no correspondía separar físicamente (por ejemplo, si alguno es fantasma
     *         o ambos son inamovibles).
     */
    boolean resolverSeparacionFisica(Colisionable c1, Colisionable c2);
}
