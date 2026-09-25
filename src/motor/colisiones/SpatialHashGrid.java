/*
Para resolver las colisiones, se divide el escenario en una "cuadrícula" lógica.
Cada entidad pertenece a celdas de la cuadricula dependiendo de su posición y su hitbox,
entonces solo se toman en cuenta para las colisiones los objetos "adyacentes" que estén en las mismas celdas.
Sino, se deberían comparar absolutamente todas la entidades con cada otra entidad.
*/

package motor.colisiones;

import java.util.ArrayList;
import java.util.Collection;
import java.awt.Shape;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class SpatialHashGrid {

    private final int tamañoCelda; // aproximadamente debería ser el doble del tamaño de una hitbox promedio
    private final Long2ObjectOpenHashMap<ArrayList<Colisionable>> cuadricula;

    public SpatialHashGrid(int tamañoCelda) {
        if (tamañoCelda < 1) { throw new IllegalArgumentException("Tamaño de celda no puede ser < 1"); }
        this.tamañoCelda = tamañoCelda;
        cuadricula = new Long2ObjectOpenHashMap<>();
    }

    /**
     * 
     * Inserta un objeto a un conjunto de celdas lógicas de la cuadrícula (HashMap), dependiendo de su hitbox.
     * 
     * <p>Se calculan las celdas a la que pertenece el objeto a través de getCelda() para las dimensiones X e Y de su polígono de colisión.
     * Luego, se recorre en X e Y entre la celdaMinX hasta celdaMaxX, con un bucle anidado entre celdaMinY hasta celdaMaxY;
     * para cada iteración del bucle se calcula la clave hash dada por la celdaX y celdaY, y con esta clave hash, es posible
     * asignar el objeto Entidad a una celda (división de la cuadrícula) en la que están todos los objetos cercanos al parámetro objeto.
     * El bucle se asegura que el objeto se coloque en todas las celdas correspondientes al tamaño y posición de su polígono de colisión. 
     * De no existir la celda correspondiente, la crea y añade el objeto en ella. Dos objetos con posiciones similares siempre 
     * caerán en las mismas celdas por cómo funcionan los hashes.
     * 
     * 
     * @param objeto El objeto con hitbox a insertar.
     * @return {@code true} si completó la inserción, {@code false} si falló porque el objeto no tiene hitbox activa.
     */
    public boolean insertar(Colisionable c) {
        if (!c.colisionesActivas()) { return false; }

        Shape formaColision = c.getHitbox().getFormaColision();
        int celdaMinX = calcularIndiceCelda(formaColision.getBounds2D().getMinX());
        int celdaMaxX = calcularIndiceCelda(poligonoColision.getMaxX());
        int celdaMinY = calcularIndiceCelda(poligonoColision.getMinY());
        int celdaMaxY = calcularIndiceCelda(poligonoColision.getMaxY());

        for (int celdaX = celdaMinX; celdaX <= celdaMaxX; celdaX++) {
            for (int celdaY = celdaMinY; celdaY <= celdaMaxY; celdaY++) {
                cuadricula.computeIfAbsent(
                    calcularClaveCelda(celdaX, celdaY),
                    claveCelda -> new ArrayList<>()
                    ).add(objeto);
            }
        }
        return true;
    }

    public ArrayList<Colisionable> getCeldaEn(int x, int y) { return cuadricula.get(calcularClaveCelda(x, y)); }
    public Collection<ArrayList<T>> getCeldas() { return cuadricula.values(); } 
    public void suprimirCeldaEn(int x, int y) { cuadricula.remove(calcularClaveCelda(x, y)); }
    public void limpiar() { cuadricula.clear(); }
    public boolean vacia() { return cuadricula.isEmpty(); }
    private int calcularIndiceCelda(double valor) { return (int) Math.floor(valor / tamañoCelda); }
    private long calcularClaveCelda(int x, int y) { return ((long) x << 32) | (y & 0xffffffffL); }

}
