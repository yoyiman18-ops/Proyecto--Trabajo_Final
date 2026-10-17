package modelo;

public record BastonMagico(
    double danio,
    double velocidad,
    double tiempoVida,
    int penetracion
) {
    public BastonMagico {
        if (!Double.isFinite(danio) || danio < 0) {
            throw new IllegalArgumentException("El daño debe ser finito y no negativo.");
        }
        if (!Double.isFinite(velocidad) || velocidad <= 0) {
            throw new IllegalArgumentException("La velocidad debe ser positiva y finita.");
        }
        if (!Double.isFinite(tiempoVida) || tiempoVida <= 0) {
            throw new IllegalArgumentException("El tiempo de vida debe ser positivo y finito.");
        }
        if (penetracion <= 0) {
            throw new IllegalArgumentException("La penetración debe ser positiva.");
        }
    }
}
