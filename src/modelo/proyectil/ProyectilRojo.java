package modelo.proyectil;

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

public class ProyectilRojo extends Proyectil {

    public ProyectilRojo(
        VecDouble2D posicion,
        Hitbox hitbox,
        double aceleracion,
        double friccion,
        double velocidadMax,
        EstrategiaMovimiento estrategiaMovimiento,
        double dañoAdicional,
        double tiempoVidaAdicional,
        int penetracionAdicional,
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
            estrategiaMovimiento,
            dañoAdicional,
            tiempoVidaAdicional,
            penetracionAdicional,
            estrategiasExpiracion,
            interaccionesColision
        );
    }

    public ProyectilRojo(
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
        EstrategiaMovimiento estrategiaMovimiento,
        double dañoAdicional,
        double tiempoVidaAdicional,
        int penetracionAdicional,
        List<EstrategiaExpiracion> estrategiasExpiracion,
        List<Interaccion> interaccionesColision
    ) {
        super(
            4,
            "Proyectil Rojo",
            hitbox,
            posicion,
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
            1 + tiempoVidaAdicional,
            1 + penetracionAdicional,
            1 + dañoAdicional,
            "idle",
            "expirar"
        );
    }
}
