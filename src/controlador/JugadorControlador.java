package controlador;

import java.util.EnumSet;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.beans.value.ChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import modelo.EntidadMovil;
import modelo.Vec2;
import vista.SpriteVista;
import javafx.stage.Window;
import javafx.scene.layout.Pane;
import motor.cache.CacheImagenes;
import javafx.scene.control.Label;

/**
 * Controla el movimiento del jugador mediante el teclado.
 *
 * <p>El controlador no crea ni destruye entidades: recibe el modelo y la vista
 * que debe actualizar. El método {@link #actualizar()} debe invocarse una vez
 * por frame.</p>
 */
public class JugadorControlador {

    private final EntidadMovil modelo;
    private final SpriteVista vista;
    private final List<EnemigoControlador> enemigos;
    private final List<ProyectilControlador> proyectiles;
    private final Pane escenario;
    private final CacheImagenes cache;
    private final List<ExperienciaControlador> experiencias;
    private final Label progreso;
    private final Set<KeyCode> teclasPresionadas = EnumSet.noneOf(KeyCode.class);
    private final EventHandler<KeyEvent> manejadorPresionar = this::alPresionarTecla;
    private final EventHandler<KeyEvent> manejadorSoltar = this::alSoltarTecla;
    private final ChangeListener<Boolean> listenerFoco = (propiedad, teniaFoco, tieneFoco) -> {
        if (!tieneFoco) {
            teclasPresionadas.clear();
        }
    };

    private Scene escena;
    private Window ventana;
    private long ultimoAtaque;
    private long cooldownAtaqueNs = 300_000_000L;
    private static final double ALCANCE_ATAQUE = 75.0;
    private int daño = 5;
    private int nivel = 1;
    private int experiencia;
    private int experienciaSiguiente = 30;
    private boolean eligiendoMejora;

    public JugadorControlador(EntidadMovil modelo, SpriteVista vista) {
        this(modelo, vista, List.of(), new ArrayList<>(), null, null, new ArrayList<>(), null);
    }

    public JugadorControlador(EntidadMovil modelo, SpriteVista vista,
                              List<EnemigoControlador> enemigos) {
        this(modelo, vista, enemigos, new ArrayList<>(), null, null, new ArrayList<>(), null);
    }

    public JugadorControlador(EntidadMovil modelo, SpriteVista vista,
                              List<EnemigoControlador> enemigos,
                              List<ProyectilControlador> proyectiles,
                              Pane escenario, CacheImagenes cache,
                              List<ExperienciaControlador> experiencias,
                              Label progreso) {
        if (modelo == null) {
            throw new IllegalArgumentException("El modelo del jugador no puede ser null");
        }
        if (vista == null) {
            throw new IllegalArgumentException("La vista del jugador no puede ser null");
        }
        this.modelo = modelo;
        this.vista = vista;
        this.enemigos = enemigos;
        this.proyectiles = proyectiles;
        this.escenario = escenario;
        this.cache = cache;
        this.experiencias = experiencias;
        this.progreso = progreso;
        vista.actualizar(modelo);
        actualizarProgreso();
    }

    /**
     * Registra los manejadores de teclado en la escena indicada.
     * Si ya estaba conectado a otra escena, primero la desconecta.
     */
    public void iniciar(Scene escena) {
        if (escena == null) {
            throw new IllegalArgumentException("La escena no puede ser null");
        }
        if (escena.getWindow() == null) {
            throw new IllegalStateException("La escena debe estar asociada a una ventana antes de iniciar el controlador");
        }
        detener();
        this.escena = escena;
        this.ventana = escena.getWindow();
        escena.addEventHandler(KeyEvent.KEY_PRESSED, manejadorPresionar);
        escena.addEventHandler(KeyEvent.KEY_RELEASED, manejadorSoltar);
        ventana.focusedProperty().addListener(listenerFoco);
    }

    /** Desconecta el controlador de la escena y libera las teclas retenidas. */
    public void detener() {
        if (escena != null) {
            escena.removeEventHandler(KeyEvent.KEY_PRESSED, manejadorPresionar);
            escena.removeEventHandler(KeyEvent.KEY_RELEASED, manejadorSoltar);
            ventana.focusedProperty().removeListener(listenerFoco);
            escena = null;
            ventana = null;
        }
        teclasPresionadas.clear();
    }

