package modelo;

import motor.controlador.ControladorEntidad;
import motor.modelo.ModeloEntidad;
import motor.modelo.graficos.Sheet;
import motor.persistencia.dao.DAOSoloLectura;

public abstract class EnsambladorControlador<T extends ModeloEntidad> {
    private final DAOSoloLectura<Integer,Sheet> sheetDAO;

    public EnsambladorControlador(DAOSoloLectura<Integer,Sheet> sheetDAO) { this.sheetDAO = sheetDAO; }

    public abstract ControladorEntidad crearControlador(T modelo);

    protected DAOSoloLectura<Integer,Sheet> getSheetDAO() { return this.sheetDAO; }

}
