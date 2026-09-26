package motor.colisiones.sistema;

import java.util.logging.Logger;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import java.util.logging.Level;
import java.util.ArrayList;
import java.util.Collection;
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
    private final ArrayList<Colisionable> colisionablesActuales;
    private final LongOpenHashSet idsVisitados;

    public FaseGeneralSpatialHashGrid(int tamañoCelda) {
        if (tamañoCelda < 1) { throw new IllegalArgumentException("Tamaño de celda debe ser igual o mayor a 1"); }
        this.cuadricula = new SpatialHashGrid(tamañoCelda);
        this.pares = new ArrayList<>();
        this.colisionablesActuales = new ArrayList<>();
        this.idsVisitados = new LongOpenHashSet();
    }

    @Override
    public Iterable<ParColision> calcularPares(Iterable<Colisionable> colisionables) {
        limpiar();
        if (colisionables == null || !colisionables.iterator().hasNext()) { return pares; }
        colisionables.forEach(c -> colisionablesActuales.add(c));
        for (Colisionable c : colisionablesActuales) { cuadricula.insertar(c); }
        if (cuadricula.vacia()) { return pares; }

        for (ArrayList<Colisionable> celda : cuadricula.getCeldas()) { 
            if (celda == null || celda.isEmpty() || celda.size() <= 1 ) { continue; }
            for (int i = 0; i < celda.size() - 1; i++) {
                Colisionable a = celda.get(i);
                System.out.println(a.getHitbox().getId());
                for (int j = i+1; j < celda.size(); j++) {
                    Colisionable b = celda.get(j);
                    System.out.println(b.getHitbox().getId());
                    // si no estaba ya el id combinado, agrega el par a la lista de pares
                    if (idsVisitados.add(combinarIds(a, b))) { 
                    // si no tienen capas compatibles, los descarta. en otro caso los añade a la lista de pares
                    if (!capasCompatibles(a, b)) { continue; } else { pares.add(new ParColision(a, b)); }}
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

    private boolean capasCompatibles(Colisionable a, Colisionable b) {
        return a.getHitbox().capasCompatibles(b.getHitbox());
    }

    private long combinarIds(Colisionable a, Colisionable b) {
        return Codificacion.combinarInt(a.getHitbox().getId(), b.getHitbox().getId());
    }



}