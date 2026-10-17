package modelo;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;

public final class Moneda extends Entidad implements ModeloMoneda {

    private final int valor;
    private boolean recogida;

    public Moneda(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        int valor
    ) {
        this(nombre, hitbox, posicion, valor, new NotificadorDebil<>());
    }

    public Moneda(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        int valor,
        Notificador<Entidad> notificador
    ) {
        super(
            nombre,
            hitbox,
            posicion,
            new VecDouble2D(),
            0,
            0,
            0,
            notificador
        );

        if (valor <= 0) {
            throw new IllegalArgumentException(
                "El valor de la moneda debe ser positivo."
            );
        }

        this.valor = valor;
        this.recogida = false;
    }

    @Override
    public int getValor() {
        return valor;
    }

    @Override
    public boolean estaRecogida() {
        return recogida;
    }

    @Override
    public boolean recoger() {
        if (recogida) {
            return false;
        }

        recogida = true;
        return true;
    }

    @Override
    public Entidad getEntidad() {
        return this;
    }
}

