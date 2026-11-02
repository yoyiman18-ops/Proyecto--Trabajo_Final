package modelo;

import java.util.Objects;

import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.NotificadorFuerte;

public final class Partida implements ModeloPartida {
    private final int cantidadOleadas;
    private final int maxEnemigosSimultaneos;
    private final double intervaloOleadas;
    private final Notificador<EstadoOleadas> notificadorOleadas;
    private final Notificador<Integer> notificadorSonido;
    private final Integer idSonidoMostrarMejoras;
    private final Integer idSonidoMejoraAplicada;
    private final Integer idSonidoVictoria;
    private final Integer idSonidoDerrota;

    private int oleadaActual;
    private int enemigosActivos;
    private int enemigosRestantesOleada;
    private int enemigosPorAparecer;
    private double tiempoHastaOleada;
    private long ultimoSegundoPublicado;
    private boolean oleadasCompletadas;
    private boolean enPausa;
    private boolean eleccionMejoraActiva;
    private boolean partidaFinalizada;

    public Partida() {
        this(5, 6, 6.0, new NotificadorDebil<>());
    }

    public Partida(
        Integer idSonidoMostrarMejoras,
        Integer idSonidoMejoraAplicada,
        Integer idSonidoVictoria,
        Integer idSonidoDerrota
    ) {
        this(
            5,
            6,
            6.0,
            new NotificadorDebil<>(),
            new NotificadorFuerte<>(),
            idSonidoMostrarMejoras,
            idSonidoMejoraAplicada,
            idSonidoVictoria,
            idSonidoDerrota
        );
    }

    public Partida(
        int cantidadOleadas,
        int maxEnemigosSimultaneos,
        double intervaloOleadas,
        Notificador<EstadoOleadas> notificadorOleadas
    ) {
        this(
            cantidadOleadas,
            maxEnemigosSimultaneos,
            intervaloOleadas,
            notificadorOleadas,
            new NotificadorFuerte<>(),
            null,
            null,
            null,
            null
        );
    }

    public Partida(
        int cantidadOleadas,
        int maxEnemigosSimultaneos,
        double intervaloOleadas,
        Notificador<EstadoOleadas> notificadorOleadas,
        Notificador<Integer> notificadorSonido,
        Integer idSonidoMostrarMejoras,
        Integer idSonidoMejoraAplicada,
        Integer idSonidoVictoria,
        Integer idSonidoDerrota
    ) {
        if (cantidadOleadas <= 0 || maxEnemigosSimultaneos <= 0) {
            throw new IllegalArgumentException("La cantidad de oleadas y el máximo de enemigos deben ser positivos.");
        }
        if (!Double.isFinite(intervaloOleadas) || intervaloOleadas < 0) {
            throw new IllegalArgumentException("Los parámetros de configuración de la partida no son válidos.");
        }
        this.cantidadOleadas = cantidadOleadas;
        this.maxEnemigosSimultaneos = maxEnemigosSimultaneos;
        this.intervaloOleadas = intervaloOleadas;
        this.notificadorOleadas = Objects.requireNonNull(notificadorOleadas, "El notificador de oleadas no puede ser nulo");
        this.notificadorSonido = Objects.requireNonNull(notificadorSonido, "El notificador de sonido no puede ser nulo");
        this.idSonidoMostrarMejoras = idSonidoMostrarMejoras;
        this.idSonidoMejoraAplicada = idSonidoMejoraAplicada;
        this.idSonidoVictoria = idSonidoVictoria;
        this.idSonidoDerrota = idSonidoDerrota;

        this.oleadaActual = 0;
        this.enemigosActivos = 0;
        this.enemigosRestantesOleada = 0;
        this.enemigosPorAparecer = 0;
        this.tiempoHastaOleada = 0;
        this.ultimoSegundoPublicado = -1;
        this.oleadasCompletadas = false;
        this.enPausa = false;
        this.eleccionMejoraActiva = false;
        this.partidaFinalizada = false;
    }

