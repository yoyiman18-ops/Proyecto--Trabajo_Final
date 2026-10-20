package motor.persistencia.dao;

import motor.modelo.graficos.Frame;
import motor.persistencia.PKFrame;

public abstract class FrameDAO implements DAO<PKFrame,Frame> {

    // claves compuestas de "entidad fuerte" (agregacion de sheet con animacion)
    private final String nombreAnimacion;
    private final int idSheet;

    public FrameDAO(String nombreAnimacion, int idSheet) {
        if (nombreAnimacion == null || nombreAnimacion.isEmpty() || nombreAnimacion.isBlank()) {
            throw new IllegalArgumentException("nombreAnimacion nulo o vacio invalido");
        }
        this.nombreAnimacion = nombreAnimacion;
        this.idSheet = idSheet;
    }

    protected String getNombreAnimacion() { return this.nombreAnimacion; }
    protected int getIdSheet() { return this.idSheet; }
}
