package controlador;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import modelo.Enemigo;
import modelo.ItemMejoraAtributo;
import modelo.Personaje;
import motor.EstadoOleadas;
import motor.Motor;
import motor.colisiones.hitboxes.CategoriaColision;
import motor.colisiones.hitboxes.HitboxRectangular;
import motor.colisiones.hitboxes.MascaraColision;
import motor.colisiones.hitboxes.TipoHitbox;
import motor.modelo.Entidad;
import motor.mvc.Controlador;
import motor.recursos.Extension.Imagen;
import motor.recursos.GestorRecursos;
import motor.util.VecDouble2D;
import motor.util.observer.Observador;
import vista.FondoMazmorra;
import vista.Animacion;
import vista.EnemigoVista;
import vista.ItemVista;
import vista.SpriteVista;
import vista.VistaJuego;

/**
 * Controlador principal de la partida.
 * Orquesta la interacción entre el Motor, los Modelos (Personaje, Enemigos, Items)
 * y las Vistas (VistaJuego, Sprites, Formas geométricas).
 */
public final class ControladorPartida {
    private final int cantidadOleadas = 5;
    private final int maxEnemigosSimultaneos = 6;
    private final double intervaloOleadas = 6.0;
    private final double probabilidadMejoraAtributo = 0.25;
    private final double aumentoVidaMaxima = 20.0;
    private final double distanciaMaxAtaque = 72.0;

    private final Motor motor;
    private final VistaJuego vistaJuego;
    private final FondoMazmorra fondoMazmorra;
    private final GestorRecursos gestorRecursos;
    private final Runnable onVolverAlMenu;
    private final Runnable onReintentar;

    private final List<Controlador> controladores = new CopyOnWriteArrayList<>();
    private final List<EnemigoControlador> enemigos = new CopyOnWriteArrayList<>();
    private final List<ExperienciaControlador> experiencias = new CopyOnWriteArrayList<>();
    private final List<MejoraAtributoControlador> mejorasAtributos = new CopyOnWriteArrayList<>();

    private Personaje personaje;
    private JugadorControlador jugadorControlador;

    private Image frameEnemigoIdle1;
    private Image frameEnemigoIdle2;
    private Image frameEnemigoGolpe;

    private int oleadaActual = 0;
    private int enemigosRestantesOleada = 0;
    private int enemigosPorAparecer = 0;
    private double tiempoHastaOleada = 0;
    private long ultimoSegundoPublicado = -1;
    private boolean oleadasCompletadas = false;
    private boolean enPausa = false;
    private boolean eleccionMejoraActiva = false;
    private boolean partidaFinalizada = false;
    private List<ItemMejoraAtributo> opcionesMejoraActuales = List.of();

    private final Observador<Double> observadorReloj;

    public ControladorPartida(
        Motor motor,
        VistaJuego vistaJuego,
        FondoMazmorra fondoMazmorra,
        GestorRecursos gestorRecursos,
        Runnable onVolverAlMenu,
        Runnable onReintentar
    ) {
        this.motor = Objects.requireNonNull(motor, "El motor no puede ser nulo");
        this.vistaJuego = Objects.requireNonNull(vistaJuego, "La vista del juego no puede ser nula");
        this.fondoMazmorra = Objects.requireNonNull(fondoMazmorra, "El fondo de mazmorra no puede ser nulo");
        this.gestorRecursos = Objects.requireNonNull(gestorRecursos, "El gestor de recursos no puede ser nulo");
        this.onVolverAlMenu = Objects.requireNonNull(onVolverAlMenu, "El callback de volver al menú es obligatorio");
        this.onReintentar = Objects.requireNonNull(onReintentar, "El callback de reintentar es obligatorio");

        this.observadorReloj = this::tick;
        inicializarRecursos();
        configurarVistas();
        inicializarJugador();

        this.motor.reloj().suscribirObservador(observadorReloj);
    }

    private void inicializarRecursos() {
        this.frameEnemigoIdle1 = gestorRecursos.getImagen("Transparent PNG/idle/frame-1", Imagen.PNG);
        this.frameEnemigoIdle2 = gestorRecursos.getImagen("Transparent PNG/idle/frame-2", Imagen.PNG);
        this.frameEnemigoGolpe = gestorRecursos.getImagen("Transparent PNG/got hit/frame", Imagen.PNG);
    }

