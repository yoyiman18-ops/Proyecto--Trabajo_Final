package motor.persistencia.dao;

import motor.modelo.graficos.Animacion;
import motor.persistencia.PKAnimacion;

public abstract class AnimacionDAO implements DAO<PKAnimacion,Animacion> {
    
    private final int idSheet;

    public AnimacionDAO(int idSheet) {
        this.idSheet = idSheet;
    }

    protected int getIdSheet() { return this.idSheet; }
}
