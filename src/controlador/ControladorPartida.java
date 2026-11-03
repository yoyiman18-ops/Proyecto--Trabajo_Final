package controlador;

import java.awt.geom.Rectangle2D;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import modelo.EstadoOleadas;
import modelo.Jugador;
import modelo.ModeloPartida;
import modelo.enemigo.Enemigo;
import modelo.enemigo.EsqueletoFactory;
import modelo.enemigo.SpawnerEnemigos;
import motor.Motor;
import motor.entrada.Accion;
import motor.entrada.EstadoAcciones;
import motor.entrada.ReceptorEstadoAcciones;
import motor.modelo.Ticking;
import motor.util.VecDouble2D;
import motor.util.observer.Observador;
import vista.OpcionMejoraVisual;
import vista.VistaFondo;
import vista.VistaPartida;

public final class ControladorPartida implements ReceptorEstadoAcciones, Ticking {

    private static final double BONUS_DANIO_ATAQUE = 5.0;
    private static final double BONUS_VIDA_MAXIMA = 20.0;
    private static final double BONUS_VELOCIDAD_ATAQUE_PORCENTUAL = 15.0;
    private static final double BONUS_DEFENSA_PORCENTUAL = 10.0;

    private static final List<OpcionMejoraVisual> OPCIONES_MEJORA = List.of(
        new OpcionMejoraVisual("RARA", "Poder de Ataque", "+5 Daño", "Incrementa el daño base de tus proyectiles.", "danio"),
        new OpcionMejoraVisual("RARA", "Vitalidad", "+20 Vida Máx.", "Aumenta tu vida máxima y restaura vitalidad.", "corazon"),
        new OpcionMejoraVisual("ÉPICA", "Cadencia Arcana", "+15% Vel. Ataque", "Reduce el tiempo de enfriamiento entre disparos.", "tiempo"),
        new OpcionMejoraVisual("ÉPICA", "Armadura Rúnica", "+10% Defensa", "Reduce el daño recibido de los enemigos.", "defensa")
    );

    private final ModeloPartida modeloPartida;
    private final ControladorVistaJuego controladorVista;
    private final Motor motor;
    private final Observador<EstadoAcciones> observadorAcciones;
    private final Observador<Double> observadorTick;
    private final Observador<Jugador> observadorVidaJugador;
    private final Observador<Integer> observadorExpiracionJugador;
    private final Observador<EstadoOleadas> observadorOleadas;
    private final Observador<Enemigo> observadorEnemigoGenerado;

    private EstadoAcciones estadoAccionesActual;
    private int ultimaExperienciaSincronizada;
    private int ultimoNivelSincronizado;
    private int ultimaExperienciaSiguienteSincronizada;

    public ControladorPartida(
        ModeloPartida modeloPartida,
        ControladorVistaJuego controladorVista,
        Motor motor
    ) {
        this.modeloPartida = Objects.requireNonNull(modeloPartida, "El modelo de la partida no puede ser nulo");
        this.controladorVista = Objects.requireNonNull(controladorVista, "El controlador de vista no puede ser nulo");
        this.motor = Objects.requireNonNull(motor, "El motor no puede ser nulo");
        this.observadorAcciones = this::actualizarEstadoAcciones;
        this.observadorTick = this::tick;
        this.observadorVidaJugador = this::alModificarVidaJugador;
        this.observadorExpiracionJugador = this::alExpirarJugador;
        this.observadorOleadas = this::alActualizarOleadas;
        this.observadorEnemigoGenerado = this::alGenerarEnemigo;
        this.estadoAccionesActual = new EstadoAcciones(EnumSet.noneOf(Accion.class));
        this.ultimaExperienciaSincronizada = -1;
        this.ultimoNivelSincronizado = -1;
        this.ultimaExperienciaSiguienteSincronizada = -1;
    }

