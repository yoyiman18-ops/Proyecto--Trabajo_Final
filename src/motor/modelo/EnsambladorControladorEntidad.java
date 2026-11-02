package motor.modelo;

import java.util.Objects;

import motor.controlador.ControladorEntidad;
import motor.controlador.EntidadControlador;
import motor.modelo.graficos.GestorSheetAnimada;
import motor.modelo.graficos.Sheet;
import motor.persistencia.dao.DAOSoloLectura;
import motor.recursos.GestorAudio;
import motor.vista.VistaImagen;

public class EnsambladorControladorEntidad {
    private final DAOSoloLectura<Integer,Sheet> sheetDAO;
    private final GestorAudio gestorAudio;

    public EnsambladorControladorEntidad(
        DAOSoloLectura<Integer,Sheet> sheetDAO,
        GestorAudio gestorAudio
    ) {
        this.sheetDAO = Objects.requireNonNull(sheetDAO, "El DAO de sheets no puede ser nulo");
        this.gestorAudio = Objects.requireNonNull(gestorAudio, "El gestor de audio no puede ser nulo");
    }

    public ControladorEntidad crearControlador(ModeloEntidad modelo) {
        Sheet sheet = sheetDAO.cargar(modelo.getIdGlobal());
        EntidadControlador controlador = new EntidadControlador(
            modelo,
            new VistaImagen(sheet.getImagen()),
            new GestorSheetAnimada(sheet),
            gestorAudio
        );
        return controlador;
    }
}
