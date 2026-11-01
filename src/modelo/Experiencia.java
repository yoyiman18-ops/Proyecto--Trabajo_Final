package modelo;

import java.util.Objects;

import motor.colisiones.Colisionable;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Dañable;
import motor.modelo.Entidad;
import motor.modelo.Solido;
import motor.util.VecDouble2D;

public final class Experiencia
extends Entidad
implements ModeloExperiencia
{
    private final ModeloJugador jugador;
    private final int cantidad;

    public Experiencia(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        int cantidad,
        ModeloJugador jugador
    ) {
        this(idGlobal, nombre, hitbox, posicion, cantidad, jugador, null);
    }

    public Experiencia(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        int cantidad,
        ModeloJugador jugador,
        Integer idSonidoExpiracion
    ) {
        super(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            new VecDouble2D(),
            0.0,
            0.0,
            0.0,
            null,
            idSonidoExpiracion
        );
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad de experiencia debe ser positiva.");
        }
        this.jugador = Objects.requireNonNull(jugador, "El jugador no puede ser nulo.");
        this.cantidad = cantidad;
    }

    @Override
    public int getCantidad() {
        return cantidad;
    }

    @Override
    public boolean recoger(ModeloJugador jugadorObjetivo) {
        Objects.requireNonNull(jugadorObjetivo, "El jugador no puede ser nulo.");
        if (!activo() || jugadorObjetivo.getVida() <= 0) {
            return false;
        }
        jugadorObjetivo.ganarExperiencia(cantidad);
        expirar(1);
        return true;
    }

    @Override
    public void colisionar(Colisionable o) {}

    @Override
    public void colisionarCon(Dañable o) {
        if (activo() && o == jugador) {
            recoger(jugador);
        }
    }

    @Override
    public void colisionarCon(Solido o) {}
}
