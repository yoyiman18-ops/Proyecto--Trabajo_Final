package modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;

public final class Partida implements ModeloPartida {
    private static final ItemMejoraAtributo.Atributo[] ATRIBUTOS_DISPONIBLES =
        ItemMejoraAtributo.Atributo.values();
    private static final int CANTIDAD_OPCIONES_MEJORA = 3;

    private final int cantidadOleadas;
    private final int maxEnemigosSimultaneos;
    private final double intervaloOleadas;
    private final Notificador<EstadoOleadas> notificadorOleadas;

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
    private List<ItemMejoraAtributo> opcionesMejoraActuales;

    public Partida() {
        this(5, 6, 6.0, new NotificadorDebil<>());
    }

    public Partida(
        int cantidadOleadas,
        int maxEnemigosSimultaneos,
        double intervaloOleadas,
        Notificador<EstadoOleadas> notificadorOleadas
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
        this.opcionesMejoraActuales = List.of();
    }

    @Override
    public Notificador<EstadoOleadas> getNotificadorOleadas() {
        return notificadorOleadas;
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
        this.partidaFinalizada = true;
        this.enPausa = false;
        this.eleccionMejoraActiva = false;
        this.opcionesMejoraActuales = List.of();
    }

    @Override
    public List<ItemMejoraAtributo> iniciarEleccionMejora() {
        this.eleccionMejoraActiva = true;
        ThreadLocalRandom aleatorio = ThreadLocalRandom.current();
        ItemMejoraAtributo.Atributo[] pool = ATRIBUTOS_DISPONIBLES.clone();

        int cantidadOpciones = Math.min(CANTIDAD_OPCIONES_MEJORA, pool.length);
        List<ItemMejoraAtributo> generadas = new ArrayList<>(cantidadOpciones);
        for (int i = 0; i < cantidadOpciones; i++) {
            int indiceElegido = aleatorio.nextInt(i, pool.length);
            ItemMejoraAtributo.Atributo atributo = pool[indiceElegido];
            pool[indiceElegido] = pool[i];

            ItemMejoraAtributo.Rareza rareza = ItemMejoraAtributo.Rareza.sortear(aleatorio.nextDouble());
            generadas.add(ItemMejoraAtributo.crear(atributo, rareza));
        }

        this.opcionesMejoraActuales = List.copyOf(generadas);
        return this.opcionesMejoraActuales;
    }

    @Override
    public ItemMejoraAtributo consumirOpcionMejora(int indice) {
        ItemMejoraAtributo seleccionada = null;
        if (indice >= 0 && indice < opcionesMejoraActuales.size()) {
            seleccionada = opcionesMejoraActuales.get(indice);
        }
        this.opcionesMejoraActuales = List.of();
        this.eleccionMejoraActiva = false;
        return seleccionada;
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
}
