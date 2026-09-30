package motor;

public record EstadoOleadas(
	int oleada,
	int enemigos,
	long segundosHastaOleada,
	boolean completadas
) {}