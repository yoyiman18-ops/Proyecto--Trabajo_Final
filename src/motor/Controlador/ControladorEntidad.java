package motor.Controlador;

import motor.modelo.Activable;
import motor.modelo.ModeloEntidad;
import motor.modelo.Ticking;
import vista.VistaImagen;

public interface ControladorEntidad extends Activable, Ticking 
{
    ModeloEntidad getEntidad();
    VistaImagen getVista();
}
