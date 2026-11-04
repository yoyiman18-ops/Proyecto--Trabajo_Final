package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import modelo.enemigo.Enemigo;
import modelo.enemigo.SpawnerEnemigos;
import motor.util.Contador;
import motor.util.observer.Notificador;

public final class Partida implements ModeloPartida {
    private final Jugador jugador;
    private final int cantidadOleadas;
    private final int maxEnemigosSimultaneos;
    private final double velocidadBaseEnemigos;
    private final double incrementoVelocidadOleada;
    private final Contador contadorEntreOleadas;
    private final Notificador<EstadoOleadas> notificadorOleadas;
    private final Notificador<Integer> notificadorSonido;
    private final Notificador<Enemigo> notificadorEnemigoGenerado;
    private final SpawnerEnemigos spawnerEnemigos;
    private final Integer idSonidoMostrarMejoras;
    private final Integer idSonidoMejoraAplicada;
    private final Integer idSonidoVictoria;
    private final Integer idSonidoDerrota;
    private final List<Enemigo> enemigos;

    private int oleadaActual;
    private int enemigosActivos;
    private int enemigosRestantesOleada;
    private int enemigosPorAparecer;
    private long ultimoSegundoPublicado;
    private boolean oleadasCompletadas;
    private boolean enPausa;
    private boolean eleccionMejoraActiva;
    private boolean partidaFinalizada;

    public Partida(
        Jugador jugador,
        SpawnerEnemigos spawnerEnemigos,
        int cantidadOleadas,
        int maxEnemigosSimultaneos,
        double intervaloOleadas,
        double velocidadBaseEnemigos,
        double incrementoVelocidadOleada,
        Notificador<EstadoOleadas> notificadorOleadas,
        Notificador<Integer> notificadorSonido,
        Notificador<Enemigo> notificadorEnemigoGenerado,
        Integer idSonidoMostrarMejoras,
        Integer idSonidoMejoraAplicada,
        Integer idSonidoVictoria,
        Integer idSonidoDerrota
    ) {
        if (cantidadOleadas <= 0 || maxEnemigosSimultaneos <= 0) {
            throw new IllegalArgumentException("La cantidad de oleadas y el máximo de enemigos deben ser positivos.");
        }
        if (!Double.isFinite(intervaloOleadas) || intervaloOleadas < 0
            || !Double.isFinite(velocidadBaseEnemigos) || velocidadBaseEnemigos <= 0
            || !Double.isFinite(incrementoVelocidadOleada) || incrementoVelocidadOleada < 0) {
            throw new IllegalArgumentException("Los parámetros de configuración de la partida no son válidos.");
        }
        this.jugador = Objects.requireNonNull(jugador, "El jugador no puede ser nulo");
        this.spawnerEnemigos = Objects.requireNonNull(spawnerEnemigos, "El spawner de enemigos no puede ser nulo");
        this.cantidadOleadas = cantidadOleadas;
        this.maxEnemigosSimultaneos = maxEnemigosSimultaneos;
        this.velocidadBaseEnemigos = velocidadBaseEnemigos;
        this.incrementoVelocidadOleada = incrementoVelocidadOleada;
        this.contadorEntreOleadas = new Contador(intervaloOleadas);
        this.notificadorOleadas = Objects.requireNonNull(notificadorOleadas, "El notificador de oleadas no puede ser nulo");
        this.notificadorSonido = Objects.requireNonNull(notificadorSonido, "El notificador de sonido no puede ser nulo");
        this.notificadorEnemigoGenerado = Objects.requireNonNull(notificadorEnemigoGenerado, "El notificador de enemigo generado no puede ser nulo");
        this.spawnerEnemigos.getNotificadorEnemigoGenerado().suscribirObservador(this::alGenerarEnemigo);
        this.idSonidoMostrarMejoras = idSonidoMostrarMejoras;
        this.idSonidoMejoraAplicada = idSonidoMejoraAplicada;
        this.idSonidoVictoria = idSonidoVictoria;
        this.idSonidoDerrota = idSonidoDerrota;
        this.enemigos = new ArrayList<>();

        this.oleadaActual = 0;
        this.enemigosActivos = 0;
        this.enemigosRestantesOleada = 0;
        this.enemigosPorAparecer = 0;
        this.ultimoSegundoPublicado = -1;
        this.oleadasCompletadas = false;
        this.enPausa = false;
        this.eleccionMejoraActiva = false;
        this.partidaFinalizada = false;
    }

