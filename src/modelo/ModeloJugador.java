package modelo;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.modelo.Direccion;
import motor.util.Eje;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;

public interface ModeloJugador extends ObjetivoEnemigo {
    double getVidaMax();
    VecDouble2D getVelocidad();
    Hitbox getHitbox();
    Notificador<ModeloJugador> getNotificadorVida();
    Entidad getEntidad();
    void acelerar(Direccion direccion, double dt);
    void frenar(Eje eje, double dt);
    void mover(double dt);
    int ganarExperiencia(int cantidad);
    void aumentarVidaMaxima(double cantidad);
    void aumentarDanioAtaque(double cantidad);
    void aumentarVelocidadMaxima(double porcentaje);
    void aumentarDefensa(double porcentaje);
}
