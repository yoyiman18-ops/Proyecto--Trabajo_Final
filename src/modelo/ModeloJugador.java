package modelo;

import java.util.List;
import java.util.Objects;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.modelo.Direccion;
import motor.util.Eje;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;

public interface ModeloJugador extends ObjetivoEnemigo {
    record DatosDisparo(
        VecDouble2D origen,
        VecDouble2D direccion,
        double danio,
        boolean esCritico
    ) {
        public DatosDisparo {
            Objects.requireNonNull(origen, "El origen del disparo no puede ser nulo.");
            Objects.requireNonNull(direccion, "La dirección del disparo no puede ser nula.");
            if (!Double.isFinite(danio) || danio < 0) {
                throw new IllegalArgumentException("El daño del disparo debe ser finito y no negativo.");
            }
        }
    }

    double getVidaMax();
    int getExperiencia();
    int getNivel();
    int getExperienciaSiguiente();
    double getDanioAtaque();
    double getProbabilidadCritico();
    double getProbabilidadMultidisparo();
    Direccion getDireccion();
    VecDouble2D getVectorDireccion();
    VecDouble2D getVelocidad();
    Hitbox getHitbox();
    Notificador<ModeloJugador> getNotificadorVida();
    Entidad getEntidad();
    void acelerar(Direccion direccion, double dt);
    void frenar(Eje eje, double dt);
    void mover(double dt);
    boolean intentarAtacar();
    List<DatosDisparo> realizarAtaque();
    int ganarExperiencia(int cantidad);
    boolean consumirNivelPendiente();
    void aumentarVidaMaxima(double cantidad);
    void aumentarDanioAtaque(double cantidad);
    void aumentarVelocidadAtaque(double porcentaje);
    void aumentarVelocidadMaxima(double porcentaje);
    void aumentarDefensa(double porcentaje);
    void aumentarProbabilidadCritico(double porcentaje);
    void aumentarProbabilidadMultidisparo(double porcentaje);
}
