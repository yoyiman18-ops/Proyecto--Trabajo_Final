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
    private static final long COOLDOWN_ATAQUE_NS = 300_000_000L;
    private static final double ALCANCE_ATAQUE = 75.0;

    public JugadorControlador(EntidadMovil modelo, SpriteVista vista) {
        this(modelo, vista, List.of(), new ArrayList<>(), null, null);
    }

    public JugadorControlador(EntidadMovil modelo, SpriteVista vista,
                              List<EnemigoControlador> enemigos) {
        this(modelo, vista, enemigos, new ArrayList<>(), null, null);
    }

    public JugadorControlador(EntidadMovil modelo, SpriteVista vista,
                              List<EnemigoControlador> enemigos,
                              List<ProyectilControlador> proyectiles,
                              Pane escenario, CacheImagenes cache) {
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
        vista.actualizar(modelo);
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

        if (teclaPresionada(KeyCode.SPACE)) {
            atacar();
        }

        modelo.mover();
        vista.actualizar(modelo);
    }

    private void atacar() {
        long ahora = System.nanoTime();
        if (ahora - ultimoAtaque < COOLDOWN_ATAQUE_NS) {
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
                    direccion.getX(), direccion.getY(), vistaProyectil
            ));
            ultimoAtaque = ahora;
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
