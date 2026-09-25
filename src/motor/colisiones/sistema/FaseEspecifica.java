package motor.colisiones.sistema;
import motor.colisiones.ParColision;

/**
 * 
 * Fase del sistema de colisiones donde se efectúan las colisiones de los pares de colisión calculados.
 */
public interface FaseEspecifica {
    public void procesarColisiones(Iterable<ParColision> pares);
}
