package modelo.experiencia;

import java.util.List;

import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.Interaccion;
import motor.modelo.ModeloEntidad;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.NotificadorFuerte;

public class ExperienciaGema extends Experiencia {

    public ExperienciaGema(
        VecDouble2D posicion,
        Hitbox hitbox,
        double cantidadExp,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        this(
            posicion,
            hitbox,
            new NotificadorDebil<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            new NotificadorFuerte<>(),
            cantidadExp,
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision
        );
    }

    public ExperienciaGema(
        VecDouble2D posicion,
        Hitbox hitbox,
        Notificador<VecDouble2D> notificadorPosicion,
        Notificador<ModeloEntidad> notificadorInstanciacion,
        Notificador<Integer> notificadorExpiracion,
        Notificador<Integer> notificadorSonido,
        Notificador<String> notificadorAnimacion,
        double cantidadExp,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(
            50,
            "Gema Experiencia",
            hitbox,
            posicion,
            new VecDouble2D(0, 0),
            0.0,
            0.0,
            0.0,
            notificadorPosicion,
            notificadorInstanciacion,
            notificadorExpiracion,
            notificadorSonido,
            notificadorAnimacion,
            estrategiaMovimiento,
            estrategiasExpiracion,
            interaccionesColision,
            cantidadExp,
            "idle",
            "expirar"
        );
    }
}