    /** Actualiza el modelo y la vista una vez por frame. */
    public void actualizar() {
        if (modelo instanceof modelo.EntidadViva entidadViva && !entidadViva.estaVivo()) {
            return;
        }
        recogerExperiencia();
        double direccionX = 0;
        double direccionY = 0;

        if (teclaPresionada(KeyCode.A) || teclaPresionada(KeyCode.LEFT)) {
            direccionX -= 1;
        }
        if (teclaPresionada(KeyCode.D) || teclaPresionada(KeyCode.RIGHT)) {
            direccionX += 1;
        }
        if (teclaPresionada(KeyCode.W) || teclaPresionada(KeyCode.UP)) {
            direccionY -= 1;
        }
        if (teclaPresionada(KeyCode.S) || teclaPresionada(KeyCode.DOWN)) {
            direccionY += 1;
        }

        if (direccionX != 0 || direccionY != 0) {
            modelo.setDireccion(direccionX, direccionY);
            modelo.acelerar();
        } else {
            modelo.frenar();
        }

        if (teclaPresionada(KeyCode.SPACE) && !eligiendoMejora) {
            atacar();
        }

        modelo.mover();
        vista.actualizar(modelo);
    }

    private void atacar() {
        long ahora = System.nanoTime();
        if (ahora - ultimoAtaque < cooldownAtaqueNs) {
            return;
        }

        if (escenario == null || cache == null || !(modelo instanceof modelo.EntidadViva)) {
            return;
        }

        EnemigoControlador objetivo = null;
        double distanciaMenor = ALCANCE_ATAQUE;
        for (EnemigoControlador enemigo : enemigos) {
            if (enemigo.estaVivo()) {
                double distancia = enemigo.distanciaAl(modelo);
                if (distancia < distanciaMenor) {
                    distanciaMenor = distancia;
                    objetivo = enemigo;
                }
            }
        }

        if (objetivo != null) {
            Vec2 jugador = modelo.getPosicion();
            Vec2 enemigo = objetivo.getModelo().getPosicion();
            Point2D direccion = new Point2D(
                    enemigo.getX() - jugador.getX(),
                    enemigo.getY() - jugador.getY()
            );
            SpriteVista vistaProyectil = new SpriteVista(
                    cache, "disparo", new javafx.geometry.Rectangle2D(0, 0, 360, 360), 20, 20
            );
            escenario.getChildren().add(vistaProyectil);
            proyectiles.add(new ProyectilControlador(
                    jugador.getX(), jugador.getY(),
                    direccion.getX(), direccion.getY(), daño, vistaProyectil
            ));
            ultimoAtaque = ahora;
        }
    }

    private void recogerExperiencia() {
            for (ExperienciaControlador experienciaControlador : experiencias) {
                if (experienciaControlador.actualizar(modelo)) {
                    experiencia += experienciaControlador.getCantidad();
                    comprobarSubidaNivel();
                }
            }
        }

        private void comprobarSubidaNivel() {
            if (experiencia < experienciaSiguiente || eligiendoMejora) {
                actualizarProgreso();
                return;
            }
            experiencia -= experienciaSiguiente;
            nivel++;
            experienciaSiguiente += 20;
            eligiendoMejora = true;
            actualizarProgreso();
        }

        private void elegirMejora(KeyCode tecla) {
            if (!eligiendoMejora) {
                return;
            }
            if (tecla == KeyCode.DIGIT1) {
                daño += 2;
            } else if (tecla == KeyCode.DIGIT2) {
                cooldownAtaqueNs = Math.max(100_000_000L, cooldownAtaqueNs - 50_000_000L);
            } else if (tecla == KeyCode.DIGIT3 && modelo instanceof modelo.EntidadViva jugador) {
                jugador.mejorarDefensa(1);
            } else {
                return;
            }
            eligiendoMejora = false;
            comprobarSubidaNivel();
            actualizarProgreso();
        }

        private void actualizarProgreso() {
            if (progreso == null) {
                return;
            }
            if (eligiendoMejora) {
                progreso.setText("Nivel " + nivel + " | XP " + experiencia + "/" + experienciaSiguiente
                        + "\nMEJORA: [1] Daño  [2] Vel. ataque  [3] Defensa");
            } else {
                progreso.setText("Nivel " + nivel + " | XP " + experiencia + "/" + experienciaSiguiente
                        + " | Daño: " + daño);
            }
        }

    public EntidadMovil getModelo() {
        return modelo;
    }

    public SpriteVista getVista() {
        return vista;
    }

    private boolean teclaPresionada(KeyCode tecla) {
        return teclasPresionadas.contains(tecla);
    }

    private void alPresionarTecla(KeyEvent evento) {
        elegirMejora(evento.getCode());
        if (esTeclaDeMovimiento(evento.getCode())) {
            teclasPresionadas.add(evento.getCode());
            evento.consume();
        }
    }

    private void alSoltarTecla(KeyEvent evento) {
        if (esTeclaDeMovimiento(evento.getCode())) {
            teclasPresionadas.remove(evento.getCode());
            evento.consume();
        }
    }

    private boolean esTeclaDeMovimiento(KeyCode tecla) {
        return tecla == KeyCode.W || tecla == KeyCode.A
                || tecla == KeyCode.S || tecla == KeyCode.D
                || tecla == KeyCode.UP || tecla == KeyCode.DOWN
                || tecla == KeyCode.LEFT || tecla == KeyCode.RIGHT
                || tecla == KeyCode.SPACE;
    }
}
