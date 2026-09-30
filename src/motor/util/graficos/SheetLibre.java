package motor.util.graficos;

import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

/**
 * Una hoja donde cada recorte puede tener tamaño y posición arbitraria. No es recomendable para animaciones,
 * pero si para elementos de HUD, GUI, etc.
 */
public class SheetLibre {
    private final Map<String,Rectangle2D> recortes;

    public SheetLibre() {
        this.recortes = new HashMap<>();
    }

    public void addRecorte(String nombre, Rectangle2D recorte) { recortes.computeIfAbsent(nombre, n -> recorte); }
    public Rectangle2D getRecorte(String nombre) { return recortes.get(nombre); }



}