    @Override
    public Notificador<EstadoOleadas> getNotificadorOleadas() {
        return notificadorOleadas;
    }

    @Override
    public Notificador<Integer> getNotificadorSonido() {
        return notificadorSonido;
    }

    @Override
    public boolean isEnPausa() {
        return enPausa;
    }

    @Override
    public void setEnPausa(boolean enPausa) {
        if (!partidaFinalizada) {
            this.enPausa = enPausa;
        }
    }

    @Override
    public boolean isEleccionMejoraActiva() {
        return eleccionMejoraActiva;
    }

    @Override
    public boolean isPartidaFinalizada() {
        return partidaFinalizada;
    }

    @Override
    public void finalizarPartida() {
        if (this.partidaFinalizada) {
            return;
        }
        this.partidaFinalizada = true;
        this.enPausa = false;
        this.eleccionMejoraActiva = false;
        emitirSonido(oleadasCompletadas ? idSonidoVictoria : idSonidoDerrota);
    }

    @Override
    public double calcularVelocidadEnemigoOleadaActual() {
        return 75 + Math.max(0, oleadaActual - 1) * 8.0;
    }

    @Override
    public int calcularEnemigosAGenerar() {
        int espaciosDisponibles = Math.max(0, maxEnemigosSimultaneos - enemigosActivos);
        return Math.min(espaciosDisponibles, enemigosPorAparecer);
    }

    @Override
    public void registrarEnemigoGenerado() {
        if (enemigosPorAparecer > 0) {
            enemigosPorAparecer--;
            enemigosActivos++;
            publicarEstadoOleadas((long) Math.ceil(tiempoHastaOleada));
        }
    }

    @Override
    public void registrarEnemigoDerrotado() {
        if (enemigosActivos > 0) {
            enemigosActivos--;
        }
        if (enemigosRestantesOleada > 0) {
            enemigosRestantesOleada--;
        }
        if (enemigosRestantesOleada == 0) {
            tiempoHastaOleada = intervaloOleadas;
            ultimoSegundoPublicado = -1;
        }
        publicarEstadoOleadas((long) Math.ceil(tiempoHastaOleada));
    }

    @Override
    public boolean actualizarOleadas(double dt) {
        if (oleadasCompletadas || partidaFinalizada) {
            return false;
        }

        if (oleadaActual == 0) {
            iniciarSiguienteOleada();
            return false;
        }

        if (enemigosRestantesOleada > 0) {
            return false;
        }

        tiempoHastaOleada -= dt;
        if (tiempoHastaOleada <= 0) {
            if (oleadaActual >= cantidadOleadas) {
                oleadasCompletadas = true;
                tiempoHastaOleada = 0;
                publicarEstadoOleadas(0);
                return true;
            }
            iniciarSiguienteOleada();
            return false;
        }

        long segundosRestantes = Math.max(0, (long) Math.ceil(tiempoHastaOleada));
        if (segundosRestantes != ultimoSegundoPublicado) {
            publicarEstadoOleadas(segundosRestantes);
            ultimoSegundoPublicado = segundosRestantes;
        }
        return false;
    }

    private void iniciarSiguienteOleada() {
        oleadaActual++;
        enemigosRestantesOleada = oleadaActual * maxEnemigosSimultaneos;
        enemigosPorAparecer = enemigosRestantesOleada;
        tiempoHastaOleada = 0;
        ultimoSegundoPublicado = -1;
        publicarEstadoOleadas(0);
    }

    private void publicarEstadoOleadas(long segundosRestantes) {
        notificadorOleadas.notificar(new EstadoOleadas(
            oleadaActual,
            enemigosActivos,
            segundosRestantes,
            oleadasCompletadas
        ));
    }

    private void emitirSonido(Integer idSonido) {
        if (idSonido != null) {
            notificadorSonido.notificar(idSonido);
        }
    }
}
