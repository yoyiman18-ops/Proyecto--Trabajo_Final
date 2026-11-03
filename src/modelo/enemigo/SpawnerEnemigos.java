package modelo.enemigo;

import java.util.Objects;
import java.util.Random;

import motor.modelo.Posicionable;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorFuerte;

/**
 * Generador espacial y temporal de enemigos en el área de juego.
 * Encapsula la lógica de cadencia, selección de bordes de aparición y
 * parametrización de la fábrica para no sobrecargar ni al modelo de partida
 * ni a los controladores de interfaz.
 */
public class SpawnerEnemigos {

    public static final double CADENCIA_DEFAULT = 0.8;
    public static final double MIN_X_DEFAULT = 48.0;
    public static final double MAX_X_DEFAULT = 960.0 - 48.0 - EsqueletoFactory.ANCHO_HITBOX_ESQUELETO;
    public static final double MIN_Y_DEFAULT = 112.0;
    public static final double MAX_Y_DEFAULT = 720.0 - 48.0 - EsqueletoFactory.ALTO_HITBOX_ESQUELETO;

    private final EnemigoFactory enemigoFactory;
    private final Notificador<Enemigo> notificadorEnemigoGenerado;
    private final Random random;

    private double cadencia;
    private double tiempoAcumulado;
    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    public SpawnerEnemigos(EnemigoFactory enemigoFactory) {
        this(
            enemigoFactory,
            new NotificadorFuerte<>(),
            CADENCIA_DEFAULT,
            MIN_X_DEFAULT,
            MAX_X_DEFAULT,
            MIN_Y_DEFAULT,
            MAX_Y_DEFAULT
        );
    }

    public SpawnerEnemigos(
        EnemigoFactory enemigoFactory,
        Notificador<Enemigo> notificadorEnemigoGenerado,
        double cadencia,
        double minX,
        double maxX,
        double minY,
        double maxY
    ) {
        this.enemigoFactory = Objects.requireNonNull(enemigoFactory, "La fábrica de enemigos no puede ser nula");
        this.notificadorEnemigoGenerado = Objects.requireNonNull(notificadorEnemigoGenerado, "El notificador no puede ser nulo");
        if (!Double.isFinite(cadencia) || cadencia <= 0) {
            throw new IllegalArgumentException("La cadencia de spawn debe ser positiva y finita.");
        }
        this.cadencia = cadencia;
        this.minX = minX;
        this.maxX = Math.max(minX, maxX);
        this.minY = minY;
        this.maxY = Math.max(minY, maxY);
        this.random = new Random();
        this.tiempoAcumulado = this.cadencia;
    }

    public void configurarLimites(double minX, double maxX, double minY, double maxY) {
        this.minX = minX;
        this.maxX = Math.max(minX, maxX);
        this.minY = minY;
        this.maxY = Math.max(minY, maxY);
    }

    public void actualizar(double dt, int cantidadPendiente, double velocidadOleada, Posicionable objetivo) {
        if (cantidadPendiente <= 0 || dt <= 0) {
            return;
        }
        tiempoAcumulado += dt;
        while (tiempoAcumulado >= cadencia && cantidadPendiente > 0) {
            tiempoAcumulado -= cadencia;
            cantidadPendiente--;
            Enemigo enemigo = generarEnemigo(velocidadOleada, objetivo);
            if (enemigo != null) {
                notificadorEnemigoGenerado.notificar(enemigo);
            }
        }
    }

    public Enemigo generarEnemigo(double velocidadOleada, Posicionable objetivo) {
        VecDouble2D pos = calcularPosicionBorde();
        enemigoFactory.setPosicion(pos);
        enemigoFactory.setVelocidadMax(velocidadOleada);
        enemigoFactory.setObjetivo(objetivo);
        return enemigoFactory.get();
    }

    public void reiniciar() {
        this.tiempoAcumulado = this.cadencia;
    }

    public Notificador<Enemigo> getNotificadorEnemigoGenerado() {
        return notificadorEnemigoGenerado;
    }

    public EnemigoFactory getFactory() {
        return enemigoFactory;
    }

    public double getCadencia() {
        return cadencia;
    }

    public void setCadencia(double cadencia) {
        if (!Double.isFinite(cadencia) || cadencia <= 0) {
            throw new IllegalArgumentException("La cadencia debe ser positiva y finita.");
        }
        this.cadencia = cadencia;
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxY() {
        return maxY;
    }

    private VecDouble2D calcularPosicionBorde() {
        int borde = random.nextInt(4);
        double x;
        double y;
        double deltaX = Math.max(1.0, maxX - minX);
        double deltaY = Math.max(1.0, maxY - minY);

        switch (borde) {
            case 0 -> { // Borde superior
                x = minX + random.nextDouble() * deltaX;
                y = minY;
            }
            case 1 -> { // Borde inferior
                x = minX + random.nextDouble() * deltaX;
                y = maxY;
            }
            case 2 -> { // Borde izquierdo
                x = minX;
                y = minY + random.nextDouble() * deltaY;
            }
            default -> { // Borde derecho
                x = maxX;
                y = minY + random.nextDouble() * deltaY;
            }
        }
        return new VecDouble2D(x, y);
    }
}
