package vista;

import javafx.geometry.Rectangle2D;
import motor.util.VecDouble2D;
import motor.util.observer.Observador;

public interface VistaImagen 

{
    public void setVisbilidad(boolean visible);
    public void setPosicion(VecDouble2D posicion);
}
