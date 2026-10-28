package modelo;

public record EstadoOleadas(
    int oleada,
    int enemigos,
    long segundosHastaOleada,
    boolean completadas
) {}
