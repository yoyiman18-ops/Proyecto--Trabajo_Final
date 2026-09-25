package motor.colisiones.sistema;

import java.util.logging.Logger;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import motor.colisiones.ParColision;
import motor.colisiones.SpatialHashGrid;
import motor.util.Codificacion;
import motor.colisiones.Colisionable;

/**
 * 
 * Fase general que evalúa los pares de colisión a partir
 * de una estructura auxiliar <code>SpatialHashGrid</code>.
 */
public class FaseGeneralSpatialHashGrid implements FaseGeneral {
    private static final Logger logger = Logger.getLogger(FaseGeneralSpatialHashGrid.class.getName());

    private final SpatialHashGrid cuadricula;
    private final Collection<ParColision> pares;
    private final Collection<Colisionable> colisionablesActuales;
    private final LongSet idsVisitados;

    public FaseGeneralSpatialHashGrid(int tamañoCelda) {
        if (tamañoCelda < 1) { throw new IllegalArgumentException("Tamaño de celda debe ser igual o mayor a 1"); }
        this.cuadricula = new SpatialHashGrid(tamañoCelda);
        this.pares = new ArrayList<ParColision>();
        this.colisionablesActuales = new ArrayList<>();
        this.idsVisitados = LongSet.of();
    }

    @Override
    public Iterable<ParColision> calcularPares(Iterable<Colisionable> colisionables) {
        limpiar();
        if (colisionables == null || !colisionables.iterator().hasNext()) { return pares; }
        colisionables.forEach(c -> colisionablesActuales.add(c));
        for (Colisionable c : colisionablesActuales) { cuadricula.insertar(c); }
        if (cuadricula.vacia()) { return pares; }

        for (ArrayList<Colisionable> celda : cuadricula.getCeldas() ) { 
            if (celda == null || celda.isEmpty() || celda.size() <= 1 ) { continue; }
            for (int i = 0; i < celda.size() - 1; i++) {
                Colisionable a = celda.get(i);
                for (int j = i+1; j < celda.size(); j++) {
                    Colisionable b = celda.get(j);
                    // si no estaba ya el id combinado, agrega el par a la lista de pares
                    if (idsVisitados.add(combinarIds(a, b))) { pares.add(new ParColision(a, b)); }
                }
            }
        }
        return pares;
    }
    
    public void limpiar() { 
        cuadricula.limpiar();
        pares.clear();
        colisionablesActuales.clear();
        idsVisitados.clear();
    }

    private long combinarIds(Colisionable a, Colisionable b) {
        return Codificacion.combinarInt(a.getHitbox().getId(), b.getHitbox().getId());
    }



}