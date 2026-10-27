package controlador;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javafx.application.Platform;
import modelo.ItemMejoraAtributo;
import modelo.ModeloJugador;
import modelo.ModeloPartida;
import modelo.Partida;
import modelo.EstadoOleadas;
import motor.Motor;
import motor.mvc.Controlador;
import motor.mvc.ControladorUsuario;
import motor.recursos.GestorRecursos;
import motor.util.VecDouble2D;
import motor.util.observer.Observador;
import vista.VistaFondo;
import vista.OpcionMejoraVisual;
import vista.VistaPartida;

/**
 * Controlador principal de la partida.
 * Orquesta la interacción entre el Motor, los Modelos (ModeloPartida, ModeloJugador, ModeloEnemigo, Items)
 * y las Vistas (VistaPartida, VistaFondo) mediante Inversión de Dependencias.
 */
public final class ControladorPartida {
    private final Motor motor;
    private final ModeloPartida modeloPartida;
    private final VistaPartida vistaJuego;
    private final VistaFondo fondoMazmorra;
    private final FabricaEntidadesPartida fabricaEntidades;
    private final Runnable onVolverAlMenu;
    private final Runnable onReintentar;

    private final List<Controlador> controladores;
    private final List<EnemigoControlador> enemigos;
    private final List<ProyectilControlador> proyectiles;
    private final List<ExperienciaControlador> experiencias;

    private ModeloJugador personaje;
    private ControladorUsuario jugadorControlador;

    private final Observador<Double> observadorReloj;
    private final Observador<EstadoOleadas> observadorOleadas;

    public ControladorPartida(
        Motor motor,
        VistaPartida vistaJuego,
        VistaFondo fondoMazmorra,
        GestorRecursos gestorRecursos,
        Runnable onVolverAlMenu,
        Runnable onReintentar
    ) {
        this(
            motor,
            new Partida(),
            vistaJuego,
            fondoMazmorra,
            new FabricaEntidadesPartidaPorDefecto(
                Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo")
            ),
            onVolverAlMenu,
            onReintentar
        );
    }

    public ControladorPartida(
        Motor motor,
        ModeloPartida modeloPartida,
        VistaPartida vistaJuego,
        VistaFondo fondoMazmorra,
        GestorRecursos gestorRecursos,
        Runnable onVolverAlMenu,
        Runnable onReintentar
    ) {
        this(
            motor,
            modeloPartida,
            vistaJuego,
            fondoMazmorra,
            new FabricaEntidadesPartidaPorDefecto(
                Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo")
            ),
            onVolverAlMenu,
            onReintentar
        );
    }

    public ControladorPartida(
        Motor motor,
        ModeloPartida modeloPartida,
        VistaPartida vistaJuego,
        VistaFondo fondoMazmorra,
        FabricaEntidadesPartida fabricaEntidades,
        Runnable onVolverAlMenu,
        Runnable onReintentar
    ) {
        this.motor = Objects.requireNonNull(motor, "El motor no puede ser nulo");
        this.modeloPartida = Objects.requireNonNull(modeloPartida, "El modelo de la partida no puede ser nulo");
        this.vistaJuego = Objects.requireNonNull(vistaJuego, "La vista del juego no puede ser nula");
        this.fondoMazmorra = Objects.requireNonNull(fondoMazmorra, "El fondo de mazmorra no puede ser nulo");
        this.fabricaEntidades = Objects.requireNonNull(fabricaEntidades, "La fábrica de entidades no puede ser nula");
        this.onVolverAlMenu = Objects.requireNonNull(onVolverAlMenu, "El callback de volver al menú es obligatorio");
        this.onReintentar = Objects.requireNonNull(onReintentar, "El callback de reintentar es obligatorio");

        this.controladores = new CopyOnWriteArrayList<>();
        this.enemigos = new CopyOnWriteArrayList<>();
        this.proyectiles = new CopyOnWriteArrayList<>();
        this.experiencias = new CopyOnWriteArrayList<>();

        this.observadorReloj = this::tick;
        this.observadorOleadas = estado -> Platform.runLater(() -> {
            this.vistaJuego.actualizarOleada(estado.oleada());
            this.vistaJuego.actualizarEnemigos(estado.enemigos());
            this.vistaJuego.actualizarTiempo(estado.segundosHastaOleada());
        });

        this.modeloPartida.getNotificadorOleadas().suscribirObservador(observadorOleadas);

        configurarVistas();
        inicializarJugador();

        this.motor.reloj().suscribirObservador(observadorReloj);
    }

