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
import motor.colisiones.Colisionable;

/**
 * 
 * Fase general que evalúa los pares de colisión a partir
 * de una estructura auxiliar <code>SpatialHashGrid</code>.
 */
public class FaseGeneralSpatialHashGrid implements FaseGeneral {
    private static final Logger logger = Logger.getLogger(FaseGeneralSpatialHashGrid.class.getName());

    private final SpatialHashGrid cuadricula;
    private final ArrayList<ParColision> pares;
    private final ArrayList<Colisionable> colisionablesActuales;
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
        colisionablesActuales.clear();
        colisionablesActuales.trimToSize();
        colisionables.forEach(c -> colisionablesActuales.add(c));
        cuadricula.limpiar();
        pares.clear();
        for (Colisionable c : colisionablesActuales) { cuadricula.insertar(c); }
        if (cuadricula.vacia()) { return pares; }

        idsVisitados.clear();
        for (ArrayList<Colisionable> celda : cuadricula.getCeldas() ) { 
            if (celda == null || celda.isEmpty() || celda.size() <= 1 ) { continue; }
            for (int i = 0; i < celda.size() - 1; i++) {
                Colisionable a = celda.get(i);
                for (int j = i+1; j < celda.size(); j++) {
                    Colisionable b = celda.get(j);
                    // si no estaba ya el id combinado, agrega el par a la lista de pares
                    if (idsVisitados.add(a.combinarIds(b))) { pares.add(new ParEntidades(a, b)); }
                }
            }
        }
        return pares;
    }
    
    public void limpiarCuadricula() { cuadricula.limpiar(); }
    
    private long combinarIds



}