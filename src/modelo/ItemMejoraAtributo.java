package modelo;

import java.util.Objects;
import java.util.function.ObjDoubleConsumer;

public record ItemMejoraAtributo(Atributo atributo, Rareza rareza, double cantidad) {
    public enum Atributo {
        VIDA_MAXIMA(15, false, ModeloJugador::aumentarVidaMaxima),
        DANIO_ATAQUE(4, false, ModeloJugador::aumentarDanioAtaque),
        VELOCIDAD_ATAQUE(10, true, ModeloJugador::aumentarVelocidadAtaque),
        VELOCIDAD_MAXIMA(8, true, ModeloJugador::aumentarVelocidadMaxima),
        DEFENSA(4, true, ModeloJugador::aumentarDefensa),
        PROBABILIDAD_CRITICO(8, true, ModeloJugador::aumentarProbabilidadCritico),
        PROBABILIDAD_MULTIDISPARO(8, true, ModeloJugador::aumentarProbabilidadMultidisparo);

        private final double valorBase;
        private final boolean esPorcentual;
        private final ObjDoubleConsumer<ModeloJugador> aplicador;

        Atributo(double valorBase, boolean esPorcentual, ObjDoubleConsumer<ModeloJugador> aplicador) {
            this.valorBase = valorBase;
            this.esPorcentual = esPorcentual;
            this.aplicador = Objects.requireNonNull(aplicador);
        }

        public double valorBase() {
            return valorBase;
        }

        public boolean esPorcentual() {
            return esPorcentual;
        }

        public void aplicar(ModeloJugador jugador, double cantidad) {
            aplicador.accept(jugador, cantidad);
        }
    }

    public enum Rareza {
        COMUN(1.0, 0.50),
        RARA(1.6, 0.30),
        EPICA(2.4, 0.15),
        LEGENDARIA(3.5, 0.05);

        private static final Rareza[] VALORES = values();

        private final double multiplicador;
        private final double probabilidad;

        Rareza(double multiplicador, double probabilidad) {
            this.multiplicador = multiplicador;
            this.probabilidad = probabilidad;
        }

        public double multiplicador() {
            return multiplicador;
        }

        public double probabilidad() {
            return probabilidad;
        }

        public double calcularValor(Atributo atributo) {
            Objects.requireNonNull(atributo, "El atributo no puede ser nulo.");
            return Math.round(atributo.valorBase() * multiplicador);
        }

        public static Rareza sortear(double tirada) {
            if (!Double.isFinite(tirada) || tirada < 0 || tirada >= 1) {
                throw new IllegalArgumentException("La tirada debe estar en el rango [0, 1).");
            }
            double acumulado = 0;
            for (Rareza rareza : VALORES) {
                acumulado += rareza.probabilidad;
                if (tirada < acumulado) {
                    return rareza;
                }
            }
            return VALORES[VALORES.length - 1];
        }
    }

    public ItemMejoraAtributo(Atributo atributo, double cantidad) {
        this(atributo, Rareza.COMUN, cantidad);
    }

    public ItemMejoraAtributo {
        if (atributo == null || rareza == null || !Double.isFinite(cantidad) || cantidad <= 0) {
            throw new IllegalArgumentException("El atributo, la rareza y una cantidad positiva son obligatorios.");
        }
    }

    public static ItemMejoraAtributo crear(Atributo atributo, Rareza rareza) {
        if (atributo == null || rareza == null) {
            throw new IllegalArgumentException("El atributo y la rareza son obligatorios.");
        }
        return new ItemMejoraAtributo(atributo, rareza, rareza.calcularValor(atributo));
    }

    public void aplicar(ModeloJugador jugador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador no puede ser nulo.");
        }
        atributo.aplicar(jugador, cantidad);
    }
}
