package modelo;

import java.util.Objects;
import motor.util.VecDouble2D;

public record DatosDisparo(
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
