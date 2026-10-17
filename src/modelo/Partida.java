package modelo;

import java.util.List;
import java.util.Objects;

import motor.EstadoOleadas;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;

public final class Partida implements ModeloPartida {
    private final int cantidadOleadas;
    private final int maxEnemigosSimultaneos;
    private final double intervaloOleadas;
    private final double probabilidadMejoraAtributo;
    private final double aumentoVidaMaxima;
    private final double distanciaMaxAtaque;
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
        this(5, 6, 6.0, 0.25, 20.0, 72.0, new NotificadorDebil<>());
    }

    public Partida(
        int cantidadOleadas,
        int maxEnemigosSimultaneos,
        double intervaloOleadas,
        double probabilidadMejoraAtributo,
        double aumentoVidaMaxima,
        double distanciaMaxAtaque,
        Notificador<EstadoOleadas> notificadorOleadas
    ) {
        if (cantidadOleadas <= 0 || maxEnemigosSimultaneos <= 0) {
            throw new IllegalArgumentException("La cantidad de oleadas y el máximo de enemigos deben ser positivos.");
        }
        if (!Double.isFinite(intervaloOleadas) || intervaloOleadas < 0
            || !Double.isFinite(probabilidadMejoraAtributo) || probabilidadMejoraAtributo < 0 || probabilidadMejoraAtributo > 1
            || !Double.isFinite(aumentoVidaMaxima) || aumentoVidaMaxima <= 0
            || !Double.isFinite(distanciaMaxAtaque) || distanciaMaxAtaque <= 0) {
            throw new IllegalArgumentException("Los parámetros de configuración de la partida no son válidos.");
        }
        this.cantidadOleadas = cantidadOleadas;
        this.maxEnemigosSimultaneos = maxEnemigosSimultaneos;
        this.intervaloOleadas = intervaloOleadas;
        this.probabilidadMejoraAtributo = probabilidadMejoraAtributo;
        this.aumentoVidaMaxima = aumentoVidaMaxima;
        this.distanciaMaxAtaque = distanciaMaxAtaque;
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
    public int getOleadaActual() {
        return oleadaActual;
    }

    @Override
    public int getCantidadOleadas() {
        return cantidadOleadas;
    }

    @Override
    public int getMaxEnemigosSimultaneos() {
        return maxEnemigosSimultaneos;
    }

    @Override
    public int getEnemigosActivos() {
        return enemigosActivos;
    }

    @Override
    public int getEnemigosRestantesOleada() {
        return enemigosRestantesOleada;
    }

    @Override
    public int getEnemigosPorAparecer() {
        return enemigosPorAparecer;
    }

    @Override
    public double getProbabilidadMejoraAtributo() {
        return probabilidadMejoraAtributo;
    }

    @Override
    public double getAumentoVidaMaxima() {
        return aumentoVidaMaxima;
    }

    @Override
    public double getDistanciaMaxAtaque() {
        return distanciaMaxAtaque;
    }

    @Override
    public boolean isOleadasCompletadas() {
        return oleadasCompletadas;
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
    public List<ItemMejoraAtributo> getOpcionesMejoraActuales() {
        return opcionesMejoraActuales;
    }

    @Override
    public List<ItemMejoraAtributo> iniciarEleccionMejora() {
        this.eleccionMejoraActiva = true;
        this.opcionesMejoraActuales = List.of(
            new ItemMejoraAtributo(ItemMejoraAtributo.Atributo.VIDA_MAXIMA, 20),
            new ItemMejoraAtributo(ItemMejoraAtributo.Atributo.DANIO_ATAQUE, 5),
            new ItemMejoraAtributo(ItemMejoraAtributo.Atributo.VELOCIDAD_MAXIMA, 10),
            new ItemMejoraAtributo(ItemMejoraAtributo.Atributo.DEFENSA, 5)
        );
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
