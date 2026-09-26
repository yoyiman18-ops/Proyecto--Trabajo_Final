package motor.colisiones.sistema;

import motor.colisiones.Colisionable;
import motor.colisiones.ParColision;

/**
 * 
 * Fase del sistema de colisiones donde se evalúan los pares de colisión a partir de un conjunto
 * de colisionables.
 */
public interface FaseGeneral {
    public Iterable<ParColision> calcularPares(Iterable<Colisionable> coIisionables);
}