    private void configurarVistas() {
        vistaJuego.setOnPausa(this::solicitarPausa);
        vistaJuego.setOnReanudar(this::solicitarReanudacion);
        vistaJuego.setOnVolverAlMenuDesdePausa(this::volverAlMenu);
        vistaJuego.setOnSeleccionarMejora(this::elegirMejora);
        vistaJuego.setOnReintentar(this::reintentarPartida);
        vistaJuego.setOnVolverAlMenuDesdeFinPartida(this::volverAlMenu);
    }

    private void solicitarPausa() {
        if (modeloPartida.isPartidaFinalizada() || modeloPartida.isEnPausa()) {
            return;
        }
        modeloPartida.setEnPausa(true);
        motor.detener();
        vistaJuego.mostrarPausa();
    }

    private void solicitarReanudacion() {
        if (!modeloPartida.isEnPausa()) {
            return;
        }
        modeloPartida.setEnPausa(false);
        vistaJuego.cerrarPausa();
        if (!modeloPartida.isEleccionMejoraActiva() && !modeloPartida.isPartidaFinalizada()) {
            motor.iniciar();
        }
    }

    private void reintentarPartida() {
        detener();
        onReintentar.run();
    }

    private void volverAlMenu() {
        detener();
        onVolverAlMenu.run();
    }

    private void inicializarJugador() {
        this.personaje = fabricaEntidades.crearModeloJugador();

        this.jugadorControlador = fabricaEntidades.crearControladorJugador(
            personaje,
            vistaJuego::getAnchoAreaJuego,
            vistaJuego::getAltoAreaJuego,
            new JugadorControlador.LimitesArea(
                fondoMazmorra.getMargenHorizontalJugable(),
                fondoMazmorra.getMargenSuperiorJugable(),
                fondoMazmorra.getMargenInferiorJugable()
            ),
            p -> Platform.runLater(() -> {
                vistaJuego.actualizarVida(p.getVida(), p.getVidaMax());
                if (p.getVida() <= 0 && !modeloPartida.isPartidaFinalizada()) {
                    finalizarPartida("FIN DE LA PARTIDA");
                }
            }),
            this::dispararProyectil
        );

        controladores.add(jugadorControlador);
        motor.teclado().suscribir(jugadorControlador.getObservadorAcciones());
        motor.colisionables().add(personaje.getEntidad());
        vistaJuego.agregarNodoJuego(jugadorControlador.getVista());

        actualizarProgresoJugador();
    }

    private void actualizarProgresoJugador() {
        Platform.runLater(() -> {
            vistaJuego.actualizarVida(personaje.getVida(), personaje.getVidaMax());
            vistaJuego.actualizarProgreso(
                personaje.getNivel(),
                personaje.getExperiencia(),
                personaje.getExperienciaSiguiente()
            );
            vistaJuego.actualizarDanio((int) Math.round(personaje.getDanioAtaque()));
        });
    }

    public void tick(Double dt) {
        if (modeloPartida.isPartidaFinalizada() || personaje.getVida() <= 0) {
            return;
        }

        fondoMazmorra.actualizarAnimacion(dt);

        for (Controlador c : controladores) {
            c.tick(dt);
        }

        actualizarProyectiles();

        if (modeloPartida.actualizarOleadas(dt)) {
            finalizarPartida("¡VICTORIA! OLEADAS COMPLETADAS");
            return;
        }
        generarEnemigosPendientes();
        recogerExperiencias();
    }

    private void generarEnemigosPendientes() {
        int cantidadAGenerar = modeloPartida.calcularEnemigosAGenerar();
        if (cantidadAGenerar <= 0) {
            return;
        }

        double ancho = Math.max(vistaJuego.getAnchoAreaJuego(), 960);
        double alto = Math.max(vistaJuego.getAltoAreaJuego(), 720);
        ThreadLocalRandom aleatorio = ThreadLocalRandom.current();

        for (int i = 0; i < cantidadAGenerar; i++) {
            double x;
            double y;
            if (aleatorio.nextBoolean()) {
                x = ancho - 55;
                y = aleatorio.nextDouble(80, Math.max(81, alto - 40));
            } else {
                x = aleatorio.nextDouble(100, Math.max(101, ancho - 40));
                y = alto - 55;
            }

            double velocidad = modeloPartida.calcularVelocidadEnemigoOleadaActual();
            EnemigoControlador controlador = fabricaEntidades.crearEnemigo(x, y, velocidad, personaje);

            enemigos.add(controlador);
            controladores.add(controlador);
            motor.colisionables().add(controlador.getModelo());
            modeloPartida.registrarEnemigoGenerado();
            Platform.runLater(() -> vistaJuego.agregarNodoJuego(controlador.getVista()));
        }
    }

