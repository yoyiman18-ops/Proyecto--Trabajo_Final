package modelo.proyectil;

import controlador.EntidadControlador;
import controlador.ProyectilControlador;
import modelo.EnsambladorControlador;
import motor.controlador.ControladorEntidad;
import motor.modelo.Entidad;
import motor.modelo.ModeloEntidad;
import motor.modelo.graficos.GestorSheetAnimada;
import motor.modelo.graficos.Sheet;
import motor.persistencia.dao.DAOSoloLectura;
import vista.VistaImagen;

public class EnsambladorControladorEntidad {
    private final DAOSoloLectura<Integer,Sheet> sheetDAO;

    public EnsambladorControladorEntidad(DAOSoloLectura<Integer,Sheet> sheetDAO) {
        this.sheetDAO = sheetDAO;
    }

    @Override public ControladorEntidad crearControlador(ModeloEntidad modelo) {
        Sheet sheet = ().cargar(modelo.getIdGlobal());
        EntidadControlador controlador = new EntidadControlador(
            modelo,
            new VistaImagen(sheet.getImagen()),
            new GestorSheetAnimada(sheet)
        );
        return controlador;
    }
}
