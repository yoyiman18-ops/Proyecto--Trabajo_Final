package modelo.enemigo;

import java.util.List;

import modelo.Personaje;
import modelo.TipoInteraccion;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Direccion;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.Interaccion;
import motor.modelo.ModeloEntidad;
import motor.util.ContadorSegundos;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.NotificadorFuerte;

public class Enemigo extends Personaje {

    private final double daño;
    private final ContadorSegundos cooldownAtaque;
    private boolean contactoConObjetivo;
    private Direccion direccion;
    private String animacionActual;

    public Enemigo(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax,
        Notificador<VecDouble2D> notificadorPosicion,
        Notificador<ModeloEntidad> notificadorInstanciacion,
        Notificador<Integer> notificadorExpiracion,
        Notificador<Integer> notificadorSonido,
        Notificador<String> notificadorAnimacion,
        Notificador<Double> notificadorDaño,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        String animacionDefault,
        String animacionExpirar,
        Integer idSonidoExpiracion,
        double vidaMaxima,
        double defensaMaximaPorcentual,
        Integer idSonidoDaño,
        double daño,
        double intervaloAtaque
    ) {
        super(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            velocidad,
            aceleracion,
            friccion,
            velocidadMax,
            notificadorPosicion,
            notificadorInstanciacion,
            notificadorExpiracion,
            notificadorSonido,
            notificadorAnimacion,
            notificadorDaño,
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision,
            animacionDefault,
            animacionExpirar,
            idSonidoExpiracion,
            vidaMaxima,
            defensaMaximaPorcentual,
            idSonidoDaño
        );
        if (!Double.isFinite(daño) || daño < 0) {
            throw new IllegalArgumentException("El daño debe ser finito y no negativo.");
        }
        if (!Double.isFinite(intervaloAtaque) || intervaloAtaque <= 0) {
            throw new IllegalArgumentException("El intervalo de ataque debe ser positivo y finito.");
        }
        this.daño = daño;
        this.cooldownAtaque = new ContadorSegundos(intervaloAtaque, true);
        this.contactoConObjetivo = false;
        this.direccion = Direccion.ABAJO;
        this.animacionActual = animacionDefault;

        if (daño > 0) {
            añadirInteraccionColision(new Interaccion(TipoInteraccion.ATAQUE_ENEMIGO.clase(), daño));
        }
        añadirInteraccionColision(new Interaccion(TipoInteraccion.SOLIDO.clase(), 0.0));
    }

    public Enemigo(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        VecDouble2D velocidad,
        double aceleracion,
        double friccion,
        double velocidadMax,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        String animacionDefault,
        String animacionExpirar,
        Integer idSonidoExpiracion,
        double vidaMaxima,
        double defensaMaximaPorcentual,
        Integer idSonidoDaño,
        double daño,
        double intervaloAtaque
    ) {
        this(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            velocidad,
            aceleracion,
            friccion,
            velocidadMax,
            new NotificadorDebil<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision,
            animacionDefault,
            animacionExpirar,
            idSonidoExpiracion,
            vidaMaxima,
            defensaMaximaPorcentual,
            idSonidoDaño,
            daño,
            intervaloAtaque
        );
    }

    @Override
    public void tick(double dt) {
        if (!activo()) {
            return;
        }
        cooldownAtaque.tick(dt);

        if (contactoConObjetivo) {
            getVelocidad().setX(0.0);
            getVelocidad().setY(0.0);
            contactoConObjetivo = false;
            return;
        }

        super.tick(dt);

        VecDouble2D vel = getVelocidad();
        if (vel.getX() != 0.0 || vel.getY() != 0.0) {
            if (Math.abs(vel.getX()) >= Math.abs(vel.getY())) {
                direccion = vel.getX() >= 0.0 ? Direccion.DERECHA : Direccion.IZQUIERDA;
            } else {
                direccion = vel.getY() >= 0.0 ? Direccion.ABAJO : Direccion.ARRIBA;
            }
            if (cooldownAtaque.estaListo()) {
                String animacionMovimiento = obtenerAnimacionMovimiento(direccion);
                if (!animacionMovimiento.equals(animacionActual)) {
                    animacionActual = animacionMovimiento;
                    getNotificadorAnimacion().notificar(animacionActual);
                }
            }
        }
    }

    private String obtenerAnimacionMovimiento(Direccion dir) {
        return switch (dir) {
            case ARRIBA -> "caminar_arriba";
            case ABAJO -> "caminar_abajo";
            case IZQUIERDA -> "caminar_izquierda";
            case DERECHA -> "caminar_derecha";
        };
    }

    private String obtenerAnimacionAtaque(Direccion dir) {
        return switch (dir) {
            case ARRIBA -> "atacar_arriba";
            case ABAJO -> "atacar_abajo";
            case IZQUIERDA -> "atacar_izquierda";
            case DERECHA -> "atacar_derecha";
        };
    }

    @Override
    public List<Interaccion> getInteracciones() {
        if (!activo()) {
            return List.of();
        }
        if (cooldownAtaque.estaListo() && daño > 0) {
            return super.getInteracciones();
        }
        return List.of(new Interaccion(TipoInteraccion.SOLIDO.clase(), 0.0));
    }

    @Override
    public boolean interactuar(Interaccion i) {
        if (i == null) {
            return false;
        }
        TipoInteraccion interaccion = TipoInteraccion.deInt(i.clase());
        if (interaccion == TipoInteraccion.AÑADIR_EXP) {
            this.contactoConObjetivo = true;
            if (cooldownAtaque.estaListo()) {
                cooldownAtaque.reiniciar();
                animacionActual = obtenerAnimacionAtaque(direccion);
                getNotificadorAnimacion().notificar(animacionActual);
            }
            return true;
        }
        return super.interactuar(i);
    }
}
