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
        String animacionExpirar,
        Integer idSonidoExpiracion
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
            animacionExpirar,
            idSonidoExpiracion
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
        String animacionExpirar,
        Integer idSonidoExpiracion
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
            idSonidoExpiracion
        );
        if (!Double.isFinite(cantidadExp) || cantidadExp <= 0) {
            throw new IllegalArgumentException("La cantidad de experiencia debe ser positiva y finita.");
        }
        añadirInteraccionColision(new Interaccion(TipoInteraccion.GANAREXP.clase(), cantidadExp));
    }

    @Override
    public boolean interactuar(Interaccion i) {
        if (i == null || !activo()) {
            return false;
        }
        TipoInteraccion interaccion = TipoInteraccion.deInt(i.clase());
        if (interaccion == TipoInteraccion.AÑADIR_EXP) {
            expirar();
            return true;
        }
        return false;
    }
}
