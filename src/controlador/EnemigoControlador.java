package controlador;

import javafx.geometry.Point2D;
import modelo.EntidadViva;
import modelo.EntidadMovil;
import modelo.Vec2;
import vista.SpriteVista;

/** Controla el comportamiento básico de un enemigo que persigue al jugador. */
public class EnemigoControlador {

    private final EntidadViva modelo;
    private final EntidadViva objetivo;
    private final SpriteVista vista;
    private final Runnable alMorir;
    private final double posicionInicialX;
    private final double posicionInicialY;
    private long momentoRespawn;
    private long ultimoAtaque;
    private boolean muerteRegistrada;
    private static final long TIEMPO_RESPAWN_NS = 2_000_000_000L;
    private static final long COOLDOWN_ATAQUE_NS = 700_000_000L;
    private static final int DAÑO_CONTACTO = 1;

    public EnemigoControlador(EntidadViva modelo, EntidadViva objetivo, SpriteVista vista) {
        this(modelo, objetivo, vista, () -> {});
    }

    public EnemigoControlador(EntidadViva modelo, EntidadViva objetivo,
                              SpriteVista vista, Runnable alMorir) {
        if (modelo == null || objetivo == null || vista == null) {
            throw new IllegalArgumentException("El enemigo, el objetivo y la vista son obligatorios");
        }
        if (alMorir == null) {
            throw new IllegalArgumentException("El callback de muerte no puede ser null");
        }
        this.modelo = modelo;
        this.objetivo = objetivo;
        this.vista = vista;
        this.alMorir = alMorir;
        Vec2 posicionInicial = modelo.getPosicion();
        this.posicionInicialX = posicionInicial.getX();
        this.posicionInicialY = posicionInicial.getY();
        vista.actualizar(modelo);
    }

    /** Actualiza la dirección, el movimiento y la representación del enemigo. */
    public void actualizar() {
        if (!modelo.estaVivo()) {
            if (System.nanoTime() >= momentoRespawn) {
                modelo.setPosicion(posicionInicialX, posicionInicialY);
                modelo.revivir();
                muerteRegistrada = false;
                vista.setVisible(true);
                vista.actualizar(modelo);
            }
            return;
        }

        Vec2 posicionEnemigo = modelo.getPosicion();
        Vec2 posicionObjetivo = objetivo.getPosicion();
        Point2D diferencia = new Point2D(
                posicionObjetivo.getX() - posicionEnemigo.getX(),
                posicionObjetivo.getY() - posicionEnemigo.getY()
        );

        if (diferencia.magnitude() > 0) {
            modelo.setDireccion(diferencia.getX(), diferencia.getY());
            modelo.acelerar();
            modelo.mover();
        } else {
            modelo.frenar();
        }

        atacarSiCorresponde();
        vista.actualizar(modelo);
    }

    public void recibirAtaque(int daño) {
        if (!modelo.estaVivo()) {
            return;
        }
        modelo.recibirDaño(daño);
        if (!modelo.estaVivo()) {
            vista.setVisible(false);
            momentoRespawn = System.nanoTime() + TIEMPO_RESPAWN_NS;
            if (!muerteRegistrada) {
                muerteRegistrada = true;
                alMorir.run();
            }
        }
    }

    private void atacarSiCorresponde() {
        if (distanciaAl(objetivo) > 20 || !objetivo.estaVivo()) {
            return;
        }
        long ahora = System.nanoTime();
        if (ahora - ultimoAtaque >= COOLDOWN_ATAQUE_NS) {
            objetivo.recibirDaño(DAÑO_CONTACTO);
            ultimoAtaque = ahora;
        }
    }

    public double distanciaAl(EntidadMovil entidad) {
        Vec2 posicion = modelo.getPosicion();
        Vec2 objetivo = entidad.getPosicion();
        double dx = objetivo.getX() - posicion.getX();
        double dy = objetivo.getY() - posicion.getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    public boolean estaVivo() {
        return modelo.estaVivo();
    }

    public EntidadViva getModelo() {
        return modelo;
    }
}
