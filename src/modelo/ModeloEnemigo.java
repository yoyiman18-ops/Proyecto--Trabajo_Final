package modelo;

import motor.modelo.Entidad;
import motor.util.VecDouble2D;

public interface ModeloEnemigo {
    VecDouble2D getPosicion();
    VecDouble2D getVelocidad();
    double getVelocidadMax();
    double getVida();
    double getVidaMaxima();
    double getDanio();
    int getExperienciaRecompensa();
    boolean recibirDanio(double cantidad);
    void actualizarEnfriamientoAtaque(double dt);
    boolean atacar(ObjetivoEnemigo objetivo);
    void perseguir(VecDouble2D destino, double dt);
    void detener();
    void mover(double dt);
    Entidad getEntidad();
}
