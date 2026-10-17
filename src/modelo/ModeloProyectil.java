package modelo;

import motor.modelo.Entidad;
import motor.util.VecDouble2D;

public interface ModeloProyectil {
    VecDouble2D getPosicion();
    VecDouble2D getVelocidad();
    double getVelocidadMaxima();
    double getDanio();
    double getTiempoVida();
    void actualizarTiempoVida(double deltaTiempo);
    boolean estaExpirado();
    void mover(double dt);
    Entidad getEntidad();
}
