package modelo;

public record ItemMejoraAtributo(Atributo atributo, Rareza rareza, double cantidad) {
    public enum Atributo {
        VIDA_MAXIMA,
        DANIO_ATAQUE,
        VELOCIDAD_ATAQUE,
        VELOCIDAD_MAXIMA,
        DEFENSA,
        PROBABILIDAD_CRITICO,
        PROBABILIDAD_MULTIDISPARO
    }

    public enum Rareza {
        COMUN,
        RARA,
        EPICA,
        LEGENDARIA
    }

    public ItemMejoraAtributo(Atributo atributo, double cantidad) {
        this(atributo, Rareza.COMUN, cantidad);
    }

    public ItemMejoraAtributo {
        if (atributo == null || rareza == null || !Double.isFinite(cantidad) || cantidad <= 0) {
            throw new IllegalArgumentException("El atributo, la rareza y una cantidad positiva son obligatorios.");
        }
    }

    public void aplicar(ModeloJugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo.");
        }
        switch (atributo) {
            case VIDA_MAXIMA -> jugador.aumentarVidaMaxima(cantidad);
            case DANIO_ATAQUE -> jugador.aumentarDanioAtaque(cantidad);
            case VELOCIDAD_ATAQUE -> jugador.aumentarVelocidadAtaque(cantidad);
            case VELOCIDAD_MAXIMA -> jugador.aumentarVelocidadMaxima(cantidad);
            case DEFENSA -> jugador.aumentarDefensa(cantidad);
            case PROBABILIDAD_CRITICO -> jugador.aumentarProbabilidadCritico(cantidad);
            case PROBABILIDAD_MULTIDISPARO -> jugador.aumentarProbabilidadMultidisparo(cantidad);
        }
    }
}
