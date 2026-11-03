package modelo.experiencia;

import java.util.List;

import controlador.SonidoJuego;
import motor.colisiones.hitboxes.Hitbox;
import motor.modelo.EstrategiaExpiracion;
import motor.modelo.EstrategiaMovimiento;
import motor.modelo.Interaccion;
import motor.modelo.ModeloEntidad;
import motor.util.VecDouble2D;
import motor.util.observer.Notificador;
import motor.util.observer.NotificadorDebil;
import motor.util.observer.NotificadorFuerte;

public class GemaExperiencia extends Experiencia {

    public GemaExperiencia(
        VecDouble2D posicion,
        Hitbox hitbox,
        double aceleracion,
        double friccion,
        double velocidadMax,
        double cantidadExp,
        EstrategiaMovimiento estrategiaMovimiento,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        this(
            posicion,
            hitbox,
            aceleracion,
            friccion,
            velocidadMax,
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

    public GemaExperiencia(
        VecDouble2D posicion,
        Hitbox hitbox,
        double aceleracion,
        double friccion,
        double velocidadMax,
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
            cantidadExp,
            "idle",
            "expirar",
            SonidoJuego.RECOGER_EXPERIENCIA.getId()
        );
    }
}
