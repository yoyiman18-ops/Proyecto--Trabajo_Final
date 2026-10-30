package motor.Controlador;

import motor.modelo.Activable;
import motor.modelo.Entidad;
import motor.modelo.Ticking;

public interface ModeloControladorEntidad extends Activable, Ticking 
{
    Entidad getEntidad();
}
