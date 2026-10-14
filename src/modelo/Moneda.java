package modelo;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.util.VecDouble2D;

public final class Moneda extends Entidad {

    private final int valor;
    private boolean recogida;

    public Moneda(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        int valor
    ) {
        super(
            nombre,
            hitbox,
            posicion,
            new VecDouble2D(),
            0,
            0,
            0
        );

        if (valor <= 0) {
            throw new IllegalArgumentException(
                "El valor de la moneda debe ser positivo."
            );
        }

        this.valor = valor;
        this.recogida = false;
    }

    public int getValor() {
        return valor;
    }

    public boolean estaRecogida() {
        return recogida;
    }

    public boolean recoger() {
        if (recogida) {
            return false;
        }

        recogida = true;
        return true;
    }
}