    private void configurarVistas() {
        vistaJuego.configurarEventos(new VistaJuego.Eventos() {
            @Override
            public void solicitarPausa() {
                if (partidaFinalizada || enPausa) {
                    return;
                }
                enPausa = true;
                motor.detener();
                vistaJuego.mostrarPausa();
            }

            @Override
            public void solicitarReanudacion() {
                if (!enPausa) {
                    return;
                }
                enPausa = false;
                vistaJuego.cerrarPausa();
                if (!eleccionMejoraActiva && !partidaFinalizada) {
                    motor.iniciar();
                }
            }

            @Override
            public void solicitarVolverAlMenuDesdePausa() {
                volverAlMenu();
            }

            @Override
            public void seleccionarMejora(int seleccionIndice) {
                elegirMejora(seleccionIndice);
            }

            @Override
            public void solicitarReintento() {
                detener();
                onReintentar.run();
            }

            @Override
            public void solicitarVolverAlMenuDesdeFinPartida() {
                volverAlMenu();
            }

            private void volverAlMenu() {
                detener();
                onVolverAlMenu.run();
            }
        });
    }

    private void inicializarJugador() {
        Pane areaJuego = vistaJuego.getAreaJuego();

        Image spriteSheet = gestorRecursos.getImagen("zorro/zorro_sheet", Imagen.PNG);
        SpriteVista vistaJugador = new SpriteVista(spriteSheet, 4, 4, 50, 50);

        this.personaje = new Personaje.Builder(
            "jugador",
            new HitboxRectangular(
                new java.awt.geom.Rectangle2D.Double(0, 0, 30, 20),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.JUGADOR),
                MascaraColision.of(CategoriaColision.ENEMIGO, CategoriaColision.EXP, CategoriaColision.ITEM, CategoriaColision.MURO)
            )
        )
        .friccion(2500)
        .aceleracion(3000)
        .velocidadMax(240)
        .build();

        this.jugadorControlador = new JugadorControlador(
            personaje,
            vistaJugador,
            areaJuego::getWidth,
            areaJuego::getHeight,
            new JugadorControlador.LimitesArea(
                fondoMazmorra.getMargenHorizontalJugable(),
                fondoMazmorra.getMargenSuperiorJugable(),
                fondoMazmorra.getMargenInferiorJugable()
            ),
            p -> Platform.runLater(() -> {
                vistaJuego.actualizarVida(p.getVida(), p.getVidaMax());
                if (p.getVida() <= 0 && !partidaFinalizada) {
                    finalizarPartida("FIN DE LA PARTIDA");
                }
            }),
            this::atacarEnemigoMasCercano
        );

        controladores.add(jugadorControlador);
        motor.teclado().suscribir(jugadorControlador.getObservadorAcciones());
        motor.colisionables().add(personaje);
        areaJuego.getChildren().add(jugadorControlador.getVista());

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
        if (partidaFinalizada || personaje.getVida() <= 0) {
            return;
        }

        fondoMazmorra.actualizarAnimacion(dt);

        for (Controlador c : controladores) {
            c.tick(dt);
        }

