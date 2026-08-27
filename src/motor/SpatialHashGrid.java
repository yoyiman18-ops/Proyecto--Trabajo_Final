/*
Para resolver las colisiones, se divide el escenario en una "cuadrícula" lógica.
Cada entidad pertenece a celdas de la cuadricula dependiendo de su posición y su hitbox,
entonces solo se toman en cuenta para las colisiones los objetos "adyacentes" que estén en las mismas celdas.
Sino, se deberían comparar absolutamente todas la entidades con cada otra entidad.
*/

package motor;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.geometry.Rectangle2D;
import modelo.Entidad;

import java.util.Objects;

public class SpatialHashGrid {

    private final int TAMAÑO_CELDA; // aproximadamente debería ser el doble del tamaño de una hitbox promedio
    private final HashMap<Long, ArrayList<Entidad>> cuadricula;

    public SpatialHashGrid(int tamañoCelda) {
        if (tamañoCelda < 1) { throw new IllegalArgumentException("Tamaño de celda no puede ser < 1"); } 
        this.TAMAÑO_CELDA = tamañoCelda;
        cuadricula = new HashMap<>();
    }

    /**
     * Calcula el índice de celda que corresponde al valor, dividiéndolo por TAMAÑO_CELDA y redondeando para abajo hacia el entero más cercano.
     * 
     * @param valor (double)
     * @return Índice lógico de celda (int)
     */
    private int calcularCelda(double valor) { return (int) Math.floor(valor / TAMAÑO_CELDA); }

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
    public boolean insertar(Entidad objeto) {
        if (!objeto.hitboxActiva()) { return false; }
        
        Rectangle2D poligonoColision = objeto.getPoligonoColision();
        int celdaMinX = calcularCelda(poligonoColision.getMinX());
        int celdaMaxX = calcularCelda(poligonoColision.getMaxX());
        int celdaMinY = calcularCelda(poligonoColision.getMinY());
        int celdaMaxY = calcularCelda(poligonoColision.getMaxY());

        // para todo par de celdaX y celdaY donde pertenezca el poligono de colisión del objeto
        for (int celdaX = celdaMinX; celdaX <= celdaMaxX; celdaX++) {
            for (int celdaY = celdaMinY; celdaY <= celdaMaxY; celdaY++) {
                // se calcula la clave a partir de (celdaX,celdaY)
                // dos objetos que estén en las mismas celdas caerán en las mismas arraylist
                long clave = Objects.hash(celdaX,celdaY);
                // guarda el objeto en la "celda" (una arraylist, conjunto de objetos en esa celda) correspondiente a la clave
                // si la "celda" no existe, la crea, y guarda el objeto en ella
                cuadricula.computeIfAbsent(clave, celda -> new ArrayList<>()).add(objeto);
                System.out.printf("%d Insertado en %d %d%n", objeto.getId(), celdaX, celdaY);
            }
        }
        return true;
    }

    public ArrayList<Entidad> getCelda(int x, int y) {
        long clave = Objects.hash(x,y);
        return cuadricula.get(clave);
    }

    public void limpiar() { cuadricula.clear(); }
}
