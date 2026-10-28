package modelo;

import motor.modelo.Dañable;
import motor.modelo.Entidad;
import motor.util.VecDouble2D;

public interface ModeloEnemigo extends Dañable {
    VecDouble2D getPosicion();
    VecDouble2D getVelocidad();
    double getVelocidadMax();
    double getVida();
    double getVidaMaxima();
    double getDanio();
    int getExperienciaRecompensa();
    void actualizarEnfriamientoAtaque(double dt);
    boolean atacar(ObjetivoEnemigo objetivo);
    void perseguir(VecDouble2D destino, double dt);
    void detener();
    void mover(double dt);
    Entidad getEntidad();
}
