package motor.persistencia.dao;

import motor.modelo.graficos.Recorte;
import motor.persistencia.PKRecorte;

public abstract class RecorteDAO implements DAO<PKRecorte,Recorte> {
    
    private final int idSheet;

    public RecorteDAO(int idSheet) {
        this.idSheet = idSheet;
    }

    protected int getIdSheet() { return this.idSheet; }

}
