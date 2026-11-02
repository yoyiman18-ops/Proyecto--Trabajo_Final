package motor.modelo;

import motor.util.Eje;
import motor.util.VecDouble2D;


public interface Móvil {
    void acelerar(VecDouble2D direccion, double dt);
    void frenar(Eje eje, double dt);
    VecDouble2D getVelocidad();
    VecDouble2D getPosicion();
    double getVelocidadMax();
    void setVelocidadMax(double velocidadMax);
    double getAceleracion();
    double getFriccion();
}
