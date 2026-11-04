package modelo.proyectil;

import java.util.List;

import modelo.TipoInteraccion;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.Entidad;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.Interaccion;
import motor.modelo.ModeloEntidad;
import motor.util.ContadorSegundos;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.NotificadorFuerte;

public class Proyectil extends Entidad {
    private final ContadorSegundos contadorVida;
    private int penetracion;

    public Proyectil(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
        double aceleracion,
        double friccion,
        double velocidadMax,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision,
        double tiempoVida,
        int penetracion,
        double daño,
        String animacionDefault,
        String animacionExpirar
    ) {
        this(
            idGlobal,
            nombre,
            hitbox,
            posicion,
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
            tiempoVida,
            penetracion,
            daño,
            animacionDefault,
            animacionExpirar,
            null
        );
    }

    public Proyectil(
        int idGlobal,
        String nombre,
        Hitbox hitbox,
        VecDouble2D posicion,
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
        double tiempoVida,
        int penetracion,
        double daño,
        String animacionDefault,
        String animacionExpirar,
        Integer idSonidoExpiracion
    ) {
        super(
            idGlobal,
            nombre,
            hitbox,
            posicion,
            new VecDouble2D(0, 0),
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
            idSonidoExpiracion
        );
        if (!Double.isFinite(tiempoVida) || tiempoVida <= 0) {
            throw new IllegalArgumentException("El tiempo de vida debe ser positivo y finito.");
        }
        if (daño > 0) {
            añadirInteraccionColision(new Interaccion(TipoInteraccion.DAÑAR.clase(), daño));
        }
        this.contadorVida = new ContadorSegundos(tiempoVida);
        this.penetracion = Math.max(-1, penetracion);
    }

    @Override
    public void tick(double dt) {
        if (!activo()) { return; }
        contadorVida.tick(dt);
        super.tick(dt);
        if (contadorVida.estaListo()) { expirar(); }
    }

    @Override
    public boolean interactuar(Interaccion i) {
        if (i == null) { return false; }
        TipoInteraccion interaccion = TipoInteraccion.deInt(i.clase());
        if (interaccion == null) { return false; }
        return switch (interaccion) {
            case DAÑAR -> recibirDaño(i.valor());
            case SOLIDO -> recibirDaño(Math.max(1.0, i.valor()));
            default -> false;
        };
    }

    protected boolean recibirDaño(double daño) {
        if (!activo() || penetracion < 0) { return false; }
        penetracion -= daño;
        if (penetracion <= 0) { expirar(); }
        return true;
    }
}
