/**
 * La fase general (Broad Phase) de este modelo de colisiones básicamente consiste en analizar
 * una división del espacio ya existente, y por cada división generar los pares de entidades que 
 * pertenecen a ella. Si dos entidades están en una misma división (en este caso una misma celda),
 * significa que están cerca y tienen una alta probabilidad de colisionar.
 * 
 */

package motor.colisiones;

import java.util.HashSet;
import java.util.ArrayList;
import modelo.Entidad;

public class FaseGeneral {
    private final HashSet<ParColision> pares;

    public FaseGeneral() {
        this.pares = new HashSet<ParColision>();
    }

    /**
     * Calcula todos los pares de entidades que existen en una celda, y los inserta en el HashSet 'pares'.
     * 
     * <p>Itera para cada Entidad de la celda, asociándola con cada otra Entidad restante de la celda
     * a través de un ParColisión. Los pares de colisión son guardados en el atributo 'pares', y cada par
     * solo es guardado una única vez (no hay repetición por más que el mismo par de entidades se encuentre
     * en otra celda).
     * 
     * @param celda Una celda lógica de una cuadrícula espacial, donde puede haber entidades.
     * @return {@code true} si la celda no es nula y no está vacía.
     */
    public boolean calcularParesEnCelda(ArrayList<Entidad> celda) {
        if (celda == null || celda.isEmpty()) { return false; }

        for (int i = 0; i < celda.size(); i++) {
            Entidad a = celda.get(i);
            for (int j = i+1; j < celda.size(); j++) {
                Entidad b = celda.get(j);
                pares.add(new ParColision(a, b));
            }
        }
        return true;
    }

    /** Retorna el HashSet 'pares' de esta instancia */
    public HashSet<ParColision> getPares() { return pares; }
    public void limpiar() { pares.clear(); }



}