    public void iniciar(
        Scene escena,
        Runnable onVolverAlMenu,
        Runnable onReintentar
    ) {
        Objects.requireNonNull(escena, "La escena no puede ser nula");
        Objects.requireNonNull(onVolverAlMenu, "La acción de volver al menú no puede ser nula");
        Objects.requireNonNull(onReintentar, "La acción de reintentar no puede ser nula");

        SonidoJuego.precargarTodos(motor.audio());

        VistaPartida vista = controladorVista.getVistaJuego();

        Pane rootMotor = motor.getRoot();
        rootMotor.setPickOnBounds(false);
        vista.agregarNodoJuego(rootMotor);

        motor.reloj().suscribirObservador(controladorVista.getObservadorActualizacion());
        motor.reloj().suscribirObservador(observadorTick);
        motor.teclado().suscribir(observadorAcciones);

        modeloPartida.getNotificadorOleadas().suscribirObservador(observadorOleadas);
        modeloPartida.getNotificadorSonido().suscribirObservador(motor.audio().getObservadorSonido());
        modeloPartida.getNotificadorEnemigoGenerado().suscribirObservador(observadorEnemigoGenerado);

        SpawnerEnemigos spawner = modeloPartida.getSpawnerEnemigos();
        if (spawner != null) {
            double anchoArea = vista.getAnchoAreaJuego();
            double altoArea = vista.getAltoAreaJuego();
            if (anchoArea > 0 && altoArea > 0) {
                VistaFondo fondo = controladorVista.getFondoMazmorra();
                double mH = fondo != null ? fondo.getMargenHorizontalJugable() : 0.0;
                double mSup = fondo != null ? fondo.getMargenSuperiorJugable() : 0.0;
                double mInf = fondo != null ? fondo.getMargenInferiorJugable() : 0.0;
                spawner.configurarLimites(
                    mH,
                    Math.max(mH, anchoArea - mH - EsqueletoFactory.ANCHO_HITBOX_ESQUELETO),
                    mSup,
                    Math.max(mSup, altoArea - mInf - EsqueletoFactory.ALTO_HITBOX_ESQUELETO)
                );
            }
        }

        controladorVista.configurarControlesModales(
            motor,
            modeloPartida,
            () -> {
                desuscribirObservadores();
                onVolverAlMenu.run();
            },
            () -> {
                desuscribirObservadores();
                onReintentar.run();
            },
            this::aplicarMejoraSeleccionada
        );

        controladorVista.configurarAtajoTecladoPausa(escena, motor, modeloPartida);
        motor.teclado().setEscenaRoot(escena);

        Jugador jugador = modeloPartida.getJugador();
        if (jugador != null) {
            motor.teclado().suscribir(jugador.getObservadorEstadoAcciones());
            jugador.getNotificadorVida().suscribirObservador(observadorVidaJugador);
            jugador.getNotificadorExpiracion().suscribirObservador(observadorExpiracionJugador);
            sincronizarHudInicial(jugador);
            motor.ensamblarEntidad(jugador);
        }

        Platform.runLater(vista::solicitarFoco);
        motor.iniciar();
    }

    @Override
    public Observador<EstadoAcciones> getObservadorAcciones() {
        return observadorAcciones;
    }

    @Override
    public void tick(double dt) {
        if (modeloPartida.isEnPausa() || modeloPartida.isPartidaFinalizada()) {
            return;
        }
        Jugador jugador = modeloPartida.getJugador();
        if (jugador == null || !jugador.activo()) {
            return;
        }
        modeloPartida.actualizarOleadas(dt);
        procesarAtaqueJugador(jugador);
        limitarPosicionJugador(jugador);
        sincronizarProgresoJugador(jugador);
    }

    private void alGenerarEnemigo(Enemigo enemigo) {
        if (enemigo != null) {
            motor.ensamblarEntidad(enemigo);
        }
    }

    private void actualizarEstadoAcciones(EstadoAcciones estado) {
        if (estado != null) {
            this.estadoAccionesActual = estado;
        }
    }

    private void procesarAtaqueJugador(Jugador jugador) {
        if (estadoAccionesActual.activa(Accion.ATACAR)) {
            jugador.atacar();
        }
    }

    private void limitarPosicionJugador(Jugador jugador) {
        VistaPartida vista = controladorVista.getVistaJuego();
        double anchoArea = vista.getAnchoAreaJuego();
        double altoArea = vista.getAltoAreaJuego();
        if (anchoArea <= 0 || altoArea <= 0) {
            return;
        }
        VistaFondo fondo = controladorVista.getFondoMazmorra();
        double margenHorizontal = fondo != null ? fondo.getMargenHorizontalJugable() : 0.0;
        double margenSuperior = fondo != null ? fondo.getMargenSuperiorJugable() : 0.0;
        double margenInferior = fondo != null ? fondo.getMargenInferiorJugable() : 0.0;

        Rectangle2D bounds = jugador.getHitbox().getBounds();
        double minX = margenHorizontal;
        double maxX = Math.max(minX, anchoArea - margenHorizontal - bounds.getWidth());
        double minY = margenSuperior;
        double maxY = Math.max(minY, altoArea - margenInferior - bounds.getHeight());

        VecDouble2D posicion = jugador.getPosicion();
        double x = Math.clamp(posicion.getX(), minX, maxX);
        double y = Math.clamp(posicion.getY(), minY, maxY);
        if (x != posicion.getX() || y != posicion.getY()) {
            if (x != posicion.getX()) {
                jugador.getVelocidad().setX(0.0);
            }
            if (y != posicion.getY()) {
                jugador.getVelocidad().setY(0.0);
            }
            jugador.setPosicion(new VecDouble2D(x, y));
        }
    }

    private void sincronizarHudInicial(Jugador jugador) {
        VistaPartida vista = controladorVista.getVistaJuego();
        vista.setPausaHabilitada(true);
        vista.actualizarVida(jugador.getVida(), jugador.getVidaMax());
        vista.actualizarDanio((int) Math.round(jugador.getDanioAtaque()));
        vista.actualizarOleada(0);
        vista.actualizarEnemigos(0);
        vista.actualizarTiempo(0);
        sincronizarProgresoJugador(jugador);
    }

