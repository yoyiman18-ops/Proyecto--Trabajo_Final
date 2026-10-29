package modelo;

import java.util.Objects;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;

public final class Experiencia extends Entidad implements ModeloExperiencia {
    private final int cantidad;
    private boolean recogida;

    public Experiencia(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        int cantidad
    ) {
        this(nombre, hitbox, posicion, cantidad, new NotificadorDebil<>());
    }

    public Experiencia(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        int cantidad,
        Notificador<Entidad> notificador
    ) {
        super(nombre, hitbox, posicion, new VecDouble2D(), 0, 0, 0, notificador);
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de experiencia debe ser positiva.");
        }
        this.cantidad = cantidad;
        this.recogida = false;
    }

    @Override
    public int getCantidad() {
        return cantidad;
    }

    @Override
    public boolean fueRecogida() {
        return recogida;
    }

    @Override
    public boolean recoger(ModeloJugador jugador) {
        Objects.requireNonNull(jugador, "El jugador no puede ser nulo.");
        if (recogida || jugador.getVida() <= 0) {
            return false;
        }
        jugador.ganarExperiencia(cantidad);
        recogida = true;
        return true;
    }
}
