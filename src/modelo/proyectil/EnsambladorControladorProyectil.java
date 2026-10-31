package modelo.proyectil;

import controlador.ProyectilControlador;
import modelo.EnsambladorControlador;
import motor.controlador.ControladorEntidad;
import motor.modelo.graficos.GestorSheetAnimada;
import motor.modelo.graficos.Sheet;
import motor.persistencia.dao.DAOSoloLectura;
import vista.VistaImagen;

public class EnsambladorControladorProyectil 
extends EnsambladorControlador<ModeloProyectil> {

    public EnsambladorControladorProyectil(DAOSoloLectura<Integer,Sheet> sheetDAO) { super(sheetDAO); }

    @Override public ControladorEntidad crearControlador(ModeloProyectil modelo) {
        Sheet sheet = getSheetDAO().cargar(modelo.getIdGlobal());
        ProyectilControlador pc = new ProyectilControlador(
            modelo,
            new VistaImagen(sheet.getImagen()),
            new GestorSheetAnimada(sheet)
        );
        return pc;
    }
}
