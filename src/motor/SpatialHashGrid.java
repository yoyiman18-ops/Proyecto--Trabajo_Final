/*
Para resolver las colisiones, se divide el escenario en una "cuadrícula" lógica.
Cada objeto con colisiones cae en celdas de la cuadricula dependiendo de su posición,
entonces solo se calculan las colisiones con los objetos que estén en las mismas celdas.
Sino se debe comparar cada objeto con cada otro objeto existente, lo cual es O(n^2)
*/

package motor;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import javafx.geometry.Rectangle2D;
import modelo.Colisionable;
import java.util.Objects;
import modelo.Vec2;

public class SpatialHashGrid {

    private final float TAMAÑO_CELDA;
    private final ConcurrentHashMap<Long, ArrayList<Colisionable>> cuadricula;

    public SpatialHashGrid(float tamañoCelda) {
        this.TAMAÑO_CELDA = tamañoCelda;
        cuadricula = new ConcurrentHashMap<>();
    }

    private long getCelda(int celdaX, int celdaY) {
        return ((long) celdaX << 32) | (celdaY & 0xFFFFFFFFL);
    }    

    private int calcularCelda(double x) { return (int) Math.floor(x / TAMAÑO_CELDA); }

    /**
     * 
     * @param objeto
     * @return True si completó la inserción, False si falló porque el objeto no tiene hitbox.
     */
    public boolean insertar(Colisionable objeto) {
        Rectangle2D hitbox = objeto.getHitbox();
        if (hitbox == null) { return false; } // el objeto no tiene hitbox, no tiene sentido calcular su celda
        
        int celdaMinX = calcularCelda(hitbox.getMinX());
        int celdaMaxX = calcularCelda(hitbox.getMaxX());
        int celdaMinY = calcularCelda(hitbox.getMinY());
        int celdaMaxY = calcularCelda(hitbox.getMaxY());

        // para todo par de celdaX y celdaY donde pertenezca la hitbox del objeto
        for (int celdaX = celdaMinX; celdaX <= celdaMaxX; celdaX++) {
            for (int celdaY = celdaMinY; celdaY <= celdaMaxY; celdaY++) {
                // se calcula la clave a partir de (celdaX,celdaY)
                // dos objetos que estén en las mismas celdas caerán en las mismas arraylist
                long clave = Objects.hash(celdaX,celdaY);
                // guarda el objeto en la "celda" (una arraylist, conjunto de objetos en esa celda) correspondiente a la clave
                // si la "celda" no existe, la crea, y guarda el objeto en ella
                cuadricula.computeIfAbsent(clave, celda -> new ArrayList<>()).add(objeto);
            }
        }
        return true;
    }
    


}