    @Override
    public Jugador getJugador() {
        return jugador;
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
    public Notificador<Enemigo> getNotificadorEnemigoGenerado() {
        return notificadorEnemigoGenerado;
    }

    @Override
    public SpawnerEnemigos getSpawnerEnemigos() {
        return spawnerEnemigos;
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
    public void iniciarEleccionMejora() {
        if (!partidaFinalizada) {
            this.eleccionMejoraActiva = true;
            this.enPausa = true;
            emitirSonido(idSonidoMostrarMejoras);
        }
    }

    @Override
    public void confirmarEleccionMejora() {
        if (!partidaFinalizada && this.eleccionMejoraActiva) {
            this.eleccionMejoraActiva = false;
            this.enPausa = false;
            emitirSonido(idSonidoMejoraAplicada);
        }
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
    public boolean actualizarOleadas(double dt) {
        if (oleadasCompletadas || partidaFinalizada) {
            return false;
        }

        if (oleadaActual == 0) {
            iniciarSiguienteOleada();
            return false;
        }

        if (enemigosPorAparecer > 0) {
            int cuposDisponibles = maxEnemigosSimultaneos - enemigosActivos;
            if (cuposDisponibles > 0) {
                spawnerEnemigos.actualizar(
                    dt,
                    Math.min(cuposDisponibles, enemigosPorAparecer),
                    velocidadBaseEnemigos + Math.max(0, oleadaActual - 1) * incrementoVelocidadOleada,
                    jugador
                );
            }
        }

        if (enemigosRestantesOleada > 0) {
            return false;
        }

        contadorEntreOleadas.tick(dt);
        if (contadorEntreOleadas.estaListo()) {
            if (oleadaActual >= cantidadOleadas) {
                oleadasCompletadas = true;
                publicarEstadoOleadas(0);
                return true;
            }
            iniciarSiguienteOleada();
            return false;
        }

        long segundosRestantes = Math.max(0, (long) Math.ceil(contadorEntreOleadas.getTiempoRestante()));
        if (segundosRestantes != ultimoSegundoPublicado) {
            publicarEstadoOleadas(segundosRestantes);
            ultimoSegundoPublicado = segundosRestantes;
        }
        return false;
    }

    private void alGenerarEnemigo(Enemigo enemigo) {
        if (enemigo == null || enemigosPorAparecer <= 0 || enemigosActivos >= maxEnemigosSimultaneos || enemigos.contains(enemigo)) {
            return;
        }
        enemigos.add(enemigo);
        enemigosPorAparecer--;
        enemigosActivos++;
        publicarEstadoOleadas((long) Math.ceil(contadorEntreOleadas.getTiempoRestante()));
        enemigo.getNotificadorExpiracion().suscribirObservador(e -> registrarEnemigoDerrotado(enemigo));
        notificadorEnemigoGenerado.notificar(enemigo);
    }

    private void registrarEnemigoDerrotado(Enemigo enemigo) {
        if (enemigo == null || !enemigos.remove(enemigo)) {
            return;
        }
        if (enemigosActivos > 0) {
            enemigosActivos--;
        }
        if (enemigosRestantesOleada > 0) {
            enemigosRestantesOleada--;
        }
        if (enemigosRestantesOleada == 0) {
            contadorEntreOleadas.reiniciar();
            ultimoSegundoPublicado = -1;
        }
        publicarEstadoOleadas((long) Math.ceil(contadorEntreOleadas.getTiempoRestante()));
    }

    private void iniciarSiguienteOleada() {
        oleadaActual++;
        enemigosRestantesOleada = oleadaActual * maxEnemigosSimultaneos;
        enemigosPorAparecer = enemigosRestantesOleada;
        contadorEntreOleadas.reset();
        ultimoSegundoPublicado = -1;
        spawnerEnemigos.reiniciar();
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
