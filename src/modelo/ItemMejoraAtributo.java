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

    public String descripcion() {
        return switch (atributo) {
            case VIDA_MAXIMA -> "Vida máxima +" + (int) cantidad;
            case DANIO_ATAQUE -> "Daño +" + (int) cantidad;
            case VELOCIDAD_MAXIMA -> "Velocidad máxima +" + (int) cantidad + "%";
            case DEFENSA -> "Defensa +" + (int) cantidad;
        };
    }

    public void aplicar(Personaje personaje) {
        switch (atributo) {
            case VIDA_MAXIMA -> personaje.aumentarVidaMaxima(cantidad);
            case DANIO_ATAQUE -> personaje.aumentarDanioAtaque(cantidad);
            case VELOCIDAD_MAXIMA -> personaje.aumentarVelocidadMaxima(cantidad);
            case DEFENSA -> personaje.aumentarDefensa(cantidad);
        }
    }
}