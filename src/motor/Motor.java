package motor;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import controlador.EnemigoControlador;
import controlador.ExperienciaControlador;
import controlador.JugadorControlador;
import controlador.MejoraAtributoControlador;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import modelo.ItemMejoraAtributo;
import modelo.Personaje;
import motor.colisiones.Colisionable;
import motor.colisiones.SistemaColisiones;
import motor.colisiones.sistema.FaseEspecificaSimple;
import motor.colisiones.sistema.FaseGeneralSpatialHashGrid;
import motor.entrada.GestorEntradaTeclado;
import motor.mvc.Controlador;
import motor.recursos.GestorRecursos;
import motor.util.RelojDelta;
import motor.util.observer.NotificadorFuerte;
import motor.util.observer.Observador;

public class Motor {
    private static final int CANTIDAD_OLEADAS = 5;
    private static final int MAX_ENEMIGOS_SIMULTANEOS = 5;
    private static final double INTERVALO_OLEADAS = 10;
    private static final double PROBABILIDAD_MEJORA_ATRIBUTO = 0.2;
    private static final double AUMENTO_VIDA_MAXIMA = 20;

    private final SistemaColisiones sistemaColisiones;
    private final GestorRecursos gestorRecursos;
    private final GestorEntradaTeclado gestorEntradaTeclado;
    private final RelojDelta relojDelta;
    private final Pane root;
    private final CopyOnWriteArrayList<Controlador> controladores;
    private final CopyOnWriteArrayList<Colisionable> colisionables;
    private final CopyOnWriteArrayList<EnemigoControlador> enemigos;
    private final CopyOnWriteArrayList<ExperienciaControlador> experiencias;
    private final CopyOnWriteArrayList<MejoraAtributoControlador> mejorasAtributos;
    private Personaje jugador;
    private Observador<Personaje> observadorProgreso = personaje -> {};
    private Observador<Personaje> observadorSubidaNivel = personaje -> {};
    private Observador<EstadoOleadas> observadorOleadas = estado -> {};
    private int oleadaActual;
    private int enemigosRestantesOleada;
    private int enemigosPorAparecer;
    private double tiempoHastaOleada;
    private long ultimoSegundoPublicado = -1;
    private boolean oleadasCompletadas;
    private boolean eleccionMejoraActiva;

    /**
     * Crear motor default.
     */
    public Motor() {
        this(new Pane());
    }

    public Motor(Pane root) {
        this(
            new SistemaColisiones(new FaseGeneralSpatialHashGrid(100), new FaseEspecificaSimple()),
            new GestorRecursos(),
            root
        );
    }

    public Motor(
        SistemaColisiones sistemaColisiones,
        GestorRecursos gestorRecursos,
        Pane root
        ) {
        this.sistemaColisiones = sistemaColisiones;
        this.gestorRecursos = gestorRecursos;
        this.relojDelta = new RelojDelta(new NotificadorFuerte<>());
        this.root = root;
        this.gestorEntradaTeclado = new GestorEntradaTeclado();
        this.colisionables = new CopyOnWriteArrayList<>();
        this.controladores = new CopyOnWriteArrayList<>();
        this.enemigos = new CopyOnWriteArrayList<>();
        this.experiencias = new CopyOnWriteArrayList<>();
        this.mejorasAtributos = new CopyOnWriteArrayList<>();
        
        relojDelta.suscribirObservador(e -> {
            this.gestorEntradaTeclado.tick();
            actualizarOleadas(e);
            this.sistemaColisiones.resolverColisiones(colisionables);
            for (Controlador c : controladores) { c.tick(e); }
            recogerExperiencias();
            recogerMejorasAtributos();
        });
    }

    public void instanciarJugador(String nombre) {
        instanciarJugador(nombre, personaje -> {});
    }

    public void instanciarJugador(String nombre, Observador<Personaje> observadorVida) {
        instanciarJugador(nombre, observadorVida, estado -> {});
    }

    public void instanciarJugador(
        String nombre,
        Observador<Personaje> observadorVida,
        Observador<EstadoOleadas> observadorOleadas
    ) {
        instanciarJugador(nombre, observadorVida, observadorOleadas, personaje -> {});
    }