    private void dispararProyectil() {
        if (modeloPartida.isPartidaFinalizada() || !personaje.intentarAtacar()) {
            return;
        }

        ThreadLocalRandom aleatorio = ThreadLocalRandom.current();
        VecDouble2D direccionBase = personaje.getVectorDireccion();
        boolean esMultidisparo = aleatorio.nextDouble(100) < personaje.getProbabilidadMultidisparo();
        double[] angulos = esMultidisparo ? new double[] { -18.0, 0.0, 18.0 } : new double[] { 0.0 };

        for (double angulo : angulos) {
            VecDouble2D direccionDisparo = rotarVector(direccionBase, angulo);
            boolean esCritico = aleatorio.nextDouble(100) < personaje.getProbabilidadCritico();
            double danioFinal = esCritico
                ? personaje.getDanioAtaque() * 2.0
                : personaje.getDanioAtaque();

            double origenX = personaje.getPosicion().getX() + 17 + direccionDisparo.getX() * 16;
            double origenY = personaje.getPosicion().getY() + 17 + direccionDisparo.getY() * 16;

            ProyectilControlador proyectil = fabricaEntidades.crearProyectil(
                origenX,
                origenY,
                direccionDisparo,
                danioFinal,
                esCritico
            );

            proyectiles.add(proyectil);
            controladores.add(proyectil);
            motor.colisionables().add(proyectil.getModelo());
            Platform.runLater(() -> vistaJuego.agregarNodoJuego(proyectil.getVista()));
        }
    }

    private VecDouble2D rotarVector(VecDouble2D vector, double grados) {
        if (grados == 0) {
            return vector.clone();
        }
        double radianes = Math.toRadians(grados);
        double cos = Math.cos(radianes);
        double sin = Math.sin(radianes);
        double x = vector.getX() * cos - vector.getY() * sin;
        double y = vector.getX() * sin + vector.getY() * cos;
        return new VecDouble2D(x, y);
    }

    private void actualizarProyectiles() {
        for (ProyectilControlador proyectil : proyectiles) {
            if (proyectil.estaActivo()) {
                for (EnemigoControlador enemigo : enemigos) {
                    if (proyectil.getModelo().getHitbox().intersecta(enemigo.getModelo().getHitbox())) {
                        proyectil.registrarImpacto();
                        aplicarDanioEnemigo(enemigo, proyectil.getDanio());
                        break;
                    }
                }
            }

            if (!proyectil.estaActivo()) {
                removerProyectil(proyectil);
            }
        }
    }

    private void removerProyectil(ProyectilControlador proyectil) {
        proyectiles.remove(proyectil);
        controladores.remove(proyectil);
        motor.colisionables().remove(proyectil.getModelo());
        Platform.runLater(() -> vistaJuego.removerNodoJuego(proyectil.getVista()));
    }

    private void aplicarDanioEnemigo(EnemigoControlador enemigo, double danio) {
        if (enemigo.recibirDanio(danio)) {
            double x = enemigo.getModelo().getPosicion().getX() + 16;
            double y = enemigo.getModelo().getPosicion().getY() + 16;

            enemigos.remove(enemigo);
            controladores.remove(enemigo);
            motor.colisionables().remove(enemigo.getModelo());
            Platform.runLater(() -> vistaJuego.removerNodoJuego(enemigo.getVista()));

            crearGemaExperiencia(x, y, enemigo.getExperienciaRecompensa());

            modeloPartida.registrarEnemigoDerrotado();
            generarEnemigosPendientes();
        }
    }

    private void crearGemaExperiencia(double x, double y, int cantidad) {
        ExperienciaControlador expControlador = fabricaEntidades.crearExperiencia(personaje, x, y, cantidad);
        experiencias.add(expControlador);
        controladores.add(expControlador);
        motor.colisionables().add(expControlador.getModelo());
        Platform.runLater(() -> vistaJuego.agregarNodoJuego(expControlador.getVista()));
    }

    private void recogerExperiencias() {
        for (ExperienciaControlador exp : experiencias) {
            if (exp.fueRecogida()) {
                experiencias.remove(exp);
                controladores.remove(exp);
                motor.colisionables().remove(exp.getModelo());
                Platform.runLater(() -> vistaJuego.removerNodoJuego(exp.getVista()));
                actualizarProgresoJugador();
            }
        }
        verificarSubidaNivel();
    }

