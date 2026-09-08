/**
 * La fase general (Broad Phase) de este modelo de colisiones básicamente consiste en analizar
 * una división del espacio ya existente, y por cada división generar los pares de entidades que 
 * pertenecen a ella. Si dos entidades están en una misma división (en este caso una misma celda),
 * significa que están cerca y tienen una alta probabilidad de colisionar.
 * 
 */

package motor.colisiones;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import modelo.Entidad;
import motor.SpatialHashGrid;

public class FaseGeneralSpatialHashGrid implements FaseGeneral {

    private final int TAMAÑO_CELDA;
    private final SpatialHashGrid<Entidad> cuadricula;

    public FaseGeneralSpatialHashGrid(int tamañoCelda) {
        this.TAMAÑO_CELDA = tamañoCelda;
        this.cuadricula = new SpatialHashGrid<>(TAMAÑO_CELDA);
    }

    @Override
    public Collection<ParEntidades> calcularPares(Collection<Entidad> entidades) {
        cuadricula.limpiar();
        for (Entidad entidad : entidades) { cuadricula.insertar(entidad); }
        Collection<ParEntidades> pares = new ArrayList<>();
        if (cuadricula.vacia()) { return pares; }

        Set<Long> idsVisitados = new HashSet<>();  
        for (ArrayList<Entidad> celda : cuadricula.getCeldas() ) { 
            if (celda == null || celda.isEmpty()) { continue; }
            for (int i = 0; i < celda.size(); i++) {
                Entidad a = celda.get(i);
                for (int j = i+1; j < celda.size(); j++) {
                    Entidad b = celda.get(j);
                    if (idsVisitados.add(a.combinarIds(b))) { pares.add(new ParEntidades(a, b)); }
                }
            }
        }
        return pares;
    }
    
    public void limpiarCuadricula() { cuadricula.limpiar(); }



}