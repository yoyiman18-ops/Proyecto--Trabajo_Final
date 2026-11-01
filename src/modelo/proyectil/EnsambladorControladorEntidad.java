package modelo.proyectil;

import motor.controlador.ControladorEntidad;
import motor.controlador.EntidadControlador;
import motor.modelo.ModeloEntidad;
import motor.modelo.graficos.GestorSheetAnimada;
import motor.modelo.graficos.Sheet;
import motor.persistencia.dao.DAOSoloLectura;
import motor.vista.VistaImagen;

public class EnsambladorControladorEntidad {
    private final DAOSoloLectura<Integer,Sheet> sheetDAO;

    public EnsambladorControladorEntidad(DAOSoloLectura<Integer,Sheet> sheetDAO) {
        this.sheetDAO = sheetDAO;
    }

    public ControladorEntidad crearControlador(ModeloEntidad modelo) {
        Sheet sheet = sheetDAO.cargar(modelo.getIdGlobal());
        EntidadControlador controlador = new EntidadControlador(
            modelo,
            new VistaImagen(sheet.getImagen()),
            new GestorSheetAnimada(sheet)
        );
        return controlador;
    }
}