    private void verificarSubidaNivel() {
        if (modeloPartida.isPartidaFinalizada()
            || modeloPartida.isEleccionMejoraActiva()
            || !personaje.consumirNivelPendiente()) {
            return;
        }

        motor.detener();

        List<ItemMejoraAtributo> opciones = modeloPartida.iniciarEleccionMejora();
        List<OpcionMejoraVisual> opcionesVisuales = opciones.stream()
            .map(this::mapearOpcionMejora)
            .toList();

        int nivelActual = personaje.getNivel();
        Platform.runLater(() -> {
            if (!modeloPartida.isPartidaFinalizada()) {
                vistaJuego.mostrarEleccionMejora(nivelActual, opcionesVisuales);
            }
        });
    }

    private void elegirMejora(int seleccionIndice) {
        if (modeloPartida.isPartidaFinalizada() || !modeloPartida.isEleccionMejoraActiva()) {
            return;
        }
        vistaJuego.cerrarEleccionMejora();
        ItemMejoraAtributo elegida = modeloPartida.consumirOpcionMejora(seleccionIndice);
        if (elegida != null) {
            elegida.aplicar(personaje);
        }
        actualizarProgresoJugador();
        verificarSubidaNivel();
        if (!modeloPartida.isEleccionMejoraActiva()
            && !modeloPartida.isEnPausa()
            && !modeloPartida.isPartidaFinalizada()) {
            motor.iniciar();
        }
    }

    private void finalizarPartida(String mensaje) {
        if (modeloPartida.isPartidaFinalizada()) {
            return;
        }
        modeloPartida.finalizarPartida();
        detener();
        Platform.runLater(() -> {
            vistaJuego.cerrarPausa();
            vistaJuego.cerrarEleccionMejora();
            vistaJuego.setPausaHabilitada(false);
            vistaJuego.mostrarFinPartida(mensaje);
        });
    }

    private OpcionMejoraVisual mapearOpcionMejora(ItemMejoraAtributo item) {
        String rareza = switch (item.rareza()) {
            case COMUN -> "COMÚN";
            case RARA -> "RARA";
            case EPICA -> "ÉPICA";
            case LEGENDARIA -> "LEGENDARIA";
        };
        String nombre = switch (item.atributo()) {
            case VIDA_MAXIMA -> "Vida máxima";
            case DANIO_ATAQUE -> "Daño";
            case VELOCIDAD_ATAQUE -> "Vel. Ataque";
            case VELOCIDAD_MAXIMA -> "Velocidad";
            case DEFENSA -> "Defensa";
            case PROBABILIDAD_CRITICO -> "Crítico";
            case PROBABILIDAD_MULTIDISPARO -> "Multidisparo";
        };
        String bonus = switch (item.atributo()) {
            case VIDA_MAXIMA, DANIO_ATAQUE -> "+" + (int) item.cantidad();
            default -> "+" + (int) item.cantidad() + "%";
        };
        String descripcion = switch (item.atributo()) {
            case VIDA_MAXIMA -> "Aumenta y restaura tu vitalidad";
            case DANIO_ATAQUE -> "Mayor impacto por proyectil";
            case VELOCIDAD_ATAQUE -> "Disparás con mayor frecuencia";
            case VELOCIDAD_MAXIMA -> "Mayor agilidad en la mazmorra";
            case DEFENSA -> "Reduce el daño recibido";
            case PROBABILIDAD_CRITICO -> "Chance de disparo carmesí x2 daño";
            case PROBABILIDAD_MULTIDISPARO -> "Chance de lanzar 3 orbes en abanico";
        };
        String claveIcono = switch (item.atributo()) {
            case VIDA_MAXIMA -> "corazon";
            case DANIO_ATAQUE, PROBABILIDAD_CRITICO -> "danio";
            case VELOCIDAD_MAXIMA -> "tiempo";
            case DEFENSA -> "defensa";
            case VELOCIDAD_ATAQUE, PROBABILIDAD_MULTIDISPARO -> "experiencia";
        };
        return new OpcionMejoraVisual(rareza, nombre, bonus, descripcion, claveIcono);
    }

    public void iniciar() {
        motor.iniciar();
    }

    public void detener() {
        modeloPartida.getNotificadorOleadas().desuscribirObservador(observadorOleadas);
        motor.reloj().desuscribirObservador(observadorReloj);
        motor.detener();
    }
}