        actualizarOleadas(dt);
        recogerExperiencias();
        recogerMejorasAtributos();
    }

    private void actualizarOleadas(double dt) {
        if (oleadasCompletadas) {
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
            if (oleadaActual >= cantidadOleadas) {
                oleadasCompletadas = true;
                finalizarPartida("¡VICTORIA! OLEADAS COMPLETADAS");
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
        enemigosRestantesOleada = oleadaActual * maxEnemigosSimultaneos;
        enemigosPorAparecer = enemigosRestantesOleada;
        tiempoHastaOleada = 0;
        ultimoSegundoPublicado = -1;

        generarEnemigosPendientes();
        publicarEstadoOleadas(0);
    }

    private void generarEnemigosPendientes() {
        Pane area = vistaJuego.getAreaJuego();
        double ancho = Math.max(area.getWidth(), 960);
        double alto = Math.max(area.getHeight(), 720);
        ThreadLocalRandom aleatorio = ThreadLocalRandom.current();
        int espaciosDisponibles = maxEnemigosSimultaneos - enemigos.size();
        int cantidadAGenerar = Math.min(espaciosDisponibles, enemigosPorAparecer);

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

            double velocidad = 75 + (oleadaActual - 1) * 8.0;

            Enemigo modeloEnemigo = new Enemigo(
                "enemigo",
                new HitboxRectangular(
                    new java.awt.geom.Rectangle2D.Double(0, 0, 32, 32),
                    TipoHitbox.SOLIDA,
                    MascaraColision.of(CategoriaColision.ENEMIGO),
                    MascaraColision.of(CategoriaColision.JUGADOR)
                ),
                new VecDouble2D(x, y),
                velocidad,
                50,
                10,
                10
            );

            EnemigoVista vistaEnemigo = new EnemigoVista(
                new Animacion(List.of(frameEnemigoIdle1, frameEnemigoIdle2), 0.35),
                frameEnemigoGolpe
            );

            EnemigoControlador controlador = new EnemigoControlador(
                modeloEnemigo,
                vistaEnemigo,
                personaje
            );

            enemigos.add(controlador);
            controladores.add(controlador);
            motor.colisionables().add(modeloEnemigo);
            enemigosPorAparecer--;
            Platform.runLater(() -> area.getChildren().add(vistaEnemigo));
        }
    }

    private void atacarEnemigoMasCercano() {
        if (partidaFinalizada) {
            return;
        }

        EnemigoControlador objetivoAtaque = null;
        double distanciaMinima = distanciaMaxAtaque;
        double jugadorX = personaje.getPosicion().getX() + 25;
        double jugadorY = personaje.getPosicion().getY() + 25;

        for (EnemigoControlador enemigo : enemigos) {
            double dx = enemigo.getModelo().getPosicion().getX() + 16 - jugadorX;
            double dy = enemigo.getModelo().getPosicion().getY() + 16 - jugadorY;
            double distancia = Math.hypot(dx, dy);
            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                objetivoAtaque = enemigo;
            }
        }

        if (objetivoAtaque != null && objetivoAtaque.recibirDanio(personaje.getDanioAtaque())) {
            EnemigoControlador derrotado = objetivoAtaque;
            double x = derrotado.getModelo().getPosicion().getX() + 16;
            double y = derrotado.getModelo().getPosicion().getY() + 16;

            enemigos.remove(derrotado);
            controladores.remove(derrotado);
            motor.colisionables().remove(derrotado.getModelo());
            Platform.runLater(() -> vistaJuego.getAreaJuego().getChildren().remove(derrotado.getVista()));

            crearGemaExperiencia(x, y, derrotado.getModelo().getExperienciaRecompensa());
            crearMejoraAleatoria(x, y);

            enemigosRestantesOleada--;
            generarEnemigosPendientes();

            if (enemigosRestantesOleada == 0) {
                tiempoHastaOleada = intervaloOleadas;
                ultimoSegundoPublicado = -1;
            }
            publicarEstadoOleadas((long) Math.ceil(tiempoHastaOleada));
        }
    }

    private void crearGemaExperiencia(double x, double y, int cantidad) {
        Entidad modeloExp = new Entidad(
            "experiencia",
            new HitboxRectangular(
                new java.awt.geom.Rectangle2D.Double(0, 0, 18, 18),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.EXP),
                MascaraColision.of(CategoriaColision.JUGADOR)
            ),
            new VecDouble2D(x, y),
            new VecDouble2D(),
            0,
            0,
            0
        );

        ItemVista vistaExp = ItemVista.crearExperiencia();

        ExperienciaControlador expControlador = new ExperienciaControlador(personaje, modeloExp, vistaExp, cantidad);
        experiencias.add(expControlador);
        controladores.add(expControlador);
        motor.colisionables().add(modeloExp);
        Platform.runLater(() -> vistaJuego.getAreaJuego().getChildren().add(vistaExp));
    }

    private void crearMejoraAleatoria(double x, double y) {
        if (ThreadLocalRandom.current().nextDouble() >= probabilidadMejoraAtributo) {
            return;
        }

        ItemMejoraAtributo mejora = new ItemMejoraAtributo(ItemMejoraAtributo.Atributo.VIDA_MAXIMA, aumentoVidaMaxima);
        Entidad modeloMejora = new Entidad(
            "mejora-atributo",
            new HitboxRectangular(
                new java.awt.geom.Rectangle2D.Double(0, 0, 22, 22),
                TipoHitbox.SOLIDA,
                MascaraColision.of(CategoriaColision.ITEM),
                MascaraColision.of(CategoriaColision.JUGADOR)
            ),
            new VecDouble2D(x, y),
            new VecDouble2D(),
            0,
            0,
            0
        );

        ItemVista vistaMejora = ItemVista.crearMejoraAtributo();

        MejoraAtributoControlador mejoraControlador = new MejoraAtributoControlador(personaje, mejora, modeloMejora, vistaMejora);
        mejorasAtributos.add(mejoraControlador);
        controladores.add(mejoraControlador);
        motor.colisionables().add(modeloMejora);
        Platform.runLater(() -> vistaJuego.getAreaJuego().getChildren().add(vistaMejora));
    }

    private void recogerExperiencias() {
        for (ExperienciaControlador exp : experiencias) {
            if (exp.fueRecogida()) {
                experiencias.remove(exp);
                controladores.remove(exp);
                motor.colisionables().remove(exp.getModelo());
                Platform.runLater(() -> vistaJuego.getAreaJuego().getChildren().remove(exp.getVista()));
                actualizarProgresoJugador();
            }
        }
        verificarSubidaNivel();
    }

    private void recogerMejorasAtributos() {
        for (MejoraAtributoControlador mejora : mejorasAtributos) {
            if (mejora.fueRecogida()) {
                mejorasAtributos.remove(mejora);
                controladores.remove(mejora);
                motor.colisionables().remove(mejora.getModelo());
                Platform.runLater(() -> vistaJuego.getAreaJuego().getChildren().remove(mejora.getVista()));
                actualizarProgresoJugador();
            }
        }
    }

    private void verificarSubidaNivel() {
        if (partidaFinalizada || eleccionMejoraActiva || !personaje.consumirNivelPendiente()) {
            return;
        }

        eleccionMejoraActiva = true;
        motor.detener();

        this.opcionesMejoraActuales = List.of(
            new ItemMejoraAtributo(ItemMejoraAtributo.Atributo.VIDA_MAXIMA, 20),
            new ItemMejoraAtributo(ItemMejoraAtributo.Atributo.DANIO_ATAQUE, 5),
            new ItemMejoraAtributo(ItemMejoraAtributo.Atributo.VELOCIDAD_MAXIMA, 10),
            new ItemMejoraAtributo(ItemMejoraAtributo.Atributo.DEFENSA, 5)
        );

        List<String> descripciones = opcionesMejoraActuales.stream()
            .map(ControladorPartida::formatearDescripcionMejora)
            .toList();

        int nivelActual = personaje.getNivel();
        Platform.runLater(() -> {
            if (!partidaFinalizada) {
                vistaJuego.mostrarEleccionMejora(nivelActual, descripciones);
            }
        });
    }

    private void elegirMejora(int seleccionIndice) {
        if (partidaFinalizada || !eleccionMejoraActiva) {
            return;
        }
        vistaJuego.cerrarEleccionMejora();
        if (seleccionIndice >= 0 && seleccionIndice < opcionesMejoraActuales.size()) {
            ItemMejoraAtributo elegida = opcionesMejoraActuales.get(seleccionIndice);
            elegida.aplicar(personaje);
        }
        opcionesMejoraActuales = List.of();
        eleccionMejoraActiva = false;
        actualizarProgresoJugador();
        verificarSubidaNivel();
        if (!eleccionMejoraActiva && !enPausa && !partidaFinalizada) {
            motor.iniciar();
        }
    }

    private void finalizarPartida(String mensaje) {
        if (partidaFinalizada) {
            return;
        }
        partidaFinalizada = true;
        enPausa = false;
        eleccionMejoraActiva = false;
        opcionesMejoraActuales = List.of();
        detener();
        Platform.runLater(() -> {
            vistaJuego.cerrarPausa();
            vistaJuego.cerrarEleccionMejora();
            vistaJuego.setPausaHabilitada(false);
            vistaJuego.mostrarFinPartida(mensaje);
        });
    }

    private static String formatearDescripcionMejora(ItemMejoraAtributo item) {
        return switch (item.atributo()) {
            case VIDA_MAXIMA -> "Vida máxima +" + (int) item.cantidad();
            case DANIO_ATAQUE -> "Daño +" + (int) item.cantidad();
            case VELOCIDAD_MAXIMA -> "Velocidad máxima +" + (int) item.cantidad() + "%";
            case DEFENSA -> "Defensa +" + (int) item.cantidad();
        };
    }

    private void publicarEstadoOleadas(long segundosRestantes) {
        EstadoOleadas estado = new EstadoOleadas(
            oleadaActual,
            enemigos.size(),
            segundosRestantes,
            oleadasCompletadas
        );
        Platform.runLater(() -> {
            vistaJuego.actualizarOleada(estado.oleada());
            vistaJuego.actualizarEnemigos(estado.enemigos());
            vistaJuego.actualizarTiempo(estado.segundosHastaOleada());
        });
    }

    public void iniciar() {
        motor.iniciar();
    }

    public void detener() {
        motor.reloj().desuscribirObservador(observadorReloj);
        motor.detener();
    }
}