    public void instanciarJugador(
        String nombre,
        Observador<Personaje> observadorVida,
        Observador<EstadoOleadas> observadorOleadas,
        Observador<Personaje> observadorProgreso
    ) {
        instanciarJugador(nombre, observadorVida, observadorOleadas, observadorProgreso, personaje -> {});
    }

    public void instanciarJugador(
        String nombre,
        Observador<Personaje> observadorVida,
        Observador<EstadoOleadas> observadorOleadas,
        Observador<Personaje> observadorProgreso,
        Observador<Personaje> observadorSubidaNivel
    ) {
        JugadorControlador c = new JugadorControlador(
            nombre,
            gestorRecursos,
            observadorVida,
            this::atacarEnemigoMasCercano
        );
        this.jugador = c.getPersonaje();
        this.observadorOleadas = observadorOleadas;
        this.observadorProgreso = observadorProgreso;
        this.observadorSubidaNivel = observadorSubidaNivel;
        this.oleadaActual = 0;
        this.enemigosRestantesOleada = 0;
        this.enemigosPorAparecer = 0;
        this.tiempoHastaOleada = 0;
        this.ultimoSegundoPublicado = -1;
        this.oleadasCompletadas = false;
        this.eleccionMejoraActiva = false;
        this.enemigos.clear();
        this.experiencias.clear();
        this.mejorasAtributos.clear();
        this.controladores.add(c);
        this.gestorEntradaTeclado.suscribir(c.getObservadorAcciones());
        Platform.runLater(() -> root.getChildren().add(c.getVista()));
        this.observadorProgreso.cambio(jugador);
    }

    private void actualizarOleadas(double dt) {
        if (jugador == null || jugador.getVida() <= 0 || oleadasCompletadas) {
            return;
        }
        if (oleadaActual == 0) {
            iniciarSiguienteOleada();
            return;
        }
        if (enemigosRestantesOleada > 0) {
            return;
        }

        tiempoHastaOleada -= dt;
        if (tiempoHastaOleada <= 0) {
            if (oleadaActual >= CANTIDAD_OLEADAS) {
                oleadasCompletadas = true;
                publicarEstadoOleadas(0);
                return;
            }
            iniciarSiguienteOleada();
        }

        long segundosRestantes = Math.max(0, (long) Math.ceil(tiempoHastaOleada));
        if (segundosRestantes != ultimoSegundoPublicado) {
            publicarEstadoOleadas(segundosRestantes);
            ultimoSegundoPublicado = segundosRestantes;
        }
    }

    private void iniciarSiguienteOleada() {
        oleadaActual++;
        enemigosRestantesOleada = oleadaActual * MAX_ENEMIGOS_SIMULTANEOS;
        enemigosPorAparecer = enemigosRestantesOleada;
        tiempoHastaOleada = 0;
        ultimoSegundoPublicado = -1;

        generarEnemigosPendientes();
        publicarEstadoOleadas(0);
    }

    private void generarEnemigosPendientes() {
        double ancho = Math.max(root.getWidth(), 960);
        double alto = Math.max(root.getHeight(), 720);
        ThreadLocalRandom aleatorio = ThreadLocalRandom.current();
        int espaciosDisponibles = MAX_ENEMIGOS_SIMULTANEOS - enemigos.size();
        int cantidadAGenerar = Math.min(espaciosDisponibles, enemigosPorAparecer);
        for (int indice = 0; indice < cantidadAGenerar; indice++) {
            double x;
            double y;
            if (aleatorio.nextBoolean()) {
                x = ancho - 55;
                y = aleatorio.nextDouble(80, alto - 40);
            } else {
                x = aleatorio.nextDouble(100, ancho - 40);
                y = alto - 55;
            }

            double velocidad = 75 + (oleadaActual - 1) * 8;
            EnemigoControlador enemigo = new EnemigoControlador(jugador, x, y, velocidad);
            enemigos.add(enemigo);
            controladores.add(enemigo);
            enemigosPorAparecer--;
            Platform.runLater(() -> root.getChildren().add(enemigo.getVista()));
        }
    }

