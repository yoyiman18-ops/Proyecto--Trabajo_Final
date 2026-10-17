package modelo;

public record ItemMejoraAtributo(Atributo atributo, double cantidad) {
    public enum Atributo {
        VIDA_MAXIMA,
        DANIO_ATAQUE,
        VELOCIDAD_MAXIMA,
        DEFENSA
    }

    public ItemMejoraAtributo {
        if (atributo == null || !Double.isFinite(cantidad) || cantidad <= 0) {
            throw new IllegalArgumentException("El atributo y una cantidad positiva son obligatorios.");
        }
    }

    public void aplicar(ModeloJugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo.");
        }
        switch (atributo) {
            case VIDA_MAXIMA -> jugador.aumentarVidaMaxima(cantidad);
            case DANIO_ATAQUE -> jugador.aumentarDanioAtaque(cantidad);
            case VELOCIDAD_MAXIMA -> jugador.aumentarVelocidadMaxima(cantidad);
            case DEFENSA -> jugador.aumentarDefensa(cantidad);
        }
    }
}
