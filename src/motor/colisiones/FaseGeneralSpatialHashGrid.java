/**
 * La fase general (Broad Phase) de este modelo de colisiones básicamente consiste en analizar
 * una división del espacio ya existente, y por cada división generar los pares de entidades que 
 * pertenecen a ella. Si dos entidades están en una misma división (en este caso una misma celda),
 * significa que están cerca y tienen una alta probabilidad de colisionar.
 * 
 */

package motor.colisiones;
import java.util.logging.Logger;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import modelo.Entidad;
import motor.util.SpatialHashGrid;

public class FaseGeneralSpatialHashGrid implements FaseGeneral {

    private final SpatialHashGrid<Entidad> cuadricula;
    private final Logger logger;
    private final List<ParEntidades> pares;
    private final LongSet idsVisitados;

    public FaseGeneralSpatialHashGrid(int tamañoCelda) {
        if (tamañoCelda < 1) { throw new IllegalArgumentException("Tamaño de celda debe ser igual o mayor a 1"); }
        this.cuadricula = new SpatialHashGrid<>(tamañoCelda);
        this.logger = Logger.getLogger(getClass().getName());
        this.pares = new ArrayList<ParEntidades>();
        this.idsVisitados = LongSet.of();
    }

    @Override
    public List<ParEntidades> calcularPares(List<Entidad> entidades) {
        List<Entidad> entidadesActuales = new ArrayList<>(entidades);
        cuadricula.limpiar();
        pares.clear();
        for (Entidad entidad : entidadesActuales) { cuadricula.insertar(entidad); }
        if (cuadricula.vacia()) { return pares; }

        idsVisitados.clear();
        for (ArrayList<Entidad> celda : cuadricula.getCeldas() ) { 
            if (celda == null || celda.isEmpty() || celda.size() <= 1 ) { continue; }
            for (int i = 0; i < celda.size() - 1; i++) {
                Entidad a = celda.get(i);
                for (int j = i+1; j < celda.size(); j++) {
                    Entidad b = celda.get(j);
                    // si no estaba ya el id combinado, agrega el par a la lista de pares
                    if (idsVisitados.add(a.combinarIds(b))) { pares.add(new ParEntidades(a, b)); }
                }
            }
        }
        return pares;
    }
    
    public void limpiarCuadricula() { cuadricula.limpiar(); }



}