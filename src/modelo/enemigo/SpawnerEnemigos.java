package modelo.enemigo;

import java.awt.geom.Rectangle2D;
import java.util.Objects;
import java.util.Random;

import motor.modelo.Posicionable;
import motor.util.ContadorSegundos;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorFuerte;

public class SpawnerEnemigos {

    private final EnemigoFactory enemigoFactory;
    private final Notificador<Enemigo> notificadorEnemigoGenerado;
    private final ContadorSegundos cooldownSpawn;
    private final Random random;

    private double minX;
    private double maxX;
    private double minY;
    private double maxY;

    public SpawnerEnemigos(EnemigoFactory enemigoFactory) {
        this(
            enemigoFactory,
            new NotificadorFuerte<>(),
            0.8,
            48.0,
            912.0,
            112.0,
            672.0
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
        this.cooldownSpawn = new ContadorSegundos(cadencia, true);
        this.minX = minX;
        this.maxX = Math.max(minX, maxX);
        this.minY = minY;
        this.maxY = Math.max(minY, maxY);
        this.random = new Random();
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
        cooldownSpawn.tick(dt);
        if (cooldownSpawn.estaListo()) {
            cooldownSpawn.reiniciar();
            Enemigo enemigo = generarEnemigo(velocidadOleada, objetivo);
            if (enemigo != null) {
                notificadorEnemigoGenerado.notificar(enemigo);
            }
        }
    }

    private Enemigo generarEnemigo(double velocidadOleada, Posicionable objetivo) {
        Enemigo enemigo = enemigoFactory.get(objetivo, velocidadOleada);
        Rectangle2D bounds = enemigo.getHitbox().getBounds();
        enemigo.setPosicion(calcularPosicionBorde(bounds.getWidth(), bounds.getHeight()));
        return enemigo;
    }

    public void reiniciar() {
        this.cooldownSpawn.forzarListo();
    }

    public Notificador<Enemigo> getNotificadorEnemigoGenerado() {
        return notificadorEnemigoGenerado;
    }

    private VecDouble2D calcularPosicionBorde(double anchoHitbox, double altoHitbox) {
        double limiteMaxX = Math.max(minX, maxX - anchoHitbox);
        double limiteMaxY = Math.max(minY, maxY - altoHitbox);
        double deltaX = Math.max(1.0, limiteMaxX - minX);
        double deltaY = Math.max(1.0, limiteMaxY - minY);

        int borde = random.nextInt(4);
        return switch (borde) {
            case 0 -> new VecDouble2D(minX + random.nextDouble() * deltaX, minY);
            case 1 -> new VecDouble2D(minX + random.nextDouble() * deltaX, limiteMaxY);
            case 2 -> new VecDouble2D(minX, minY + random.nextDouble() * deltaY);
            default -> new VecDouble2D(limiteMaxX, minY + random.nextDouble() * deltaY);
        };
    }
}
