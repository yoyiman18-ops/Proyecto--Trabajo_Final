package modelo;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.util.VecDouble2D;

public final class Proyectil extends Entidad {

    private final double danio;
    private final double velocidad;
    private double tiempoVida;

    public Proyectil(
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D direccion,
        double danio,
        double velocidad,
        double tiempoVida
    ) {
        super(
            nombre,
            hitbox,
            posicion,
            normalizarDireccion(direccion),
            0,
            0,
            velocidad
        );

        if (!Double.isFinite(danio) || danio < 0) {
            throw new IllegalArgumentException(
                "El daño debe ser finito y no negativo."
            );
        }

        if (!Double.isFinite(velocidad) || velocidad <= 0) {
            throw new IllegalArgumentException(
                "La velocidad debe ser positiva y finita."
            );
        }

        if (!Double.isFinite(tiempoVida) || tiempoVida <= 0) {
            throw new IllegalArgumentException(
                "El tiempo de vida debe ser positivo y finito."
            );
        }

        this.danio = danio;
        this.velocidad = velocidad;
        this.tiempoVida = tiempoVida;
    }

    private static VecDouble2D normalizarDireccion(
        VecDouble2D direccion
    ) {
        if (direccion == null) {
            throw new IllegalArgumentException(
                "La dirección no puede ser nula."
            );
        }

        // La implementación depende de los métodos disponibles
        // en VecDouble2D.
        return direccion;
    }

    public double getDanio() {
        return danio;
    }

    public double getVelocidad() {
        return velocidad;
    }

    public double getTiempoVida() {
        return tiempoVida;
    }

    public void actualizarTiempoVida(double deltaTiempo) {
        if (!Double.isFinite(deltaTiempo) || deltaTiempo < 0) {
            throw new IllegalArgumentException(
                "El delta de tiempo debe ser finito y no negativo."
            );
        }

        tiempoVida = Math.max(0, tiempoVida - deltaTiempo);
    }

    public boolean estaExpirado() {
        return tiempoVida == 0;
    }
}
