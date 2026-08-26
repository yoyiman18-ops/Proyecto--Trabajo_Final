package motor.colisiones;

import motor.SpatialHashGrid;
import java.util.HashSet;
import javafx.util.Pair;
import java.util.ArrayList;
import modelo.Colisionable;

public class FaseGeneral {
    private final SpatialHashGrid cuadricula;
    private final HashSet<Pair<Colisionable,Colisionable>> pares;


    public FaseGeneral(SpatialHashGrid cuadricula) {
        this.cuadricula = cuadricula;
        this.pares = new HashSet<Pair<Colisionable,Colisionable>>();
    }

    public HashSet<Pair<Colisionable,Colisionable>> getParesEnCelda(int x, int y) {
        ArrayList<Colisionable> celda = cuadricula.getCelda(x, y);
        for (Colisionable objeto : celda) {}
        }
        return pares;
        
    }



}