    private void atacarEnemigoMasCercano() {
        EnemigoControlador objetivoAtaque = null;
        double distanciaMinima = 72;
        double jugadorX = jugador.getPosicion().getX() + 25;
        double jugadorY = jugador.getPosicion().getY() + 25;

        for (EnemigoControlador enemigo : enemigos) {
            double dx = enemigo.getModelo().getPosicion().getX() + 16 - jugadorX;
            double dy = enemigo.getModelo().getPosicion().getY() + 16 - jugadorY;
            double distancia = Math.hypot(dx, dy);
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                objetivoAtaque = enemigo;
            }
        }

        if (objetivoAtaque != null && objetivoAtaque.recibirDanio(jugador.getDanioAtaque())) {
            EnemigoControlador enemigoDerrotado = objetivoAtaque;
            double x = enemigoDerrotado.getModelo().getPosicion().getX() + 16;
            double y = enemigoDerrotado.getModelo().getPosicion().getY() + 16;
            enemigos.remove(enemigoDerrotado);
            controladores.remove(enemigoDerrotado);
            Platform.runLater(() -> root.getChildren().remove(enemigoDerrotado.getVista()));
            ExperienciaControlador experiencia = new ExperienciaControlador(
                jugador,
                x,
                y,
                enemigoDerrotado.getModelo().getExperienciaRecompensa()
            );
            experiencias.add(experiencia);
            controladores.add(experiencia);
            Platform.runLater(() -> root.getChildren().add(experiencia.getVista()));
            generarMejoraAtributo(x, y);
            enemigosRestantesOleada--;
            generarEnemigosPendientes();
            if (enemigosRestantesOleada == 0) {
                tiempoHastaOleada = INTERVALO_OLEADAS;
                ultimoSegundoPublicado = -1;
            }
            publicarEstadoOleadas((long) Math.ceil(tiempoHastaOleada));
        }
    }

    private void recogerExperiencias() {
        for (ExperienciaControlador experiencia : experiencias) {
            if (experiencia.fueRecogida()) {
                experiencias.remove(experiencia);
                controladores.remove(experiencia);
                Platform.runLater(() -> root.getChildren().remove(experiencia.getVista()));
                observadorProgreso.cambio(jugador);
            }
        }
        mostrarSiguienteMejoraSiPendiente();
    }

    private void mostrarSiguienteMejoraSiPendiente() {
        if (eleccionMejoraActiva || !jugador.consumirNivelPendiente()) {
            return;
        }
        eleccionMejoraActiva = true;
        relojDelta.detener();
        observadorSubidaNivel.cambio(jugador);
    }

    public void continuarDespuesDeMejora() {
        if (!eleccionMejoraActiva) {
            return;
        }
        eleccionMejoraActiva = false;
        if (jugador.consumirNivelPendiente()) {
            eleccionMejoraActiva = true;
            observadorSubidaNivel.cambio(jugador);
        } else {
            relojDelta.iniciar();
        }
    }

    private void generarMejoraAtributo(double x, double y) {
        if (ThreadLocalRandom.current().nextDouble() >= PROBABILIDAD_MEJORA_ATRIBUTO) {
            return;
        }
        MejoraAtributoControlador mejora = new MejoraAtributoControlador(
            jugador,
            new ItemMejoraAtributo(ItemMejoraAtributo.Atributo.VIDA_MAXIMA, AUMENTO_VIDA_MAXIMA),
            x,
            y
        );
        mejorasAtributos.add(mejora);
        controladores.add(mejora);
        Platform.runLater(() -> root.getChildren().add(mejora.getVista()));
    }

    private void recogerMejorasAtributos() {
        for (MejoraAtributoControlador mejora : mejorasAtributos) {
            if (mejora.fueRecogida()) {
                mejorasAtributos.remove(mejora);
                controladores.remove(mejora);
                Platform.runLater(() -> root.getChildren().remove(mejora.getVista()));
            }
        }
    }

    private void publicarEstadoOleadas(long segundosRestantes) {
        observadorOleadas.cambio(new EstadoOleadas(
            oleadaActual,
            enemigos.size(),
            segundosRestantes,
            oleadasCompletadas
        ));
    }

    public GestorRecursos recursos() { return this.gestorRecursos; }
    public GestorEntradaTeclado teclado() { return this.gestorEntradaTeclado; }
    public Pane root() { return this.root; }
    public void iniciar() { this.relojDelta.iniciar(); }
    public void detener() { this.relojDelta.detener(); }
}


