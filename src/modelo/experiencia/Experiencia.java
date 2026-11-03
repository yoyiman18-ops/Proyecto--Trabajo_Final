package modelo.experiencia;

import java.util.List;

import modelo.TipoInteraccion;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.Interaccion;
import motor.modelo.ModeloEntidad;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.NotificadorFuerte;

public class Experiencia extends Entidad {
    private double cantidadExp;

    public Experiencia(
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
        double cantidadExp,
        String animacionDefault,
        String animacionExpirar
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
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision,
            cantidadExp,
            animacionDefault,
            animacionExpirar
        );
    }

    public Experiencia(
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
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        double cantidadExp,
        String animacionDefault,
        String animacionExpirar
    ) {
        super(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            velocidad != null ? velocidad : new VecDouble2D(),
            aceleracion,
            friccion,
            velocidadMax,
            notificadorPosicion,
            notificadorInstanciacion,
            notificadorExpiracion,
            notificadorSonido,
            notificadorAnimacion,
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision,
            animacionDefault,
            animacionExpirar,
            null
        );
        if (!Double.isFinite(cantidadExp) || cantidadExp <= 0) {
            throw new IllegalArgumentException("La cantidad de experiencia debe ser positiva y finita.");
        }
        this.cantidadExp = cantidadExp;
        añadirInteraccionColision(new Interaccion(TipoInteraccion.GANAREXP.clase(), cantidadExp));
    }

    @Override
    public void tick(double dt) {
        if (!activo()) { return; }
        super.tick(dt);
    }

    @Override
    public boolean interactuar(Interaccion i) {
        TipoInteraccion interaccion = TipoInteraccion.deInt(i.clase());
        if (interaccion == null) { return false; }
        return switch (interaccion) {
            case AÑADIR_EXP -> {
                recoger();
                yield true;
            }
            default -> false;
        };
    }

    public boolean recoger() {
        if (!activo()) { return false; }
        expirar();
        return true;
    }

    public double getCantidadExp() {
        return this.cantidadExp;
    }

    public void setCantidadExp(double cantidadExp) {
        if (!Double.isFinite(cantidadExp) || cantidadExp <= 0) {
            throw new IllegalArgumentException("La cantidad de experiencia debe ser positiva y finita.");
        }
        this.cantidadExp = cantidadExp;
    }
}