    private void sincronizarProgresoJugador(Jugador jugador) {
        int nivel = jugador.getNivel();
        int experiencia = jugador.getExperiencia();
        int experienciaSiguiente = jugador.getExperienciaSiguiente();
        if (nivel != ultimoNivelSincronizado
            || experiencia != ultimaExperienciaSincronizada
            || experienciaSiguiente != ultimaExperienciaSiguienteSincronizada) {
            ultimoNivelSincronizado = nivel;
            ultimaExperienciaSincronizada = experiencia;
            ultimaExperienciaSiguienteSincronizada = experienciaSiguiente;
            controladorVista.getVistaJuego().actualizarProgreso(nivel, experiencia, experienciaSiguiente);
        }
        if (jugador.getNivelesPendientes() > 0
            && !modeloPartida.isEleccionMejoraActiva()
            && !modeloPartida.isPartidaFinalizada()) {
            abrirModalMejora(jugador);
        }
    }

    private void abrirModalMejora(Jugador jugador) {
        modeloPartida.iniciarEleccionMejora();
        controladorVista.setEnPausa(true);
        motor.setPausa(true);
        controladorVista.mostrarEleccionMejora(jugador.getNivel(), OPCIONES_MEJORA);
    }

    private void aplicarMejoraSeleccionada(int indice) {
        Jugador jugador = modeloPartida.getJugador();
        if (jugador == null || modeloPartida.isPartidaFinalizada()) {
            return;
        }
        switch (indice) {
            case 0 -> jugador.aumentarDanioAtaque(BONUS_DANIO_ATAQUE);
            case 1 -> jugador.aumentarVidaMaxima(BONUS_VIDA_MAXIMA);
            case 2 -> jugador.aumentarVelocidadAtaque(BONUS_VELOCIDAD_ATAQUE_PORCENTUAL);
            case 3 -> jugador.aumentarDefensa(BONUS_DEFENSA_PORCENTUAL);
            default -> {
                return;
            }
        }
        jugador.consumirNivelPendiente();
        modeloPartida.confirmarEleccionMejora();

        VistaPartida vista = controladorVista.getVistaJuego();
        vista.actualizarVida(jugador.getVida(), jugador.getVidaMax());
        vista.actualizarDanio((int) Math.round(jugador.getDanioAtaque()));
        sincronizarProgresoJugador(jugador);

        if (jugador.getNivelesPendientes() > 0 && !modeloPartida.isPartidaFinalizada()) {
            abrirModalMejora(jugador);
        } else {
            controladorVista.cerrarEleccionMejora();
            controladorVista.setEnPausa(false);
            motor.setPausa(false);
            Platform.runLater(vista::solicitarFoco);
        }
    }

    private void alModificarVidaJugador(Jugador jugador) {
        if (jugador != null) {
            controladorVista.getVistaJuego().actualizarVida(jugador.getVida(), jugador.getVidaMax());
        }
    }

    private void alExpirarJugador(Integer estado) {
        if (modeloPartida.isPartidaFinalizada()) {
            return;
        }
        modeloPartida.finalizarPartida();
        controladorVista.cerrarPausa();
        controladorVista.cerrarEleccionMejora();
        controladorVista.setEnPausa(true);
        motor.setPausa(true);
        VistaPartida vista = controladorVista.getVistaJuego();
        vista.setPausaHabilitada(false);
        controladorVista.mostrarFinPartida("DERROTA");
    }

    private void alActualizarOleadas(EstadoOleadas estado) {
        if (estado == null) {
            return;
        }
        VistaPartida vista = controladorVista.getVistaJuego();
        vista.actualizarOleada(estado.oleada());
        vista.actualizarEnemigos(estado.enemigos());
        vista.actualizarTiempo(estado.segundosHastaOleada());
        if (estado.completadas() && !modeloPartida.isPartidaFinalizada()) {
            modeloPartida.finalizarPartida();
            controladorVista.cerrarPausa();
            controladorVista.cerrarEleccionMejora();
            controladorVista.setEnPausa(true);
            motor.setPausa(true);
            vista.setPausaHabilitada(false);
            controladorVista.mostrarFinPartida("¡VICTORIA!");
        }
    }

    private void desuscribirObservadores() {
        motor.reloj().desuscribirObservador(controladorVista.getObservadorActualizacion());
        motor.reloj().desuscribirObservador(observadorTick);
        motor.teclado().desuscribir(observadorAcciones);
        modeloPartida.getNotificadorOleadas().desuscribirObservador(observadorOleadas);
        modeloPartida.getNotificadorSonido().desuscribirObservador(motor.audio().getObservadorSonido());
        modeloPartida.getNotificadorEnemigoGenerado().desuscribirObservador(observadorEnemigoGenerado);
        Jugador jugador = modeloPartida.getJugador();
        if (jugador != null) {
            motor.teclado().desuscribir(jugador.getObservadorEstadoAcciones());
            jugador.getNotificadorVida().desuscribirObservador(observadorVidaJugador);
            jugador.getNotificadorExpiracion().desuscribirObservador(observadorExpiracionJugador);
        }
    }

    public ModeloPartida getModeloPartida() {
        return modeloPartida;
    }

    public ControladorVistaJuego getControladorVista() {
        return controladorVista;
    }

    public Motor getMotor() {
        return motor;
    }
}
