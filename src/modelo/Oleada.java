package modelo;

public record Oleada(
    int numero,
    int cantidadEnemigos,
    double intervaloAparicion,
    double multiplicadorVida,
    double multiplicadorDanio
) {
    public Oleada {
        if (numero <= 0) {
            throw new IllegalArgumentException("El número de oleada debe ser positivo.");
        }
        if (cantidadEnemigos <= 0) {
            throw new IllegalArgumentException("La cantidad de enemigos debe ser positiva.");
        }
        if (!Double.isFinite(intervaloAparicion) || intervaloAparicion <= 0) {
            throw new IllegalArgumentException(
                "El intervalo de aparición debe ser positivo y finito."
            );
        }
        if (!Double.isFinite(multiplicadorVida) || multiplicadorVida <= 0) {
            throw new IllegalArgumentException(
                "El multiplicador de vida debe ser positivo y finito."
            );
        }
        if (!Double.isFinite(multiplicadorDanio) || multiplicadorDanio <= 0) {
            throw new IllegalArgumentException(
                "El multiplicador de daño debe ser positivo y finito."
            );
        }
    }
}
